package cn.ve.crawler;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author ve
 * @date 2020/4/2 19:01
 */
public class HttpGetTest {
    public static void main(String[] args) throws URISyntaxException {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建HttpGet对象,设置url访问地址
        URIBuilder uriBuilder = new URIBuilder("https://www.88ys.com/");
        uriBuilder.addParameter("aa", "bb");
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        // 配置请求信息
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1000) // 创建连接最长时间
                .setConnectionRequestTimeout(500) // 获取连接最长时间
                .setSocketTimeout(10 * 1000) // 传输数据最长时间
                .build();
        // 设置进去
        httpGet.setConfig(config);

        // 使用HttpClient发起请求,获取response
        try (CloseableHttpResponse response = httpClient.execute(httpGet);) {
            if (response.getStatusLine().getStatusCode() == 200) {
                // 解析响应
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
