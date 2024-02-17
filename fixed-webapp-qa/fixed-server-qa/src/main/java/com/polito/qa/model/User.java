package com.polito.qa.model;

public class User {
	private int id;
	private String username;
	private String password;
	private String name;
	private String csrf;
	
	public User() {
		
	}
	
	public User(int id, String username, String name) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password, String csrf) {
		super();
		this.username = username;
		this.password = password;
		this.csrf = csrf;
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

	public String getCsrf() {
		return csrf;
	}

	public void setCsrf(String csrf) {
		this.csrf = csrf;
	}
	
	
	
}
