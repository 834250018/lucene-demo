package cn.ve.crawler;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ve
 * @date 2020/4/2 19:01
 */
public class HttpPostTest {
    public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException {

        // post请求体
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("aa", "bb"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs, "utf8");
        // 创建HttpPost对象,设置url访问地址
        HttpPost httpPost = new HttpPost("https://www.88ys.com/");
        httpPost.setEntity(formEntity);
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 使用HttpClient发起请求,获取response
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
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
