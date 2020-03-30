package cn.ve.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @author ve
 * @date 2020/3/30 16:19
 */
public class LuceneApplication {
    public static void main(String[] args) throws Exception {
//        parseIndex();
//        searchIndex();
        // 标准分析器,只适用于英文
//        tokenStream(new StandardAnalyzer(), "Intellij - Error : \"Usage of API documented as @since 1.6+..\"\n");
        // 中文
        KuceneKit.analyzerTest(new SmartChineseAnalyzer(), "仙王的日常生活\n" +
                "杨幂胡歌孙荣\n" +
                "主演：未知\n" +
                "相关：\n" +
                "类型：动漫 国产动漫更新：2020-03-28 04:01导演：未知地区：大陆年份：2020语言：国语剧情简介:六岁就随手干掉了妖王吞天蛤，作为一个无所不能的修真奇才，王令得隐藏自己的大能，在一群平凡的修真学生中活下去。普通人追求的钱财，仙术，法宝，声名，这个年轻人都不在意。无论豪门千金孙蓉的爱慕，影流顶级杀手的狙杀，父母无间断的啰嗦，都无法阻止他对干脆面的追求。不是在吃干脆面，就是在去小卖部买干脆面的路上。这样的他，和四个队友一起在论剑");
    }


}
