package com.polito.qa.exception;

public class AnswerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public AnswerNotFoundException(String message) {
        super(message);
    }

    public AnswerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
