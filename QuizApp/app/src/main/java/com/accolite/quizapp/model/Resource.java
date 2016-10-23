package com.accolite.quizapp.model;

public class Resource {
	private int id;
	private String url;
	private String type;
	
	public Resource() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Resource(int id, String url, String type) {
		super();
		this.id = id;
		this.url = url;
		this.type = type;
	}
	
	

}
