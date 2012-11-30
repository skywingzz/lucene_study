package com.lucene.fileSearcher;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class FileSearcher {
	public FileSearcher() {}
	
	public static void main(String[] args) throws CorruptIndexException, IOException {
		String index = "/study/lucene/indexFiles/"; //1. 인덱스 파일이 있는 경로
		int hitsPerPage = 10; //4. 한 페이지에 보여 줄 검색 결과 수
		
		IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(indexReader);
		
		SearchBO searchBO = new SearchBO();
		
		//1. 제목에 java가 있고, 내용에 main이 있는 파일 검색
//		searchBO.setTitle("java"); //파일명 검색어
//		searchBO.setKwd("main"); //파일 내용 검색어
		
		//2. 제목에 java가 있고, 내용에 main이 없는 파일 검색
		searchBO.setTitle("java"); //파일명 검색어
//		searchBO.setKwd("main"); //파일 내용 검색어
		
		
		
		
		//날짜 검색
//		searchBO.setStartDate(20121126); //수정일 검색 시작일
//		searchBO.setEndDate(20121128); //수정일 검색 종료일
		
		FileSearcherQueryMaker qm = new FileSearcherQueryMaker(searchBO);
		Query bq = qm.makeQuery();
		TopDocs results = searcher.search(bq, 5 * hitsPerPage);
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
