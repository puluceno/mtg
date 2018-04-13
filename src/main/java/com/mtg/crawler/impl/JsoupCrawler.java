package com.mtg.crawler.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.mtg.model.Result;
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

		if (table == null)
			throw new CardNotFoundException(cardName.concat(" not found!"));

		Logger.info("Found data for ".concat(cardName));
		Node tbody = table.childNode(3);

		Stream<Result> stream = tbody.childNodes().stream().filter(e -> e instanceof Element)
				.map(e -> addDetails(getStore(e), getEdition(e), getFoil(e), getPrice(e), getQty(e)));

		return new Card(cardName, stream.collect(Collectors.toList()));
	}

	@Override
	public String getStore(Object e) {
		return ((Node) e).childNode(1).childNode(0).childNode(0).attr("title");
	}

	@Override
	public boolean getFoil(Object e) {
		return ((Node) e).childNode(3).childNode(0).childNode(1).childNodes().size() > 1;
	}

	@Override
	public String getEdition(Object e) {
		return ((Node) e).childNode(3).childNode(0).childNode(1).childNode(0).toString().trim();
	}

	@Override
	public int getQty(Object e) {
		Matcher m = getDigitOnly().matcher(((Node) e).childNode(7).childNode(0).toString().trim());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}

	@Override
	public BigDecimal getPrice(Object e) {
		Node childNode = ((Node) e).childNode(5).childNode(0);
		String sPrice = childNode.childNode(childNode.childNodeSize() - 1).toString().trim().replace(".", "");
		return new BigDecimal(sPrice.substring(sPrice.lastIndexOf('$') + 2, sPrice.length()).replace(",", "."));
	}

}
