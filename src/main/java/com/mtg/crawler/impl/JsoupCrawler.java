package com.mtg.crawler.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
	public Stream<Node> find(String[] cards) {
		return Arrays.asList(cards).parallelStream().map(this::find);
	}

	private Node find(String cardName) {
		Document doc = null;
		try {
			doc = Jsoup.connect(config.getSearchUrl(cardName)).get();
		} catch (IOException e) {
			Logger.error(e, "Could not perform HTTP request");
		} catch (URISyntaxException e) {
			Logger.error(e, "Invalid URL built for card: ".concat(cardName));
		}
		Element table = doc.getElementById(config.getBaseTable());

		if (table == null)
			throw new CardNotFoundException(cardName.concat(" not found!"));

		Logger.info("Found data for ".concat(cardName));
		return table.childNode(3);
	}

}
