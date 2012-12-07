package com.lucene.sort;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FieldCacheTermsFilter;
import org.apache.lucene.search.FieldValueFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import com.lucene.fileSearcher.FileSearcherQueryMaker;
import com.lucene.fileSearcher.SearchBO;

public class SortSearch {
	public static void main(String[] args) throws CorruptIndexException, IOException {
		String index = "/study/lucene/indexFiles/"; //1. 인덱스 파일이 있는 경로
		int rowsize = 1000; //4. 한 페이지에 보여 줄 검색 결과 수
		
		IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(indexReader);
		
//		SearchBO searchBO = new SearchBO();
//		searchBO.setKwd("main");
//		FileSearcherQueryMaker qm = new FileSearcherQueryMaker(searchBO);
//		Query query = qm.makeQuery();
		
		Query query = new MatchAllDocsQuery();
		//역순 정렬
		SortField sortField = new SortField("lastmodified", SortField.LONG, true); 
		Sort sort = new Sort(sortField);
		
		//필터
		Filter filter = new FieldCacheTermsFilter("contents", "main");
		
		TopDocs results = searcher.search(query, filter, rowsize, sort);
		ScoreDoc[] hits = results.scoreDocs;
		
		int numTotalHits = results.totalHits;
    	System.out.println(numTotalHits + " total matching documents");

		System.out.println("============= 검색 결과 ================");
		System.out.println("총 검색 결과 : " + numTotalHits);
		    	
    	for(int i=0 ; i < numTotalHits; i++) {
    		Document doc = searcher.doc(hits[i].doc);
    		String fullPath = doc.get("path");
    		String filename = doc.get("filename");
    		String lastmodified = doc.get("lastmodified");
    		//SimpleDateFormat df = new SimpleDateFormat();
    		
			System.out.println((i+1) + ". 파일명 : " + filename);
			System.out.println("풀파일명 : " + fullPath) ;
			System.out.println("최종수정일 : " + lastmodified) ;
		}
    	
    	searcher.close();
	}
}
