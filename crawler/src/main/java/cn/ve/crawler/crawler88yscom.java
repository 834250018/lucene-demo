package cn.ve.crawler;

import org.apache.http.client.config.RequestConfig;
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

import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ve
 * @date 2020/4/2 23:29
 */
public class crawler88yscom {

    public static final String HOST = "https://www.88ys.com";
    // 创建连接池管理器
    public static final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

    // 配置请求信息
    public static final RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(2000) // 创建连接最长时间
            .setConnectionRequestTimeout(2000) // 获取连接最长时间
            .setSocketTimeout(10 * 1000) // 传输数据最长时间
            .build();

    public static void main(String[] args) {
        // todo 改成多线程窃取算法
        for (int i = 1; i < 100; i++) {
            // 鉴于这里暂时是单线程,所以增加打印,确定进度
            System.out.println("页面进度>>>>>>>>页数" + i);
            Document doc = getDoc(HOST + "/vod-list-id-1-pg-" + i + "-order--by-hits-class-0-year-0-letter--area--lang-.html");
            if (doc == null) {
                continue;
            }
            Elements elements = doc.select(".p1.m1 a");
            for (int j = 0; j < elements.size(); j++) {
                // 鉴于这里暂时是单线程,所以增加打印,确定进度
                System.out.println("页内进度>>>>>>>>" + j);
                String title = elements.get(j).attr("title");
                String href = elements.get(j).attr("href");
                store(title, analysisDetailPage(HOST + href));
            }
        }
    }

    private static List<String> analysisDetailPage(String url) {
        Document doc = getDoc(url);
        if (doc == null) {
            return Collections.emptyList();
        }
        String href = doc.select("#vlink_1 a").attr("href");
        List<String> playerUrl = analysisPlayerPage(HOST + href);
        return playerUrl;
    }

    private static List<String> analysisPlayerPage(String url) {
        Document doc = getDoc(url);
        if (doc == null) {
            return Collections.emptyList();
        }
        // 关键脚本,包含多段ascii编码的url
        String href = doc.select(".player.mb script").first().html();
        // 截取含有视频地址的部分
        href = href.substring(href.indexOf("unescape"));
        // 以$分割
        String urls[] = href.split("%24");
        List<String> result = new ArrayList<>();
        for (String u : urls) {
            // 视频编码后的url
            if (StringUtils.isEmpty(u) || u.length() < 10) {
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

    /**
     * 请求url返回document
     *
     * @param url
     * @return
     * @throws Exception
     */
    private static Document getDoc(String url) {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        // 创建HttpGet对象,设置url访问地址
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            Document doc = Jsoup.parse(EntityUtils.toString(response.getEntity(), "utf8"));
            return doc;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 把map存入文件中
     *
     * @param title
     * @param list
     */
    private static void store(String title, List<String> list) {
        try (FileWriter fileWriter = new FileWriter("test.txt", true)) {
            fileWriter.write(title);
            fileWriter.write("\r\n");
            for (String s : list) {
                fileWriter.write(s);
                fileWriter.write("\r\n");
            }
            fileWriter.write("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
