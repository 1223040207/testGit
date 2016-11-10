package com.yc.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.xml.builders.NumericRangeQueryBuilder;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;

public class QueryTest {

	/**
	 * @param args
	 * Query 测试
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//QueryParser(matchVersion, field, analyzer)   Lucene版本    field 搜索域     analyzer分词器
		//QueryParser parser=new QueryParser(Version.LUCENE_43, field,analyzer);
		//query=parser.parse(key);
		
		//MultiFieldQueryParser(matchVersion, fields, analyzer) 为QueryParser的升级版支持多个域的搜索   fields为域数组  analyzer为分词器
		//MultiFiledQueryParser parser=new MultiFieldQueryParser(Version.LUCENE_43, fields, analyzer);
		
		//TermQuery  关键词查询的创建
		//query= new TermQuery(new Term(field,key));
		//PrefixQuery 前缀词匹配     如果key为Lu   则Lucene匹配  Lock不匹配
		//query=new PrefixQuery(new Term(field,key));
		
		//PhraseQuery  可以指定两个关键词之间的最大距离     短语搜索
		//PhraseQuery query=new PhraseQuery();
		//设置距离   如果关键词之间的距离大于2则不被匹配搜索
		//query.setSlop(2);
		//query.add(new Term("Content","Lucene"));
		//query.add(new Term("Content","案例"));
		
		//WildcardQuery通配符搜索   通配符有：  *： 匹配一个或者多个任意字符   ?： 匹配一个任意字符
		//query=new WildcardQuery(new Term(field,"基于?"));
		//TermRangeQuery  字符串范围搜索     field:域名  lowerTerm：域的下限值    upperTerm：域的上限值	includeLower：是否包含下限  includeUpper：是否包含上限
		//TermRangeQuery query=new TermRangeQuery(field, lowerTerm, upperTerm, includeLower, includeUpper)
		
		//NumericRangeQuery 数字范围搜索   针对不同的数据类型有不同的方法     field:域名  min：域的下限值    max：域的上限值	minInclusive：是否包含下限  maxInclusive：是否包含上限
		//NumericRangeQuery.newIntRange(field, min, max, minInclusive, maxInclusive);int型
		//NumericRangeQuery.newDoubleRange(field, min, max, minInclusive, maxInclusive);double型
		//NumericRangeQuery.newFloatRange(field, min, max, minInclusive, maxInclusive);float型………………
		
		//BooleanQuery  可以吧多个query对象组合成一个query对象时     Occur有三种类型   MUST  SHOULD  MUST_NOT
		//BooleanQuery query=new BooleanQuery();
		//query.add(query, Occur.MUST);
		
		
		String key="极客学院";
		String filed="name";
		String fileds[]={"name","content"};
		Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_43);
		Query query=null;
		
		//验证query对象
		QueryParser parser=new QueryParser(Version.LUCENE_43, filed,analyzer);
		query=parser.parse(key);
		System.out.println(QueryParser.class+query.toString());
		
		MultiFieldQueryParser parser1=new MultiFieldQueryParser(Version.LUCENE_43, fileds, analyzer);
		query=parser1.parse(key);
		System.out.println(MultiFieldQueryParser.class + query.toString());
		
		query=new TermQuery(new Term(filed,key));
		System.out.println(TermQuery.class+query.toString());
		
		query=new PrefixQuery(new Term(filed,key));
		System.out.println(PrefixQuery.class+query.toString());
		
		//短语搜索
		PhraseQuery query1=new PhraseQuery();
		query1.setSlop(2);
		query1.add(new Term(filed,"极客学院"));
		query1.add(new Term(filed,"Lucene案例"));
		System.out.println(PhraseQuery.class +query1.toString());
		
		//通配符Query
		query=new WildcardQuery(new Term(filed,"极客学院?"));
		System.out.println(WildcardQuery.class+query.toString());
		
		//字符串范围搜索
		query=TermRangeQuery.newStringRange(filed, "a", "c", false, false);
		System.out.println(TermRangeQuery.class+query.toString());
		
		//组合搜索
		BooleanQuery query2=new BooleanQuery();
		query2.add(new TermQuery(new Term(filed,"极客学院")), Occur.SHOULD);
		query2.add(new TermQuery(new Term(filed,"Lucene")), Occur.MUST);
		query2.add(new TermQuery(new Term(filed,"案例")), Occur.MUST_NOT);
		System.out.println(BooleanQuery.class+query2.toString());
	}

}
