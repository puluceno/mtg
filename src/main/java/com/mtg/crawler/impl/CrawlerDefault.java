package com.mtg.crawler.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.pmw.tinylog.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.jsoniter.output.JsonStream;
import com.mtg.crawler.AbstractCrawler;
import com.mtg.crawler.Crawler;
import com.mtg.exception.CardNotFoundException;
import com.mtg.model.Card;
import com.mtg.model.ErrorMessage;
import com.mtg.model.enumtype.ErrorCode;

public class CrawlerDefault extends AbstractCrawler {

	private static class Helper {
		private static final Crawler INSTANCE = new CrawlerDefault();
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
		HtmlPage home = getBrowserInstance().getPage(config.getSearchUrl(cardName));
		HtmlTable table = null;
		int tries = 4;
		while (tries > 0 && table == null) {
			tries--;
			table = (HtmlTable) home.getElementById(config.getBaseTable());
			synchronized (home) {
				home.wait(500);
			}
		}

		if (table == null)
			throw new CardNotFoundException(cardName.concat(" not found!"));

		Logger.info("Found data for ".concat(cardName));
		return new Card(cardName,
				table.getRows().parallelStream().filter(r -> !(r.getCell(0) instanceof HtmlTableHeaderCell))
						.map(r -> addDetails(getStore(r), getEdition(r), getFoil(r), getPrice(r), getQty(r)))
						.collect(Collectors.toList()));
	}

	private WebClient getBrowserInstance() {
		WebClient client = new WebClient(BrowserVersion.CHROME);
		client.setCssErrorHandler(new SilentCssErrorHandler());
		client.getOptions().setHistorySizeLimit(1);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.getOptions().setPrintContentOnFailingStatusCode(false);
		client.getOptions().setUseInsecureSSL(true);
		client.getOptions().setDoNotTrackEnabled(true);
		client.getOptions().setJavaScriptEnabled(false);
		client.getOptions().setDownloadImages(false);
		client.getOptions().setHistoryPageCacheLimit(1);

		return client;
	}

}
