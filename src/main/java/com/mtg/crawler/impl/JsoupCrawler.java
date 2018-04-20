package com.mtg.crawler.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;

import com.mtg.crawler.AbstractCrawler;
import com.mtg.crawler.Crawler;
import com.mtg.exception.CardNotFoundException;

public class JsoupCrawler extends AbstractCrawler<Node> {

	private static class Helper {
		private static final Crawler<Node> INSTANCE = new JsoupCrawler();
	}

	public static Crawler<Node> getInstance() {
		return Helper.INSTANCE;
	}

	@Override
	public Stream<Element> find(String card) {
		return parse(card).stream();
	}

	private Elements parse(String card) {
		Document doc = null;
		try {
			doc = Jsoup.connect(config.getSearchUrl(card)).proxy("proxy.panamericano.com.br", 8080).get();
		} catch (IOException e) {
			Logger.error(e, "Could not perform HTTP request");
		} catch (URISyntaxException e) {
			Logger.error(e, "Invalid URL built for card: ".concat(card));
		}
		Elements rows = doc.getElementsByAttributeValue("mp", "2");

		if (rows == null) {
			throw new CardNotFoundException(card.concat(" not found!"));
		}

		Logger.info("Found data for ".concat(card));
		return rows;
	}

}
