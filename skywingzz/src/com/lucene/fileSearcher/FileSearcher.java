package com.lucene.fileSearcher;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class FileSearcher {
	public FileSearcher() {}
	
	public static void main(String[] args) throws CorruptIndexException, IOException {
		String index = "/study/lucene/indexFiles/"; //1. �ε��� ������ �ִ� ���
		String title = "java";
		String keyword = "main";
		int hitsPerPage = 10; //4. �� �������� ���� �� �˻� ��� ��
		
		IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(indexReader);
		
		SearchBO searchBO = new SearchBO();
		searchBO.setTitle(title);
		//searchBO.setKwd(keyword);
		
		FileSearcherQueryMaker qm = new FileSearcherQueryMaker(searchBO);
		BooleanQuery bq = qm.makeQuery();
		
		TopDocs results = searcher.search(bq, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;
		
		int numTotalHits = results.totalHits;
    	System.out.println(numTotalHits + " total matching documents");

		System.out.println("============= �˻� ��� ================");
		System.out.println("�� �˻� ��� : " + numTotalHits);
		    	
    	for(int i=0 ; i < numTotalHits; i++) {
    		Document doc = searcher.doc(hits[i].doc);
    		String fullPath = doc.get("path");
    		String filename = doc.get("filename");
    		String lastmodified = doc.get("lastmodified");
    		
			System.out.println((i+1) + ". ���ϸ� : " + filename);
			System.out.println("Ǯ���ϸ� : " + fullPath) ;
			System.out.println("���������� : " + lastmodified) ;
		}
    	
    	searcher.close();
	}
	
	
}
