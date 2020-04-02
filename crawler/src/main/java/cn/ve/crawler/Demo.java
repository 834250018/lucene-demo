package cn.ve.crawler;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author ve
 * @date 2020/4/2 16:29
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://www.88ys.com/");
        CloseableHttpResponse httpResponse = client.execute(httpGet);
        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            System.out.println("请求失败");
            return;
        }
        HttpEntity httpEntity = httpResponse.getEntity();
        System.out.println(EntityUtils.toString(httpEntity, "utf8"));

    }
}
