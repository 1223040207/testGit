package com.yc.test;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public class LuceneTest {
	public static void main(String a[]) throws Exception{
		//=================创建索引===========================//
		//创建分词器
		Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_43);
		//创建索引配置
		IndexWriterConfig indexWriterConfig=new IndexWriterConfig(Version.LUCENE_43,analyzer);
		
		//索引打开方式		create or append  没有就新建  有就打开
		indexWriterConfig.setOpenMode(OpenMode.CREATE);
		
		//索引文件
		Directory  directory=null;
		IndexWriter indexWriter=null;
		directory=FSDirectory.open(new File("D://s//"));
		//检测directory这个对象是否是锁定状态   是的话就解锁
		if(indexWriter.isLocked(directory)){
			indexWriter.unlock(directory);
		}
		//实例化indexWriter对象
		indexWriter=new IndexWriter(directory,indexWriterConfig);
		//添加测试文档
		Document doc=new Document();
		//为测试文档添加一个id域  域值为 abcde
		doc.add(new StringField("id","abcde",Store.YES));
		//为测试文档添加一个文本域
		doc.add(new TextField("Content","lucene is my home",Store.YES));
		//为测试文档添加一个数值域
		doc.add(new IntField("num",1,Store.YES));
		//将文档加入索引中
		indexWriter.addDocument(doc);
		
				//添加测试文档
				Document doc2=new Document();
				//为测试文档添加一个id域  域值为 abcde
				doc2.add(new StringField("id","fghjkl",Store.YES));
				//为测试文档添加一个文本域
				doc2.add(new TextField("Content","你好lucene",Store.YES));
				//为测试文档添加一个数值域
				doc2.add(new IntField("num",2,Store.YES));
				
				indexWriter.addDocument(doc2);
				//索引提交
				indexWriter.commit();
				indexWriter.close();
				
				
	//====================检索索引======================================//
		//读取硬盘上的索引文件
		DirectoryReader dReader= DirectoryReader.open(directory);
		//提供索引的检索方法
		IndexSearcher searcher=new IndexSearcher(dReader);		
		//Query 提供查询条件的创建	       lucene版本     索引条件     分词器
		QueryParser parse=new QueryParser(Version.LUCENE_43,"Content",analyzer);
		Query query=parse.parse("lucene");
		//TopDocs 保存检索结果   10保存前10条
		//使用默认排序   按照查询记录与查询query相关度做降序排序
		TopDocs topDocs=searcher.search(query, 10);
		if(topDocs !=null){
			System.out.println("符合查询条件的文档总数为："+topDocs.totalHits);
			for(int i=0;i<topDocs.scoreDocs.length;i++){
				Document mydoc=searcher.doc(topDocs.scoreDocs[i].doc);
				System.out.println("id="+mydoc.get("id"));
				System.out.println("Content="+mydoc.get("Content"));
				System.out.println("num="+mydoc.get("num"));
			}
		}
		//资源关闭
		directory.close();
		dReader.close();
	}
}
