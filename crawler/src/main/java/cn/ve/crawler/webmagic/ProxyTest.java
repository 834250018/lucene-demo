package cn.ve.crawler.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * @author ve
 * @date 2020/4/4 20:57
 */
public class ProxyTest implements PageProcessor {
    @Override
    public void process(Page page) {
        System.out.println(page.getRawText());
        page.putField("div", page.getRawText());
    }

    private Site site = Site.me();

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        // 创建下载器
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        // 给下载器设置代理服务器信息
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("211.21.120.163", 8080)
        ));
        Spider spider = Spider.create(new ProxyTest())
                .setDownloader(httpClientDownloader)
                .addUrl("https://myip.ipip.net/")
//                .addPipeline(new FilePipeline("d://test")) // 指定存储文件夹
                .thread(5); // 多线程
        // 设置存储方式为内存,去重过滤器为布隆过滤器
        spider.setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000000)));
        spider.run();
    }
}
