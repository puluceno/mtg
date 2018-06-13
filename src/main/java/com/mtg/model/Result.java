package com.mtg.model;

import java.util.List;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("Result")
public class Result {

	private final String name;
	private List<Card> cards;

	public Result(String name, List<Card> cards) {
		this.name = name;
		this.cards = cards;
	}

	public String getName() {
		return this.name;
	}

	public List<Card> getCards() {
		return this.cards;
	}

}
