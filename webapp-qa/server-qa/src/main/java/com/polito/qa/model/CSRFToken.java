package com.polito.qa.model;

import java.io.Serializable;
import java.util.UUID;

public class CSRFToken implements Serializable {
	private UUID value;
	
	public CSRFToken() {
		this.value = UUID.randomUUID();
	}

	public UUID getValue() {
		return value;
	}

	public void setValue(UUID value) {
		this.value = value;
	}
	
}
