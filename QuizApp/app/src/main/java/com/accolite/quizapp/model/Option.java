package com.accolite.quizapp.model;

import java.util.List;

public class Option {
	
	private int id;
	private String optionText;
	private int questionId;
	private int isCorrect;
	private List<Resource> resources;
	
	public Option(int id, String optionText, int questionId, int isCorrect, List<Resource> resources) {
		super();
		this.id = id;
		this.optionText = optionText;
		this.questionId = questionId;
		this.isCorrect = isCorrect;
		this.resources = resources;
	}

	public Option() {
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOptionText() {
		return optionText;
	}
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getIsCorrect() {
		return isCorrect;
	}
	public void setIsCorrect(int isCorrect) {
		this.isCorrect = isCorrect;
	}
	
	public List<Resource> getResources() {
		return resources;
	}
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + isCorrect;
		result = prime * result + ((optionText == null) ? 0 : optionText.hashCode());
		result = prime * result + questionId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Option other = (Option) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
}
