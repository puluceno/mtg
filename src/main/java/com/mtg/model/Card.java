package com.mtg.model;

import java.util.List;
import java.util.Map;

public class Card {

	private final String name;
	private List<Map<String, Object>> result;

	public Card(String name, List<Map<String, Object>> result) {
		this.name = name;
		this.result = result;
	}

	public String getName() {
		return this.name;
	}

	public List<Map<String, Object>> getResult() {
		return this.result;
	}

}
