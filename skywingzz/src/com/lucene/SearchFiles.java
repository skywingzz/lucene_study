package com.lucene;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchFiles {

	public static void main(String[] args) throws Exception {
		String index = "/study/lucene/lucene_index/"; //1. 인덱스 파일이 있는 경로
		String field = "filename"; //2. 키워드로 검색 할 필
	    String queryString = "dummy IndexFiles"; //3. 루씬에서 사용되는 검색쿼리
	    int hitsPerPage = 10; //4. 한 페이지에 보여 줄 검색 결과 수
	    
	    IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(index)));
	    IndexSearcher searcher = new IndexSearcher(indexReader);
	    
	    Analyzer analyzer = new KeywordAnalyzer();
	    //Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_36);
	    
	    QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);
    	Query query = parser.parse(queryString);
    	
		System.out.println("Query String : " + queryString);
    	System.out.println("Query : " + query.toString());
    	System.out.println("Searching for: " + query.toString(field));
    	
    	TopDocs results = searcher.search(query, 5 * hitsPerPage);
    	ScoreDoc[] hits = results.scoreDocs;
    	
    	int numTotalHits = results.totalHits;
    	System.out.println(numTotalHits + " total matching documents");

		System.out.println("============= 검색 결과 ================");
		System.out.println("총 검색 결과 : " + numTotalHits);
		    	
    	for(int i=0 ; i < numTotalHits; i++) {
    		//System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
    		
    		Document doc = searcher.doc(hits[i].doc);
    		String fullPath = doc.get("path");
    		String filename = doc.get("filename");
    		String lastmodified = doc.get("lastmodified");
    		
			System.out.println((i+1) + ". 파일명 : " + filename);
			System.out.println("풀파일명 : " + fullPath) ;
			System.out.println("최종수정일 : " + lastmodified) ;
		}
	}
}