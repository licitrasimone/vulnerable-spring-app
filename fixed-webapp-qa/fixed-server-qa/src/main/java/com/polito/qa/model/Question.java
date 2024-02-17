package com.polito.qa.model;



public class Question {
	private int id;
	private String text;
	private String author;
	private String date;
	

	public Question(int id, String text, String author, String date) {
		this.id = id;
		this.text = text;
		this.author = author;
		this.date = date;
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
	
}
