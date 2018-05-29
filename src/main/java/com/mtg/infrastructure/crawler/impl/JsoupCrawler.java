package com.mtg.infrastructure.crawler.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Service;

import com.mtg.exception.CardNotFoundException;
import com.mtg.infrastructure.crawler.Crawler;
import com.mtg.infrastructure.crawler.config.CrawlerConfig;

@Service
public class JsoupCrawler implements Crawler {

	private final CrawlerConfig config;

	public JsoupCrawler(CrawlerConfig config) {
		this.config = config;
	}

	@Override
	public Stream<Element> find(String card) {
		return this.parse(card).stream();
	}

	private Elements parse(String card) {
		Document doc = null;
		try {
			doc = Jsoup.connect(config.getSearchUrl(card)).proxy("proxy.panamericano.com.br", 8080).get();

			Elements rows = doc.getElementsByAttributeValue("mp", "2");
			if (rows == null) {
				throw new CardNotFoundException(card.concat(" not found!"));
			}
			Logger.info("Found data for ".concat(card));
			return rows;
		} catch (IOException e) {
			Logger.error(e, "Could not perform HTTP request");
		} catch (URISyntaxException e) {
			Logger.error(e, "Invalid URL built for card: ".concat(card));
		}
		return new Elements().empty();
	}
}
