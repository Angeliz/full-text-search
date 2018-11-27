import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.memect.neeqimport.domain.Enterprise;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EsUtil {

    /**
     * 创建新的索引
     * @param client
     * @param index
     * @param mapping
     */
    public static void createNewIndex(TransportClient client, String index, String mapping) {
        CreateIndexResponse response = client.admin().indices()
                .prepareCreate(index)
                .setSource(mapping)
                .execute().actionGet();

        if (response.isAcknowledged()) {
            System.out.println("Index created");
        } else {
            System.out.println("Index creation failed");
        }
    }

    /**
     * 删除已存在的索引后，重新创建索引
     * @param client TransportClient对象
     * @param index 索引名称
     * @param mapping json格式的mapping设置字符串
     */
    public static void createIndex(TransportClient client, String index, String mapping) {
        // 判断索引是否存在，如果存在则删除索引
        boolean indexExists = indexExist(client, index);
        if (indexExists) {
            boolean deleteIndex = deleteIndex(client, index);
            if (!deleteIndex) {
                System.out.println("Delete index: { " + index + " } failed.");
                return;
            }
        }

        // 重新创建索引
        createNewIndex(client, index, mapping);
    }

    /**
     * 删除索引
     * @param indexName 索引名
     * @return
     */
    public static boolean deleteIndex(TransportClient client, String indexName) {
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        DeleteIndexResponse response = indicesAdminClient
                .prepareDelete(indexName).execute().actionGet();

        return response.isAcknowledged();
    }

    /**
     * 创建索引
     * @param clusterName
     * @param ip
     * @param port
     * @param index
     * @param mapping
     */
    public static void createIndex(String clusterName, String ip, int port, String index, String mapping) {
        TransportClient client = getClient(clusterName, ip, port);

        createIndex(client, index, mapping);
    }

    /**
     * 判断索引是否存在
     * @param client
     * @param index
     * @return
     */
    public static boolean indexExist(TransportClient client, String index){
        IndicesExistsRequest request = new IndicesExistsRequest(index);

        IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();
        if (response.isExists()) {
            return true;
        }

        return false;
    }


    /**
     * 获取客户端
     * @param clusterName
     * @param ip
     * @param port
     * @return
     */
    public static TransportClient getClient(String clusterName, String ip, int port) {
        // 初始化并设置Setting对象
        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .build();

        // 初始化客户端
        TransportClient client = null;

        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }

        return client;
    }

    /**
     * 将一个对象变为一个json字符串
     * @param object
     * @return
     */
    public static String object2Json(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String responseValue = null;

        try {
            responseValue = mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseValue;
    }

    /**
     * 插入数据
     * @param client
     * @param index
     * @param type
     * @param object
     * @param unqId
     */
    public static void insertData(TransportClient client, String index, String type, Object object, String unqId) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String json = objectMapper.writeValueAsString(object);

            IndexResponse response = client
                    .prepareIndex(index, type, unqId)
                    .setSource(json)
                    .get();
            String responseStatus = response.status().toString();
            if (responseStatus.equals("OK") || responseStatus.equals("CREATED")) {
                System.out.println("succeed insert the data of unqId = " + unqId);
            } else {
                System.out.println("failed insert the data of unqId = " + unqId);
            }
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            System.out.println("Java bean bind json failed.");
        }
    }

    /**
     * 根据查询请求进行搜索，并返回查询结果
     * @param client 客户端
     * @param index 索引名
     * @param type 类型
     * @param queryBuilder 查询条件
     * @param page 分页查询的页码
     * @param pageCount 每次返回满足查询条件的条数
     * @param highlightBuilder 高亮命中查询条件的内容的对象
     * @return
     */
//    public SearchResponse getResponse(TransportClient client, String index, String type, QueryBuilder queryBuilder, int page, int pageCount, HighlightBuilder highlightBuilder) {
//        if (query == null || query.replaceAll("\\s", "").length() == 0) return null;
//
//        SearchResponse response = null;
//
//        try {
//            response = client.prepareSearch(index)
//                    .setTypes(type)
//                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)  // 设置查询方式
//                    .setQuery(queryBuilder)
//                    .setFetchSource(new String[]{"title", "tag", "content"}, null)
//                    // 第一个参数设置需要返回的字段，第二个参数设置不需要返回的字段。
//                    // 不设置setFetchSource时，默认返回所有字段。
//                    .setFrom(page - 1)                // 分页查询的页码
//                    .setSize(pageCount)               // 每次返回满足查询条件的条数
//                    .highlighter(highlightBuilder)    // 高亮命中查询条件的内容
//                    .get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }
}
