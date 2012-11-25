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
		String index = "/study/lucene/lucene_index/"; //1. �ε��� ������ �ִ� ���
		String field = "filename"; //2. Ű����� �˻� �� ��
	    String queryString = "dummy IndexFiles"; //3. ������� ���Ǵ� �˻�����
	    int hitsPerPage = 10; //4. �� �������� ���� �� �˻� ��� ��
	    
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

		System.out.println("============= �˻� ��� ================");
		System.out.println("�� �˻� ��� : " + numTotalHits);
		    	
    	for(int i=0 ; i < numTotalHits; i++) {
    		//System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
    		
    		Document doc = searcher.doc(hits[i].doc);
    		String fullPath = doc.get("path");
    		String filename = doc.get("filename");
    		String lastmodified = doc.get("lastmodified");
    		
			System.out.println((i+1) + ". ���ϸ� : " + filename);
			System.out.println("Ǯ���ϸ� : " + fullPath) ;
			System.out.println("���������� : " + lastmodified) ;
		}
	}
}