package com.mtg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mtg.crawler.Crawler;

@RestController
public class CardController {

	private Crawler crawler = Crawler.getInstance();

	@GetMapping("/card/{card}")
	public String getCardPrice(@PathVariable String card) {
		return crawler.findPrices(card);
	}
}
