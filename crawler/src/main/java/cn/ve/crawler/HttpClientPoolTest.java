package cn.ve.crawler;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ve
 * @date 2020/4/2 20:54
 */
public class HttpClientPoolTest {
    public static void main(String[] args) throws Exception {
        // 创建连接池管理器
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        // 设置最大连接数
        connectionManager.setMaxTotal(100);

        // 设置每个主机的最大连接数
        connectionManager.setDefaultMaxPerRoute(10);

        // 使用连接池管理器发起请求
        doGet(connectionManager);
        doPost(connectionManager);
    }

    private static void doGet(PoolingHttpClientConnectionManager connectionManager) throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        URIBuilder uriBuilder = new URIBuilder("https://www.88ys.com/");
        uriBuilder.addParameter("aa", "bb");
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        try (CloseableHttpResponse response = httpClient.execute(httpGet);) {
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content.length() + "11111111");
            }
        }
    }

    private static void doPost(PoolingHttpClientConnectionManager connectionManager) throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        // post请求体
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("aa", "bb"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs, "utf8");

        HttpPost httpPost = new HttpPost("https://www.baidu.com/");
        httpPost.setEntity(formEntity);
        try (CloseableHttpResponse response = httpClient.execute(httpPost);) {
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content + "11111111");
            }
        }
    }
}
