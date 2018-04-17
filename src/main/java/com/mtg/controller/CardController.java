package com.mtg.controller;

import org.pmw.tinylog.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mtg.business.Business;
import com.mtg.business.impl.FilterBusiness;
import com.mtg.model.Result;

@RestController
public class CardController {

	private Business<Result> businessController = FilterBusiness.getInstance();

	@PostMapping(value = "/cards", produces = "application/json")
	public String getCardPrices(@RequestBody String[] cards) {
		Logger.info("Requested data for " + cards.length + " cards.");
		return businessController.findPrices(cards);
	}
}
