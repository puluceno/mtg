package com.mtg.exception;

public class CardNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4513969554418647226L;

	public CardNotFoundException(String message) {
		super(message);
	}

}
