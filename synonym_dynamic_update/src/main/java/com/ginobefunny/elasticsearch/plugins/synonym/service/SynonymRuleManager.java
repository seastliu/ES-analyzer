/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ginobefunny.elasticsearch.plugins.synonym.service;

import com.ginobefunny.elasticsearch.plugins.synonym.DynamicSynonymPlugin;
import com.ginobefunny.elasticsearch.plugins.synonym.service.db.JDBCUtils;
import com.ginobefunny.elasticsearch.plugins.synonym.service.db.Monitor;
import com.ginobefunny.elasticsearch.plugins.synonym.service.db.QueryDbDto;
import com.mysql.cj.core.util.StringUtils;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.io.PathUtils;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ginozhang on 2017/1/12.
 */
public class SynonymRuleManager {

    private static final Logger LOGGER = ESLoggerFactory.getLogger(Monitor.class.getName());

    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    private static SynonymRuleManager singleton;

    private Configuration configuration;

    private SimpleSynonymMap synonymMap;

    private Properties jdbcProps;

    private Date synonymUpdateDate = null;

    private final static String JDBC_FILE_NAME = "jdbc.properties";
    // jdbc.properties配置信息
    private final static String ENABLE_SYNONYM = "enable.synonym";
    private final static String SYNONYM_TABLE = "synonym.table";
    private final static String SYNONYM_FIELD_NAME = "synonym.field.name";
    private final static String REFRESH_TIME_INTERVAL = "refresh.time.interval";

    public static synchronized SynonymRuleManager initial(Configuration cfg) {
        if (singleton == null) {
            synchronized (SynonymRuleManager.class) {
                if (singleton == null) {
                    singleton = new SynonymRuleManager();
                    //时间戳初始化
                    singleton.synonymUpdateDate = new Date();
                    singleton.configuration = cfg;
                    singleton.jdbcProps = new Properties();
                    singleton.loadJDBCProperties();
                    singleton.synonymMap = new SimpleSynonymMap(cfg);

                    if (Boolean.valueOf(singleton.jdbcProps.getProperty(ENABLE_SYNONYM))) {
                        int timeInterval = Integer.valueOf(singleton.jdbcProps.getProperty(
                                REFRESH_TIME_INTERVAL, "1800"));
                        // 全量加载自定义同义词
                        singleton.reloadSynonymRule();
                        pool.scheduleAtFixedRate(() -> singleton.incrementLoadSynonymRule(),
                                timeInterval, timeInterval, TimeUnit.SECONDS);
                    }
                }
            }
        }

        return singleton;
    }

    public static SynonymRuleManager getSingleton() {
        if (singleton == null) {
            throw new IllegalStateException("Please initial first.");
        }
        return singleton;
    }

    public List<String> getSynonymWords(String inputToken) {
        if (this.synonymMap == null) {
            return null;
        }

        return this.synonymMap.getSynonymWords(inputToken);
    }

    private void reloadSynonymRule() {
        LOGGER.info("## begin reload synonym rule 扩展同义词：");
        loadSynonymRule(null);
    }

    private void incrementLoadSynonymRule() {
        LOGGER.info("## begin increment synonym rule 扩展同义词：");
        Timestamp startTime = new Timestamp(synonymUpdateDate.getTime());
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        String condition = "where updatetime >= \'" + startTime + "\' and updatetime < \'" + endTime + "\'";
        loadSynonymRule(condition);
    }

    public void loadSynonymRule(String condition) {
        String table = jdbcProps.getProperty(SYNONYM_TABLE);
        String field = jdbcProps.getProperty(SYNONYM_FIELD_NAME);
        String sql = String.join(" ", "SELECT", field, "FROM", table);
        if (!StringUtils.isNullOrEmpty(condition)) {
            sql = String.join(" ", sql, condition);
        }

        QueryDbDto queryDbDto = new QueryDbDto();
        queryDbDto.setUrl(jdbcProps.getProperty("url"));
        queryDbDto.setUser(jdbcProps.getProperty("user"));
        queryDbDto.setPassword(jdbcProps.getProperty("password"));
        queryDbDto.setSql(sql);
        List<String> wordList = JDBCUtils.queryWordList(queryDbDto);
        if(Objects.isNull(wordList)|| wordList.size()==0){
            LOGGER.info("\"数据库里的同义词为空，不用加载到词典中 ");
            return;
        }

        for (String theWord : wordList) {
            if (theWord != null && !"".equals(theWord.trim())) {
                LOGGER.info(theWord);
                this.synonymMap.addRule(theWord);
            }
        }
        // 更新完之后当前的时间 对时间戳进行更新
        synonymUpdateDate = new Date();
    }

    private void loadJDBCProperties() {
        File file = PathUtils.get(new File(DynamicSynonymPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                        .getParent(), "config").toAbsolutePath().resolve(JDBC_FILE_NAME).toFile();
        LOGGER.info("加载DynamicSynonym sql properties file" + file);
        try {
            jdbcProps.load(new FileReader(file));
        } catch (IOException e) {
            LOGGER.error("加载数据库属性文件" + JDBC_FILE_NAME + "失败！, error is ", e);
        }
    }
}
