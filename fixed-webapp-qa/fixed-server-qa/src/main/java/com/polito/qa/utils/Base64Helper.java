package com.polito.qa.utils;

import java.io.Serializable;
import java.util.Base64;

public class Base64Helper implements Serializable{
	private static final long serialVersionUID = 1L;
	private String base64;
	
	public Base64Helper(String base64) {
		this.base64 = base64;
	}
	
	public String decode() {
		return new String(Base64.getDecoder().decode(this.base64));
	}
}
