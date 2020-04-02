package cn.ve.crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

/**
 * @author ve
 * @date 2020/4/2 22:02
 */
public class JsoupTest {
    public static void main(String[] args) throws Exception {
        Document document = Jsoup.parse(new URL("http://www.baidu.com/"), 10000);
        System.out.println(document.getElementsByTag("title").first().text());

        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建HttpGet对象,设置url访问地址
        URIBuilder uriBuilder = new URIBuilder("https://www.88ys.com/");
        uriBuilder.addParameter("aa", "bb");
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        CloseableHttpResponse response = httpClient.execute(httpGet);
        Document doc = Jsoup.parse(EntityUtils.toString(response.getEntity(), "utf8"));
        System.out.println(doc.getElementsByTag("title").first().text());
    }
}
