package com.mtg.crawler.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.pmw.tinylog.Logger;

import com.jsoniter.output.JsonStream;
import com.mtg.crawler.AbstractCrawler;
import com.mtg.crawler.Crawler;
import com.mtg.exception.CardNotFoundException;
import com.mtg.model.Card;
import com.mtg.model.ErrorMessage;
import com.mtg.model.enumtype.ErrorCode;

public class JsoupCrawler extends AbstractCrawler {

	private static class Helper {
		private static final Crawler INSTANCE = new JsoupCrawler();
	}

	public static Crawler getInstance() {
		return Helper.INSTANCE;
	}

	@Override
	public String findPrices(String... cards) {
		return JsonStream.serialize(find(cards));
	}

	private List<Object> find(String[] cards) {
		return Arrays.asList(cards).parallelStream().map(c -> {
			try {
				return find(c);
			} catch (IOException | InterruptedException e) {
				Logger.trace(e);
				return new ErrorMessage("Error when creating data.", ErrorCode.DATA_PARSE);
			} catch (CardNotFoundException e) {
				Logger.info(e.getMessage());
				return new ErrorMessage(e.getMessage(), ErrorCode.CARD_NOT_FOUND);
			} catch (URISyntaxException e) {
				Logger.trace(e);
				return new ErrorMessage("Error when creating searching query.", ErrorCode.INVALID_URL);
			}
		}).collect(Collectors.toList());

	}

	private Card find(String cardName) throws IOException, InterruptedException, URISyntaxException {
		Document doc = Jsoup.connect(config.getSearchUrl(cardName)).get();
		Element table = doc.getElementById(config.getBaseTable());
		int tries = 4;
		while (tries > 0 && table == null) {
			tries--;
			table = doc.getElementById(config.getBaseTable());
			synchronized (table) {
				table.wait(500);
			}
		}

		if (table == null)
			throw new CardNotFoundException(cardName.concat(" not found!"));

		Logger.info("Found data for ".concat(cardName));
		Node tbody = table.childNode(3);

		tbody.childNodes().stream().filter(e -> e instanceof Element).forEach(e -> {
			System.out.println(e);
		});

		// table.getAllElements().stream().filter(r -> {
		// return r.is("tr");
		// }).map(r -> {
		// System.out.println(r.select("td").get(0).text());
		// return "";
		// });
		return null;
	}

}
