package com.lucene.fileSearcher;

import java.util.StringTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class TextIndexTest {
	public static void main(String[] args) throws ParseException {
		String text = "아버지가방에들어가신다";
		//Analyzer analyzer = new KoreanAnalyzer(true);
		Analyzer analyzer = new KoreanAnalyzer(true);
		Analyzer analyzer2 = new com.tistory.devyongsik.analyzer.KoreanAnalyzer(true);
		
		QueryParser parser = new QueryParser(Version.LUCENE_36, "", analyzer);
    	Query query = parser.parse(text);
    	StringTokenizer st = new StringTokenizer(query.toString());
    	
    	System.out.println("========== Lucene KoreanAnalyzer ===========");
    	while (st.hasMoreElements()) {
			String txt = (String)st.nextElement();
	    	System.out.println(txt);			
		}
    	
    	
    	parser = new QueryParser(Version.LUCENE_36, "", analyzer2);
    	Query query2 = parser.parse(text);
    	st = new StringTokenizer(query2.toString());
    	
    	System.out.println("========== devyongsik KoreanAnalyzer ===========");
    	while (st.hasMoreElements()) {
			String txt = (String)st.nextElement();
	    	System.out.println(txt);			
		}
	}
		
}
