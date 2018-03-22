package com.mtg.model;

import java.util.HashMap;
import java.util.Map;

public class Card {

	private final String name;
	private final Map<String, Map<String, Integer>> result;

	public Card(String name, String store) {
		this.name = name;
		this.result = new HashMap<>();
		this.result.put(store, new HashMap<>());
	}

	public String getName() {
		return name;
	}

	public Map<String, Map<String, Integer>> getResult() {
		return result;
	}

}
