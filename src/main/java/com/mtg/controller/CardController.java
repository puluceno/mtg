package com.mtg.controller;

import org.pmw.tinylog.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mtg.crawler.Crawler;

@RestController
public class CardController {

	private Crawler crawler = Crawler.getInstance();

	@GetMapping("/card/{cardName}")
	public String getCardPrice(@PathVariable String cardName) {
		Logger.info("Requested data for ".concat(cardName).concat(" ."));
		return crawler.findPrices(cardName);
	}
}
