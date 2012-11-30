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
//		String docsPath = "/workspace/"; //1. 색인 대상 문서가 있는 경로
		String docsPath = "/study/lucene/index_source/";
		String indexPath = "/study/lucene/indexFiles/"; //2. 색인 파일이 만들어질 경로
		
		final File docDir = new File(docsPath);
	    if (!docDir.exists() || !docDir.canRead()) {
	      System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
	      System.exit(1);
	    }
	    
	    Date start = new Date();
	    
	    try {
	    	Directory dir = FSDirectory.open(new File(indexPath));
	        Analyzer analyzer = new KoreanAnalyzer(true); //문서 내용을 분석 할 때 사용 될 Analyzer
	        //Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);

	        boolean create = true; //4. 색인 파일을 새로 생성 할 것인지의 여부 
	        
	        if (create) {
	          iwc.setOpenMode(OpenMode.CREATE); //5. 새로운 인덱스 파일을 만든다. 기존 인덱스 파일은 삭제됩니다.
	        } else {
	          iwc.setOpenMode(OpenMode.CREATE_OR_APPEND); //6. 원래 있던 인덱스 파일에 문서를 추가합니다.
	        }
	        
	        IndexWriter writer = new IndexWriter(dir, iwc); //8. 드디어 IndexWriter를 생성합니다.
	        indexDocs(writer, docDir); //9. 색인 대상 문서들이 있는 디렉토리에서 문서를 읽어 색인을 합니다.

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
						indexDocs(writer, new File(file, files[i])); //10. 재귀호출을 통해 파일이 디렉토리가 아닌 경우 문서를 색인 합니다.
					}
				}
			} else { //파일
				String strFileName = file.getName();
				int dlIndex = strFileName.lastIndexOf(".");
				
				if(dlIndex <= 0) return;
				
				String fileExt = strFileName.substring(dlIndex + 1, strFileName.length());
				String fileName = strFileName.substring(0, dlIndex);
				String lowerStr = fileExt.toLowerCase();
				
				// java, txt 파일만 색인
				if(!"txt".equals(lowerStr) && !"java".equals(lowerStr)) {
					return;
				}
				
				System.out.println("index target file : " + fileName);
				
				FileInputStream fis;
				try {
					fis = new FileInputStream(file); //11. 문서내용을 가져오기 위해 Stream을 엽니다.
		        } catch (FileNotFoundException fnfe) {
		        	return;
		        }
				
				try {
					Document doc = new Document();
					
					//파일 Full Path
					Field pathField = new Field("path", file.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
					doc.add(pathField);
					
					//Path 를 제외한 파일명
					doc.add(new Field("filename", fileName.toLowerCase(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
					
					//파일의 마지막 수정일
					Date date = new Date(file.lastModified());
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
					String strDate = df.format(date);
					System.out.println("modified date : " + strDate);
					NumericField nf = new NumericField("lastmodified", Field.Store.YES, true);
					doc.add(nf.setLongValue(Long.parseLong(strDate)));
					
					//파일의 내용 색인
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