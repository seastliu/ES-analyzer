package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.analysis.PinyinConfig;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

public class MultiplePinyinTokenizerFactory extends AbstractTokenizerFactory {

    private PinyinConfig config;

    public MultiplePinyinTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
        config=new PinyinConfig(settings);
    }

    @Override
    public Tokenizer create() {
            return new MultiplePinyinTokenizer(config);
    }
}

