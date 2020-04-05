package cn.ve.crawler;

import com.lou.simhasher.SimHasher;

/**
 * @author ve
 * @date 2020/4/4 20:21
 */
public class SimHashDemo {
    public static void main(String[] args) {

        String str1 = "国家卫生健康委新闻发言人、宣传司副司长米锋在今天下午国务院联防联控机制新闻发布会上表示，4月3日，全国本土现有确诊病例降至900例以下；无本土现有疑似病例，湖北除武汉外，连续30天无本土新增确诊病例，全球疫情呈爆发增长态势，我国防输入压力持续加大。当前低风险地区正在推进生产生活秩序全面恢复，要继续严格控制人员聚集，加强重点人员防护。";
        SimHasher hash1 = new SimHasher(str1);
        //打印simhash签名
        System.out.println(hash1.getSignature());
        System.out.println("============================");

        String str2 = "4月3日，全国本土现有确诊病例降至900例以下，无本土现有疑似病例，湖北除武汉外连续30天无本土新增确诊病例。全球疫情呈暴发增长态势，我国防输入压力持续加大。当前低风险地区正在推进生产生活秩序全面恢复，要继续严格控制人员聚集，加强重点人群防护。";
        //打印simhash签名
        SimHasher hash2 = new SimHasher(str2);
        System.out.println(hash2.getSignature());
        System.out.println("============================");

        //打印海明距离
        System.out.println(hash1.getHammingDistance(hash2.getSignature()));
    }
}
