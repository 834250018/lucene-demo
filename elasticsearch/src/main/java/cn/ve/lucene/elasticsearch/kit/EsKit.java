package cn.ve.lucene.elasticsearch.kit;

import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * es官方客户端
 *
 * @author ve
 * @date 2020/3/31 17:02
 */
public enum EsKit {
    ;

    /**
     * 创建索引
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public static boolean createIndex(String indexName) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.indices().create(new CreateIndexRequest(indexName), RequestOptions.DEFAULT).isAcknowledged();
        }
    }

    /**
     * 创建索引
     *
     * @param indexName
     * @param settings
     * @param mappings
     * @param alias
     * @return
     * @throws IOException
     */
    public static boolean createIndex(String indexName, Object settings, Object mappings, String alias) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
            if (settings != null) {
                if (settings instanceof String) {
                    indexRequest.settings(String.valueOf(settings), Requests.INDEX_CONTENT_TYPE);
                } else if (settings instanceof Map) {
                    indexRequest.settings((Map) settings);
                } else if (settings instanceof XContentBuilder) {
                    indexRequest.settings((XContentBuilder) settings);
                } else if (settings instanceof Settings.Builder) {
                    indexRequest.settings((Settings.Builder) settings);
                }
            }
            if (mappings != null) {
                if (mappings instanceof String) {
                    indexRequest.mapping(String.valueOf(mappings), XContentType.JSON);
                } else if (mappings instanceof Map) {
                    indexRequest.mapping((Map) mappings);
                } else if (mappings instanceof XContentBuilder) {
                    indexRequest.mapping((XContentBuilder) mappings);
                }
            }
            if (StringUtils.isEmpty(alias)) {
                indexRequest.alias(new Alias(String.valueOf(alias)));
            }
            return client.indices().create(indexRequest, RequestOptions.DEFAULT).isAcknowledged();
        }
    }

    /**
     * 给已存在的index设置Mapping
     *
     * @param indexName
     * @param mappings
     * @return
     * @throws IOException
     */
    public static boolean putMapping(String indexName, Object mappings) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);

            if (mappings instanceof String) {
                putMappingRequest.source(String.valueOf(mappings), XContentType.JSON);
            } else if (mappings instanceof Map) {
                putMappingRequest.source((Map) mappings);
            } else if (mappings instanceof XContentBuilder) {
                putMappingRequest.source((XContentBuilder) mappings);
            }
            return client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT).isAcknowledged();
        }
    }

    /**
     * 查询匹配总数
     *
     * @param searchRequest
     * @return
     * @throws IOException
     */
    public static long count(SearchRequest searchRequest) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.search(searchRequest, RequestOptions.DEFAULT).getHits().getTotalHits().value;
        }
    }

    /**
     * 保存或更新
     *
     * @param indexRequest
     * @return
     * @throws IOException
     */
    public static String index(IndexRequest indexRequest) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.index(indexRequest, RequestOptions.DEFAULT).getId();
        }
    }

    public static String index(String indexName, Map<String, Object> map) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                builder.field(entry.getKey(), entry.getValue());
            }
        }
        builder.endObject();
        return index(new IndexRequest().index(indexName).source(builder));
    }

    /**
     * 更新
     *
     * @param updateRequest
     * @return
     * @throws IOException
     */
    public static String update(UpdateRequest updateRequest) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.update(updateRequest, RequestOptions.DEFAULT).getId();
        }
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @return
     * @throws IOException
     */
    public static String delete(DeleteRequest deleteRequest) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.delete(deleteRequest, RequestOptions.DEFAULT).getId();
        }
    }

    /**
     * 删除索引
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public static boolean deleteIndex(String indexName) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT).isAcknowledged();
        }
    }

    /**
     * 批量操作
     *
     * @param indexRequest
     * @return
     * @throws IOException
     */
    public static BulkResponse bulkIndex(DocWriteRequest... indexRequest) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(indexRequest);
        return bulkIndex(bulkRequest);
    }

    public static BulkResponse bulkIndex(BulkRequest bulkRequest) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.bulk(bulkRequest, RequestOptions.DEFAULT);
        }
    }

    /**
     * 检查索引是否存在
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public static boolean indexExists(String indexName) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
        }
    }

    /**
     * 刷新零到多个索引
     *
     * @param indexName
     * @throws IOException
     */
    public static void indexRefresh(String... indexName) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            RefreshRequest refreshRequest = new RefreshRequest(indexName);
            client.indices().refresh(refreshRequest, RequestOptions.DEFAULT);
        }
    }

    /**
     * 检查别名是否存在
     *
     * @param alias
     * @return
     * @throws IOException
     */
    public static boolean typeExists(String alias) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.indices().existsAlias(new GetAliasesRequest(alias), RequestOptions.DEFAULT);
        }
    }

    /**
     * 别名增加或删除
     *
     * @param indexName
     * @param alias
     * @param type
     * @return
     * @throws IOException
     */
    public static boolean genAlias(String indexName, String alias, IndicesAliasesRequest.AliasActions.Type type) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
            indicesAliasesRequest.addAliasAction(new IndicesAliasesRequest.AliasActions(type).indices(indexName).alias(alias));
            return client.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT).isAcknowledged();
        }
    }

    /**
     * 别名迁移
     *
     * @param oldIndexName
     * @param newIndexName
     * @param alias
     * @return
     * @throws IOException
     */
    public static boolean updateAlias(String oldIndexName, String newIndexName, String alias) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
            IndicesAliasesRequest.AliasActions removeAlias = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.REMOVE).indices(oldIndexName).alias(alias);
            IndicesAliasesRequest.AliasActions addAlias = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD).indices(newIndexName).alias(alias);
            indicesAliasesRequest.addAliasAction(removeAlias).addAliasAction(addAlias);
            return client.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT).isAcknowledged();
        }
    }

    /**
     * 查询所有
     *
     * @return
     * @throws IOException
     */
    public static SearchResponse queryAll() throws IOException {
        try (RestHighLevelClient client = getClient()) {
            SearchRequest searchRequest = new SearchRequest();
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchRequest.source(searchSourceBuilder);
            return client.search(searchRequest, RequestOptions.DEFAULT);
        }
    }

    /**
     * 查询指定索引中所有
     *
     * @return
     * @throws IOException
     */
    public static SearchResponse queryAll(String indexName) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchRequest.source(searchSourceBuilder);
            return client.search(searchRequest, RequestOptions.DEFAULT);
        }
    }

    public static SearchResponse query(String indexName, SearchSourceBuilder builder) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.search(new SearchRequest(indexName).source(builder), RequestOptions.DEFAULT);
        }
    }

    public static SearchResponse query(String indexName, String field, String value) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery(field, value));
        return query(indexName, sourceBuilder);
    }

    public static SearchResponse query(SearchRequest searchRequest) throws IOException {
        try (RestHighLevelClient client = getClient()) {
            return client.search(searchRequest, RequestOptions.DEFAULT);
        }
    }

    /**
     * 分词搜索
     *
     * @param indexName
     * @param ayalyzer  ik_smart
     * @param field
     * @param value
     * @return
     * @throws IOException
     */
    public static SearchResponse query(String indexName, String ayalyzer, String field, String value) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery(field, value).analyzer(ayalyzer));
        return query(indexName, sourceBuilder);
    }

    /**
     * 基础条件搜索
     *
     * @param termQuery 条件查询
     * @param includes  包含域
     * @param excludes  不包含域
     * @param sortField 排序域
     * @param sortOrder 升序/降序
     * @param from      默认0
     * @param size      默认10
     * @param time      超时时间
     * @param timeUnit  超时时间单位
     * @return
     * @throws IOException
     */
    public static SearchSourceBuilder sourceBuild(Map<String, Object> termQuery, String[] includes, String[] excludes, String sortField, SortOrder sortOrder, int from, int size, int time, TimeUnit timeUnit) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        for (Map.Entry<String, Object> entry : termQuery.entrySet()) {
            sourceBuilder.query(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
        }
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        sourceBuilder.timeout(new TimeValue(time, timeUnit));
        sourceBuilder.sort(new FieldSortBuilder(sortField).order(sortOrder));
        sourceBuilder.fetchSource(includes, excludes);
        return sourceBuilder;
    }


    public static RestHighLevelClient getClient() {
        return new RestHighLevelClient(RestClient.builder(
//                new HttpHost("localhost", 9201, "http"),
//                new HttpHost("localhost", 9202, "http"),
                new HttpHost("localhost", 9200, "http")
        ));
    }

}
