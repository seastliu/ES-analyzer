# ES-analyzer
ik_dynamic_update和synonym_dynamic_update支持通过Mysql进行动态配置，使用前需要在配置文件中进行DB连接配置
## ik_dynamic_update
支持通过MySQL配置IK扩展词、停用词热更新

## synonym_dynamic_update
支持通过MySQL配置SYNONYM同义词热更新

```
# 是否打开数据库同义词库(true/false)
enable.synonym=true
url=
user=
password=

# 用户可以在这里配置MySQL同义词表名
synonym.table=dynamic_synonym_rule
# 用户可以在这里配置MySQL同义词表字段名称
synonym.field.name=word

# DB自定义词库同步内存刷新时间（单位秒）
refresh.time.interval=1800
```

```
es setting配置
{
  "index": {
    "analysis": {
      "filter": {
        "dynamic_synonym_filter": {
          "expand": true,
          "ignore_case": true,
          "type": "dynamic-synonym"
        }
      },
      "analyzer": {
        "ikMaxSynonymAnalyzer": {
          "tokenizer": "ik_max_word",
          "filter": [
            "dynamic_synonym_filter","unique"
          ]
        }
      }
    }
  }
}

es mapping配置
{
      "properties": {
        "title": {
          "type": "text",
          "fields": {
            "raw": {
              "type": "text",
              "analyzer": "ikMaxSynonymAnalyzer",
              "search_analyzer": "ik_max_word"
            }
          }
        }
      }
}
```
## pinyin-multiple
支持多音词全拼音的解析，内部涉及nlp-lang jar包依赖，故需要先部署新版的nlp-lang，再部署pinyin-multiple
```
es setting配置
{
  "index": {
    "analysis": {
      "filter": {
	    "multi_pinyin_simple_filter": {
          "type" : "multiple_pinyin",
          "keep_first_letter":true,
          "keep_separate_first_letter" : false,
          "keep_full_pinyin" : false,
          "keep_original" : false,
          "limit_first_letter_length" : 50,
          "lowercase" : true
        },
        "multi_pinyin_full_filter":{
          "type" : "multiple_pinyin",
          "keep_first_letter":false,
          "keep_separate_first_letter" : false,
          "keep_full_pinyin" : false,
          "none_chinese_pinyin_tokenize":true,
          "keep_original" : false,
          "limit_first_letter_length" : 50,
          "lowercase" : true,
          "keep_joined_full_pinyin": true,
          "keep_none_chinese_in_joined_full_pinyin":true
        }
      },
      "analyzer": {
        "multiPinyinSimpleIndexAnalyzer": {
          "tokenizer": "keyword",
          "filter": [
            "cjk_width",
            "multi_pinyin_simple_filter",
            "ngram_filter",
            "lowercase"
          ]
        },
        "multiPinyiFullIndexAnalyzer":{
          "tokenizer" : "keyword",
          "filter": ["cjk_width", "ngram_filter", "multi_pinyin_full_filter", "lowercase"]
        }
      }
    }
  }
}
es mapping配置
{
  "properties": {
	"title": {
	  "type": "text",
	  "fields": {
		"pinyin": {
		  "type": "text",
		  "analyzer": "multiPinyinSimpleIndexAnalyzer"
		},
		"pinyinFull": {
		  "type": "text",
		  "analyzer": "multiPinyiFullIndexAnalyzer"
		}
	  }
	}
}

```
