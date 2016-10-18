package model;

import java.util.List;

public class Question {
	private int id;
	private String quesText;
	private Complexity complexity;
	private int time;

	private List<Option> options;
	private List<Topic> topics;
	private List<Resource> resources;

	public Question(int id, String quesText, Complexity complexity, int time, List<Option> options, List<Topic> topics,
					List<Resource> resources) {
		super();
		this.id = id;
		this.quesText = quesText;
		this.complexity = complexity;
		this.time = time;
		this.options = options;
		this.topics = topics;
		this.resources = resources;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuesText() {
		return quesText;
	}

	public void setQuesText(String quesText) {
		this.quesText = quesText;
	}

	public Complexity getComplexity() {
		return complexity;
	}

	public void setComplexity(Complexity complexity) {
		this.complexity = complexity;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((complexity == null) ? 0 : complexity.hashCode());
		result = prime * result + id;
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((quesText == null) ? 0 : quesText.hashCode());
		result = prime * result + ((resources == null) ? 0 : resources.hashCode());
		result = prime * result + time;
		result = prime * result + ((topics == null) ? 0 : topics.hashCode());
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
		Question other = (Question) obj;
		if (complexity == null) {
			if (other.complexity != null)
				return false;
		} else if (!complexity.equals(other.complexity))
			return false;
		if (id != other.id)
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (quesText == null) {
			if (other.quesText != null)
				return false;
		} else if (!quesText.equals(other.quesText))
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		if (time != other.time)
			return false;
		if (topics == null) {
			if (other.topics != null)
				return false;
		} else if (!topics.equals(other.topics))
			return false;
		return true;
	}



}
