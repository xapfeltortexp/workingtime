package com.jan.data.time;

@SuppressWarnings("serial")
public class NotValidTimeException extends Exception {
	
	public NotValidTimeException() {
		super("The given time is not correct");
	}
	
	public NotValidTimeException(String message) {
		super(message);
	}
}
