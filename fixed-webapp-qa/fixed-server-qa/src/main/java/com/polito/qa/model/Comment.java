package com.polito.qa.model;

public class Comment {
	private int id;
	private String text;
	
	public Comment(int id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	
	public Comment(String text) {
		super();
		this.text = text;
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
}
