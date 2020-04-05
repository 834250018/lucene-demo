package cn.ve.crawler.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * @author ve
 * @date 2020/4/4 2:41
 */
public class JobProcessor implements PageProcessor {

    // 解析页面
    @Override
    public void process(Page page) {
        // 解析返回的数据page,把解析的结果放到ResultItems中
//        page.putField("div", page.getHtml().css("div").all()); // 选择器
//        page.putField("div1", page.getHtml().$("div").all()); // 选择器
//        page.putField("div2", page.getHtml().xpath("//a[@href]").all()); // xpath
//        page.putField("div3", page.getHtml().css("div").regex(".*内衣.*").all()); // 正则表达式
        page.putField("div4", page.getHtml().links().all()); // 链接

        // 增加爬取链接
//        page.addTargetRequest(page.getHtml().links().get());
//        page.putField("error", page.getHtml().css(".error").all());
    }

    private Site site = Site.me();

    @Override
    public Site getSite() {
        return site
                .setCharset("utf8") // 字符编码
                .setTimeOut(10000) // 超时时间
                .setRetryTimes(3) // 重试次数
                .setRetrySleepTime(3000); // 重试时间
    }

    public static void main(String[] args) {
        Spider spider = Spider.create(new JobProcessor())
                .addUrl("https://www.88ys.com/")
                .addPipeline(new FilePipeline("test")) // 指定存储文件夹
                .thread(5); // 多线程
        // 设置存储方式为内存,去重过滤器为布隆过滤器
        spider.setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000000)));
        spider.run();
    }
}
