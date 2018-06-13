package com.mtg.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtg.business.Business;
import com.mtg.model.Result;

@RestController
@RequestMapping(path = "/cards", produces = "application/json")
public class CardController {

	private final Business<Result> businessController;

	@Cacheable(value = "cards", cacheManager = "cacheManager", condition = "@redisConfig.isEnabled()")
	@PostMapping
	public Object getCardPrices(@RequestBody String search) {
		return businessController.findPrices(search);
	}

	public CardController(Business<Result> businessController) {
		this.businessController = businessController;
	}

}
