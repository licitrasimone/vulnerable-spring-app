package com.polito.qa.model;


public class Answer {
	private int id;
	private String text;
	private String author;
	private String date;
	private int score;
	private int questionId;
	
    
    public Answer() {
    }

	
	public Answer(int id, String text, String author, String date, int score, int questionId) {
		super();
		this.id = id;
		this.text = text;
		this.author = author;
		this.date = date;
		this.score = score;
		this.questionId = questionId;
	}
	
	
	public Answer(int id, String text, String author, String date, int score) {
		super();
		this.id = id;
		this.text = text;
		this.author = author;
		this.date = date;
		this.score = score;
	}
	
	public Answer(String text, String author, int score, String date) {
		super();
		this.text = text;
		this.author = author;
		this.date = date;
		this.score = score;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	
	
}
