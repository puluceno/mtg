package com.mtg.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mtg.business.Business;
import com.mtg.business.impl.FilterBusiness;
import com.mtg.model.Result;

@RestController
public class CardController {

	private Business<Result> businessController = FilterBusiness.getInstance();

	@Cacheable("cards")
	@PostMapping(value = "/cards", produces = "application/json")
	public String getCardPrices(@RequestBody String search) {
		return businessController.findPrices(search);
	}
}
