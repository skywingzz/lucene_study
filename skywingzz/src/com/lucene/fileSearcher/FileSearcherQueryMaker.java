package com.lucene.fileSearcher;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

public class FileSearcherQueryMaker {
	private SearchBO searchBO;
	
	protected FileSearcherQueryMaker(SearchBO searchBO) {
		this.searchBO = searchBO;
	}
	
	protected Query makeQuery() throws IOException {
		BooleanQuery resultQuery = new BooleanQuery();
		
		if(searchBO.getTitle() != null && !"".equals(searchBO.getTitle())) {
//			bq.add(kwdTokenizer("filename", searchBO.getTitle()), Occur.SHOULD);
			Query query = new TermQuery(new Term("filename", searchBO.getTitle()));
			resultQuery.add(query, Occur.SHOULD);
		}
		
		if(searchBO.getArrTitle() != null && searchBO.getArrTitle().size() > 0 ) {
			for(String title : searchBO.getArrTitle()) {
				Term t = new Term("filename", title);
				Query query = new TermQuery(t);
				resultQuery.add(query, Occur.SHOULD);
			}
		}
		
		if(searchBO.getKwd() != null && !"".equals(searchBO.getKwd())) {
			//bq.add(kwdTokenizer("contents", searchBO.getKwd()), Occur.SHOULD);
			Query query = new TermQuery(new Term("contents", searchBO.getKwd()));
			resultQuery.add(query, Occur.SHOULD);
		}
		
		if(searchBO.getStartDate() > 0 && searchBO.getEndDate() > 0) {
			NumericRangeQuery nrq = NumericRangeQuery.newLongRange("lastmodified", searchBO.getStartDate(), searchBO.getEndDate(), true, false);
			resultQuery.add(nrq, Occur.SHOULD);
		}
		
		
		System.out.println("Query : " + resultQuery);
		return resultQuery;
	}
	
	private static Query kwdTokenizer(String fieldName, String kwd) throws IOException {
		Query query = null;
		Analyzer analyzer = new KoreanAnalyzer(true);
		
		TokenStream ts = analyzer.tokenStream("dummy", new StringReader(kwd));
		ts.addAttribute(CharTermAttribute.class);
		
		while(ts.incrementToken()) {
			CharTermAttribute charterm = ts.getAttribute(CharTermAttribute.class);
			query = new TermQuery(new Term(fieldName, charterm.toString()));
		}
				
		analyzer.close();
		
		return query;
	}
}
