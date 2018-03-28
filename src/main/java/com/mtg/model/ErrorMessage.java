package com.mtg.model;

import com.jsoniter.output.JsonStream;
import com.mtg.model.enumtype.ErrorCode;

public class ErrorMessage {

	private final String message;
	private final ErrorCode code;

	public ErrorMessage(String message, ErrorCode code) {
		this.message = message;
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public ErrorCode getCode() {
		return code;
	}

	@Override
	public String toString() {
		return JsonStream.serialize(this);
	}
}
