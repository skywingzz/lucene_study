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
		String index = "/study/lucene/indexFiles/"; //1. �ε��� ������ �ִ� ���
		int rowsize = 1000; //4. �� �������� ���� �� �˻� ��� ��
		
		IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(indexReader);
		
//		SearchBO searchBO = new SearchBO();
//		searchBO.setKwd("main");
//		FileSearcherQueryMaker qm = new FileSearcherQueryMaker(searchBO);
//		Query query = qm.makeQuery();
		
		Query query = new MatchAllDocsQuery();
		//���� ����
		SortField sortField = new SortField("lastmodified", SortField.LONG, true); 
		Sort sort = new Sort(sortField);
		
		//����
		Filter filter = new FieldCacheTermsFilter("contents", "main");
		
		TopDocs results = searcher.search(query, filter, rowsize, sort);
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
    		//SimpleDateFormat df = new SimpleDateFormat();
    		
			System.out.println((i+1) + ". ���ϸ� : " + filename);
			System.out.println("Ǯ���ϸ� : " + fullPath) ;
			System.out.println("���������� : " + lastmodified) ;
		}
    	
    	searcher.close();
	}
}
