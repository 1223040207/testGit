package com.yc.test;

import java.io.StringReader;
import org.apache.lucene.analysis.TokenStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class AnalyzerTest {
	//要处理的静态字符串
	private static String str="计算机1302,Lucene案例开发";
	//输出方法
	public static void print(Analyzer analyzer){
		StringReader stringReader=new StringReader(str);
		try {
			TokenStream tokenStream=analyzer.tokenStream("", stringReader);
			tokenStream.reset();
			CharTermAttribute term=tokenStream.getAttribute(CharTermAttribute.class);
			System.out.println("分词技术 "+analyzer.getClass());
			while(tokenStream.incrementToken()){
				System.out.print(term.toString()+"|");
			}
			System.out.println();
			
			System.out.println("git更新测试");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/**
	 * @param args
	 * 分词器测试  
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//StandardAnalyzer   标准分词器     会吧英文分成词根  一个个单词，汉字分成一个个的字不存在语义
		//IKAnalyzer 基于Lucene的第三方中文分词技术   里面有个中文词典 是扩展的需要另外加jar包
		//WhitespaceAnalyzer 空格分词技术  按照空格简单的切分字符串  对形成的字符串不做其他操作
		//SimpleAnalyzer   简单分词器  一句话就是一个词  遇到标点空格等就划分为一个词
		//CJKAnalyzer 二分法分词器 	将一个字的前面和后面的字组成一个词  会产生大量的无用词组
		//KeywordAnalyzer 关键词分词器   吧要处理的字符串当成一个整体 不对其做任何操作
		//StopAnalyzer   被忽略词分词器   分配忽略的词  如标点  空格   效果与SimpleAnalyzer相同
		
		Analyzer analyzer=null;
		analyzer=new StandardAnalyzer(Version.LUCENE_43);
		AnalyzerTest.print(analyzer);
		
		analyzer=new IKAnalyzer();
		AnalyzerTest.print(analyzer);
		
		analyzer=new WhitespaceAnalyzer(Version.LUCENE_43);
		AnalyzerTest.print(analyzer);
		
		analyzer=new SimpleAnalyzer(Version.LUCENE_43);
		AnalyzerTest.print(analyzer);
		
		analyzer=new CJKAnalyzer(Version.LUCENE_43);
		AnalyzerTest.print(analyzer);
		
		analyzer=new KeywordAnalyzer();
		AnalyzerTest.print(analyzer);
		
		analyzer=new StopAnalyzer(Version.LUCENE_43);
		AnalyzerTest.print(analyzer);
	}

}
