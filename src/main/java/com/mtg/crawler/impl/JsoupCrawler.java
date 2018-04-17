package com.mtg.crawler.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
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
	public Stream<Node> find(String card) {
		return parse(card).stream();
	}

	private List<Node> parse(String card) {
		Document doc = null;
		try {
			doc = Jsoup.connect(config.getSearchUrl(card)).get();
		} catch (IOException e) {
			Logger.error(e, "Could not perform HTTP request");
		} catch (URISyntaxException e) {
			Logger.error(e, "Invalid URL built for card: ".concat(card));
		}
		var table = doc.getElementById(config.getBaseTable());

		if (table == null)
			throw new CardNotFoundException(card.concat(" not found!"));

		Logger.info("Found data for ".concat(card));
		return table.childNode(3).childNodes();
	}

}
