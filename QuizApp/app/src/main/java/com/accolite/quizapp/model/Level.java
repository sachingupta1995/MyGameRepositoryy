package com.accolite.quizapp.model;

public class Level {
	
	private int id;
	private String levelName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public Level() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Level(int id, String levelName) {
		super();
		this.id = id;
		this.levelName = levelName;
	}
	
	

}
