package com.mtg.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("Card")
public class Card implements Serializable {

	private static final long serialVersionUID = -7061010916736640896L;

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
