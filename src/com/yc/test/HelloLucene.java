package com.yc.test;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.text.ParseException;

public class HelloLucene {
  public static void main(String[] args) throws Exception {
    // 0. 指定分词文本   分词器
    //   同一分词器用于索引和搜索							指定lucenne版本
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);

    // 1.创建内存索引
    Directory index = new RAMDirectory();
    
    //	两个参数： 版本   分词器											
    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
    
    
   //创建索引关键类
    IndexWriter w = new IndexWriter(index, config);
    
    //吧文档添加到索引中 
    addDoc(w, "中华人民共和国", "193398817");
    addDoc(w, "全国人民 ", "55320055Z");
    addDoc(w, "2006年", "55063554A");
    addDoc(w, "The Art of Computer Science国", "9900333X");
    w.close();

    // 2. query
   // System.out.println(args.length);
   // String querystr = args.length > 0 ? args[0] : "lucene";
    //要索引的单词
    String querystr =  "国";
    // the "title" arg specifies the default field to use
    // when no field is explicitly specified in the query.
    Query q = new QueryParser(Version.LUCENE_43, "title", analyzer).parse(querystr);

    // 3. 创建一个Searcher对象并且使用上面创建的Query对象来进行搜索，匹配到的前10个结果封装在TopScoreDocCollector对象里返回。
    int hitsPerPage = 10;
    IndexReader reader = DirectoryReader.open(index);
    IndexSearcher searcher = new IndexSearcher(reader);
    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
    searcher.search(q, collector);
    ScoreDoc[] hits = collector.topDocs().scoreDocs;
    
    // 4. display results
    System.out.println("找到   " + hits.length + " 处.");
    for(int i=0;i<hits.length;++i) {
      int docId = hits[i].doc;
      Document d = searcher.doc(docId);
      System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
    }

    // reader can only be closed when there
    // is no need to access the documents any more.
    reader.close();
  }

  private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
    Document doc = new Document();
    doc.add(new TextField("title", title, Field.Store.YES));

    // use a string field for isbn because we don't want it tokenized
    doc.add(new StringField("isbn", isbn, Field.Store.YES));
    w.addDocument(doc);
  }
}
