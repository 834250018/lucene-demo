package cn.ve.lucene.elasticsearch;

import java.io.IOException;

/**
 * @author ve
 * @date 2020/4/1 11:05
 */
public class EsMain {
    public static void main(String[] args) throws IOException {
        // 创建索引
//        EsKit.createIndex("test");

        // 为索引创建mappings信息
        /*XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("id");
                {
                    builder.field("type", "long");
                    builder.field("store", true);
                }
                builder.endObject();
                builder.startObject("title");
                {
                    builder.field("type", "text");
                    builder.field("store", true);
                    builder.field("analyzer", "ik_smart");
                }
                builder.endObject();
                builder.startObject("content");
                {
                    builder.field("type", "text");
                    builder.field("store", true);
                    builder.field("analyzer", "ik_smart");
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();

        EsKit.putMapping("test", builder);*/

        // 创建数据
        /*XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("id", 12112L);
            builder.field("title", "尲尬”，最早见于《说文解字》：“尲尬、行不正也。”普遍用于《水浒传》《西游 记》《红楼梦》等元明清时期的小说当中，南方方言中也多有使用，如");
            builder.field("content", "通话。通常是说人遇到的一种情景，让人觉得窘迫。都形容在心理学上，意味着自身或见到他人，在某种场合下，被逼迫到窘境，发生了无法预料的情况，而体现出的不知所措");
        }
        builder.endObject();
        EsKit.index(new IndexRequest().index("test").source(builder));*/

        // 检索数据
//        SearchResponse searchResponse = EsKit.query("test", "content", "形容");
//        System.out.println(searchResponse.toString());


        // 分词检索数据
//        SearchResponse searchResponse = EsKit.query("test", "ik_smart", "content", "自身或见到 发生了无法预料");
//        System.out.println(searchResponse.toString());
    }
}
