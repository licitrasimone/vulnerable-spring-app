package com.polito.qa.model;

import java.io.Serializable;

public class UserResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String email;
	private String name;
	
	public UserResponse() {

	}

	public UserResponse(int id, String email, String name) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
