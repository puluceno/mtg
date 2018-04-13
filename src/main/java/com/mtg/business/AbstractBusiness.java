package com.mtg.business;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.pmw.tinylog.Logger;

import com.jsoniter.output.JsonStream;
import com.mtg.crawler.Crawler;
import com.mtg.crawler.impl.JsoupCrawler;
import com.mtg.exception.CardNotFoundException;
import com.mtg.model.Card;
import com.mtg.model.ErrorMessage;
import com.mtg.model.Result;
import com.mtg.model.enumtype.ErrorCode;

public abstract class AbstractBusiness implements Business {

	// private Crawler crawler = CrawlerDefault.getInstance();
	private Crawler crawler = JsoupCrawler.getInstance();

	@Override
	public String findPrices(String[] cards) {
		Stream findPrices = crawler.findPrices(cards);

		Stream<Result> stream = stream2.filter(e -> e instanceof Element)
				.map(e -> addDetails(getStore(e), getEdition(e), getFoil(e), getPrice(e), getQty(e)));

		return new Card(cardName, stream.collect(Collectors.toList()));

		return JsonStream.serialize("cards");
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

}
