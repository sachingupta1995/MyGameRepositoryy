package model;

import java.util.List;

public class Quiz {
	
	private int id;
	private String description;
	private Topic topic;
	private Level level;
	private List<Question> questions;
	public Quiz(int id, String description, Topic topic, Level level, List<Question> questions) {
		super();
		this.id = id;
		this.description = description;
		this.topic = topic;
		this.level = level;
		this.questions = questions;
	}
	public Quiz() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	
	
}
