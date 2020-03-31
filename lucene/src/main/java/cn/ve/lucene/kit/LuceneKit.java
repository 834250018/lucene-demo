package cn.ve.lucene.kit;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

/**
 * @author ve
 * @date 2020/3/30 19:37
 */

public enum LuceneKit {
    ;
    public static final String INDEX_PATH = "d:\\tmp";
    public static final String TEST_PATH = "D:\\searchsource";
    public static final Analyzer ikAnalyzer = new IKAnalyzer();

    public static void main(String[] args) throws Exception {
        LuceneKit.analyzerTest("ff", "仙王的日常生活\n" +
                "杨幂胡歌孙荣\n" +
                "主演：未知\n" +
                "相关：\n" +
                "类型：动漫 国产动漫更新：2和四个队友一起在论剑");
    }

    /**
     * 使用分析器分析文本并打印
     *
     * @param content
     * @throws Exception
     */
    public static void analyzerTest(String field, String content) throws Exception {
        // 2.使用分析器对象的tokenStream方法获取一个TokenStream对象
        TokenStream tokenStream = ikAnalyzer.tokenStream(field, content);
        // 3.向tkenStream对象中设置一个引用,相当于一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 4.重置指针位置,不调用则抛异常
        tokenStream.reset();
        // 5.使用while循环遍历tokenStream对象
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute.toString());
        }
        // 6.关闭tokenStream对象
        tokenStream.close();
    }

    /**
     * 可以对要查询的内容先分词,然后基于分词的结果进行查询
     *
     * @param field
     * @param text
     * @throws Exception
     */
    public static void queryParser(String field, String text) throws Exception {
        // 1.创建一个Directory,指定索引库位置
        Directory directory = FSDirectory.open(new File(INDEX_PATH).toPath());
        // 2.创建一个IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 3.创建一个IndexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 4.创建一个Query对象,TermQuery
        QueryParser queryParser = new QueryParser(field, ikAnalyzer);
        Query query = queryParser.parse(text);
        // 5.执行查询,得到一个TopDocs对象
        // 参数1:查询对象;参数2:查询结果返回的最大记录数
        TopDocs topDocs = indexSearcher.search(query, 10);
        // 6.取查询结果的总记录数
        System.out.println("查询总记录数: " + topDocs.totalHits);
        // 7.取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc doc : scoreDocs) {
            // 取文档id
            int docId = doc.doc;
            // 根据id取文档对象
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
//            System.out.println(document.get("content"));
            System.out.println();
        }
        // 关闭IndexReader对象
        indexReader.close();
    }

    /**
     * 数字范围查询
     *
     * @param field 存入的时候必须是LongPoint
     * @param min
     * @param max
     * @throws Exception
     */
    public static void rangQuery(String field, Long min, Long max) throws Exception {
        Query query = LongPoint.newRangeQuery(field, min, max);
        // 1.创建一个Directory,指定索引库位置
        Directory directory = FSDirectory.open(new File(INDEX_PATH).toPath());
        // 2.创建一个IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 3.创建一个IndexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("总记录数: " + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
//            System.out.println(document.get("content"));
            indexReader.close();
        }
    }

    /**
     * 查询索引
     *
     * @throws Exception
     */
    public static void searchIndex(String field, String text) throws Exception {
        // 1.创建一个Directory,指定索引库位置
        Directory directory = FSDirectory.open(new File(INDEX_PATH).toPath());
        // 2.创建一个IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 3.创建一个IndexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 4.创建一个Query对象,TermQuery
        Query query = new TermQuery(new Term(field, text));
        // 5.执行查询,得到一个TopDocs对象
        // 参数1:查询对象;参数2:查询结果返回的最大记录数
        TopDocs topDocs = indexSearcher.search(query, 10);
        // 6.取查询结果的总记录数
        System.out.println("查询总记录数: " + topDocs.totalHits);
        // 7.取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc doc : scoreDocs) {
            // 取文档id
            int docId = doc.doc;
            // 根据id取文档对象
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
//            System.out.println(document.get("content"));
            System.out.println();
        }
        // 关闭IndexReader对象
        indexReader.close();
    }

    /**
     * 修改文档,原理是先查询后删除最后新增
     *
     * @throws Exception
     */
    public static void updateDocument(String field, String text) throws Exception {
        // 1.创建一个Directory,指定索引库位置
        Directory directory = FSDirectory.open(new File(INDEX_PATH).toPath());
        // 2.基于Directory对象创建一个IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(ikAnalyzer));
        Document document = new Document();
        document.add(new TextField("name", "更新之后的文档", Field.Store.YES));
        document.add(new TextField("name1", "更新之后的文档1", Field.Store.YES));
        document.add(new TextField("name2", "更新之后的文档2", Field.Store.YES));
        // 更新操作
        // 删除指定域中包含某个关键词的索引
        indexWriter.updateDocument(new Term(field, text), document);
        indexWriter.close();
    }

    /**
     * 根据查询删除
     *
     * @throws Exception
     */
    public static void deleteDocument(String field, String text) throws Exception {
        // 1.创建一个Director对象,指定索引库保存的位置
        Directory directory = FSDirectory.open(new File(INDEX_PATH).toPath());
        // 2.基于Directory对象创建一个IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(ikAnalyzer));
        // 删除指定域中包含某个关键词的索引
        indexWriter.deleteDocuments(new Term(field, text));
        indexWriter.close();
    }

    public static void deleteAllDocument() throws Exception {
        // 1.创建一个Director对象,指定索引库保存的位置
        Directory directory = FSDirectory.open(new File(INDEX_PATH).toPath());
        // 2.基于Directory对象创建一个IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(ikAnalyzer));
        indexWriter.deleteAll();
        indexWriter.close();
    }

    /**
     * 创建索引
     *
     * @throws Exception
     */
    public static void addDocument() throws Exception {
        // 1.创建一个Director对象,指定索引库保存的位置
        // 把索引库保存在磁盘
        Directory directory = FSDirectory.open(new File(INDEX_PATH).toPath());
        // 2.基于Directory对象创建一个IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(ikAnalyzer));
        // 3.读取磁盘上的文件,对应每个文件创建一个文档对象
        File dir = new File(TEST_PATH);
        File[] files = dir.listFiles();
        for (File file : files) {
            // 文件名
            String fileName = file.getName();
            // 文件路径
            String filePath = file.getPath();
            // 文件内容
            String content = FileUtils.readFileToString(file, "utf-8");
            // 文件大小
            long size = file.length();
            // 参数1:域的名称,参数2:域的内容,参数3:是否存储
            Field fieldName = new TextField("name", fileName, Field.Store.YES);
            Field fieldPath = new TextField("path", filePath, Field.Store.YES);
            Field fieldSize = new TextField("size", String.valueOf(size), Field.Store.YES);
            Field fieldContent = new TextField("content", content, Field.Store.YES);
            // 4.向文档对象中添加域
            // 创建文档对象
            Document document = new Document();
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSize);
            // 5.把文档对象写入索引库
            indexWriter.addDocument(document);
        }
        // 关闭indexWriter对象
        indexWriter.close();
    }
}
