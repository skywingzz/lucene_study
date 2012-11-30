package com.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.tistory.devyongsik.analyzer.KoreanAnalyzer;


/*
 * File Index 
 */
public class IndexFiles {
	public static void main(String[] args) {
//		String docsPath = "/workspace/"; //1. ���� ��� ������ �ִ� ���
		String docsPath = "/study/lucene/index_source/";
		String indexPath = "/study/lucene/indexFiles/"; //2. ���� ������ ������� ���
		
		final File docDir = new File(docsPath);
	    if (!docDir.exists() || !docDir.canRead()) {
	      System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
	      System.exit(1);
	    }
	    
	    Date start = new Date();
	    
	    try {
	    	Directory dir = FSDirectory.open(new File(indexPath));
	        Analyzer analyzer = new KoreanAnalyzer(true); //���� ������ �м� �� �� ��� �� Analyzer
	        //Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);

	        boolean create = true; //4. ���� ������ ���� ���� �� �������� ���� 
	        
	        if (create) {
	          iwc.setOpenMode(OpenMode.CREATE); //5. ���ο� �ε��� ������ �����. ���� �ε��� ������ �����˴ϴ�.
	        } else {
	          iwc.setOpenMode(OpenMode.CREATE_OR_APPEND); //6. ���� �ִ� �ε��� ���Ͽ� ������ �߰��մϴ�.
	        }
	        
	        IndexWriter writer = new IndexWriter(dir, iwc); //8. ���� IndexWriter�� �����մϴ�.
	        indexDocs(writer, docDir); //9. ���� ��� �������� �ִ� ���丮���� ������ �о� ������ �մϴ�.

	        writer.close();
	        	       
	        Date end = new Date();
	        System.out.println(end.getTime() - start.getTime() + " total milliseconds");
	        
		} catch (Exception e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}
	}
	
	static void indexDocs(IndexWriter writer, File file) throws IOException {
		if(file.canRead()) {
			if(file.isDirectory()) {
				String[] files = file.list();
				
				if(files != null) {
					for(int i=0 ; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i])); //10. ���ȣ���� ���� ������ ���丮�� �ƴ� ��� ������ ���� �մϴ�.
					}
				}
			} else { //����
				String strFileName = file.getName();
				int dlIndex = strFileName.lastIndexOf(".");
				
				if(dlIndex <= 0) return;
				
				String fileExt = strFileName.substring(dlIndex + 1, strFileName.length());
				String fileName = strFileName.substring(0, dlIndex);
				String lowerStr = fileExt.toLowerCase();
				
				// java, txt ���ϸ� ����
				if(!"txt".equals(lowerStr) && !"java".equals(lowerStr)) {
					return;
				}
				
				System.out.println("index target file : " + fileName);
				
				FileInputStream fis;
				try {
					fis = new FileInputStream(file); //11. ���������� �������� ���� Stream�� ���ϴ�.
		        } catch (FileNotFoundException fnfe) {
		        	return;
		        }
				
				try {
					Document doc = new Document();
					
					//���� Full Path
					Field pathField = new Field("path", file.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
					doc.add(pathField);
					
					//Path �� ������ ���ϸ�
					doc.add(new Field("filename", fileName.toLowerCase(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
					
					//������ ������ ������
					Date date = new Date(file.lastModified());
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
					String strDate = df.format(date);
					System.out.println("modified date : " + strDate);
					NumericField nf = new NumericField("lastmodified", Field.Store.YES, true);
					doc.add(nf.setLongValue(Long.parseLong(strDate)));
					
					//������ ���� ����
					doc.add(new Field("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))));
					
					if(writer.getConfig().getOpenMode() == OpenMode.CREATE) {
						System.out.println("adding New file : " + file);
						writer.addDocument(doc);
					} else {
						System.out.println("Updating file : " + file);
						writer.updateDocument(new Term("path", file.getPath()), doc);
					}
				} finally {
					fis.close();
				}
			}
		}
		
	}
}