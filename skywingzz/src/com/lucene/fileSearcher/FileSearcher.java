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
		String index = "/study/lucene/indexFiles/"; //1. �ε��� ������ �ִ� ���
		int hitsPerPage = 10; //4. �� �������� ���� �� �˻� ��� ��
		
		IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(indexReader);
		
		SearchBO searchBO = new SearchBO();
		
		//1. ���� java�� �ְ�, ���뿡 main�� �ִ� ���� �˻�
//		searchBO.setTitle("java"); //���ϸ� �˻���
//		searchBO.setKwd("main"); //���� ���� �˻���
		
		//2. ���� java�� �ְ�, ���뿡 main�� ���� ���� �˻�
		searchBO.setTitle("java"); //���ϸ� �˻���
//		searchBO.setKwd("main"); //���� ���� �˻���
		
		
		
		
		//��¥ �˻�
//		searchBO.setStartDate(20121126); //������ �˻� ������
//		searchBO.setEndDate(20121128); //������ �˻� ������
		
		FileSearcherQueryMaker qm = new FileSearcherQueryMaker(searchBO);
		Query bq = qm.makeQuery();
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
    		//SimpleDateFormat df = new SimpleDateFormat();
    		
			System.out.println((i+1) + ". ���ϸ� : " + filename);
			System.out.println("Ǯ���ϸ� : " + fullPath) ;
			System.out.println("���������� : " + lastmodified) ;
		}
    	
    	searcher.close();
	}
	
	
}
