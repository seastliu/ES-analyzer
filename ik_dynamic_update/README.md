IK Analysis for Elasticsearch
=============================

因为涉及到ES连接MySQL，故需要在ES所在机器上添加DB访问权限，相关配置如下：
${JAVA_HOME}/jre/lib/security下java.policy添加一下配置
permission java.net.SocketPermission "*", "connect,resolve";
//mysql-connector-java-8需要添加如下配置
permission java.lang.RuntimePermission "setContextClassLoader";

这是基于Elasticsearch 6.4.2的elasticsearch-analysis-ik-6.4.2分词器，原本的分词器不支持MySQL动态扩展词库和停用词库；

本项目修改了下源码使IK Analysis支持mysql创建扩展词库和停用词库，配置很简单

- clone 本项目

- 直接`mvn package`

已有插件：

- 解压 target/releases/elasticsearch-analysis-ik-6.4.0.zip

- 将 elasticsearch-analysis-ik-6.4.0.jar 放到$ES_HOME$/plugins/analysis-ik/下覆盖

- 将 druid-1.1.10.jar 放到$ES_HOME$/plugins/analysis-ik/下

- 将 plugin-security.policy 放到$ES_HOME$/plugins/analysis-ik/ 下覆盖

- 将 config/jdbc.properties 修改为你自己的信息并放到$ES_HOME$/config/analysis-ik/下

- 重启ES

新装插件：

- 解压 target/releases/elasticsearch-analysis-ik-6.4.0.zip

- 将解压内容放到$ES_HOME$/plugins/analysis-ik目录下

- 将 config/jdbc.properties 修改为你自己的信息并放到$ES_HOME$/config/analysis-ik/下

