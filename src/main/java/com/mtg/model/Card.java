package com.mtg.model;

import java.util.List;

public class Card {

	private final String name;
	private List<Result> result;

	public Card(String name, List<Result> result) {
		this.name = name;
		this.result = result;
	}

	public String getName() {
		return this.name;
	}

	public List<Result> getResult() {
		return this.result;
	}

}
