package cn.ve.crawler;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ve
 * @date 2020/4/2 23:29
 */
public class crawler88yscom {

    public static final String HOST = "https://www.88ys.com";
    // 创建连接池管理器
    public static final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

    static {
        cm.setMaxTotal(100);
    }

    public static void main(String[] args) throws Exception {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        CloseableHttpResponse response = null;
        Map<String, List> map = new HashMap<>();
        for (int i = 1; i < 2; i++) {
            // 创建HttpGet对象,设置url访问地址
            HttpGet httpGet = new HttpGet(HOST + "/vod-type-id-1-pg-" + i + ".html");
            response = httpClient.execute(httpGet);
            Document doc = Jsoup.parse(EntityUtils.toString(response.getEntity(), "utf8"));
            Elements elements = doc.select(".p1.m1 a");
//            for (int j = 0; j < elements.size(); j++) {
            for (int j = 0; j < 3; j++) {
                String title = elements.get(j).attr("title");
                String href = elements.get(j).attr("href");
                System.out.println(title + ":" + href);
                map.put(title, analysisDetailPage(HOST + href));
            }
            System.out.println();
        }
        if (response != null) {
            response.close();
        }
    }

    private static List<String> analysisDetailPage(String url) throws Exception {

        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        // 创建HttpGet对象,设置url访问地址
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        Document doc = Jsoup.parse(EntityUtils.toString(response.getEntity(), "utf8"));
        String href = doc.select("#vlink_1 a").attr("href");
        System.out.println(href);
        List<String> playerUrl = analysisPlayerPage(HOST + href);
        System.out.println(playerUrl);
        System.out.println();
        response.close();
        return playerUrl;
    }

    private static List<String> analysisPlayerPage(String url) throws Exception {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        // 创建HttpGet对象,设置url访问地址
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            Document doc = Jsoup.parse(EntityUtils.toString(response.getEntity(), "utf8"));
            // 关键脚本,包含多段ascii编码的url
            String href = doc.select(".player.mb script").first().html();
            // 截取含有视频地址的部分
            href = href.substring(href.indexOf("unescape"));
            // 以$分割
            String urls[] = href.split("%24");
            List<String> result = new ArrayList<>();
            for (String u : urls) {
                // 视频编码后的url
                if (StringUtils.isEmpty(u) || u.length()< 10) {
                    continue;
                }
                final String r = u.replaceAll("'", "")
                        .replaceAll("\\)", "")
                        .replaceAll(";", "");
                try {
                    result.add(URLDecoder.decode(r, "utf8"));
                } catch (Exception e) {
                    // 跳过
                }
            }
            return result;
        }
    }
}
