package com.mtg.controller;

import org.pmw.tinylog.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mtg.crawler.Crawler;
import com.mtg.crawler.impl.JsoupCrawler;

@RestController
public class CardController {

	// private Crawler crawler = CrawlerDefault.getInstance();
	private Crawler crawler = JsoupCrawler.getInstance();

	@PostMapping(value = "/cards", produces = "application/json")
	public String getCardPrices(@RequestBody String[] cards) {
		Logger.info("Requested data for " + cards.length + " cards.");
		return crawler.findPrices(cards);
	}
}
