package com.lucene.fileSearcher;

import java.util.List;

public class SearchBO {
	String title = "";
	String kwd = "";
	List<String> arrTitle;
	List<String> arrKwd;
	
	long startDate = 0L;
	long endDate = 0L;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKwd() {
		return kwd;
	}
	public void setKwd(String kwd) {
		this.kwd = kwd;
	}
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public List<String> getArrTitle() {
		return arrTitle;
	}
	public void setArrTitle(List<String> arrTitle) {
		this.arrTitle = arrTitle;
	}
	public List<String> getArrKwd() {
		return arrKwd;
	}
	public void setArrKwd(List<String> arrKwd) {
		this.arrKwd = arrKwd;
	}
		
	
}
