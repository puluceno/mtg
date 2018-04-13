package com.mtg.crawler.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.pmw.tinylog.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
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
		HtmlTable table = (HtmlTable) home.getElementById(config.getBaseTable());
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
				table.getRows().stream().filter(r -> !(r.getCell(0) instanceof HtmlTableHeaderCell))
						.map(r -> addDetails(getStore(r), getEdition(r), getFoil(r), getPrice(r), getQty(r)))
						.collect(Collectors.toList()));
	}

	@Override
	public String getStore(Object r) {
		return ((HtmlTableRow) r).getCell(0).getFirstChild().getFirstChild().getAttributes().item(5).getNodeValue();
	}

	@Override
	public boolean getFoil(Object r) {
		return ((HtmlTableRow) r).getCell(1).getLastChild().getLastChild().getLastChild() != null ? true : false;
	}

	@Override
	public String getEdition(Object r) {
		return ((HtmlTableRow) r).getCell(1).getFirstChild().getLastChild().getTextContent().replace(" (Foil)", "");
	}

	@Override
	public int getQty(Object r) {
		Matcher m = getDigitOnly().matcher(((HtmlTableRow) r).getCell(3).getFirstChild().getTextContent());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}

	@Override
	public BigDecimal getPrice(Object r) {
		String trim = ((HtmlTableRow) r).getCell(2).getFirstChild().getTextContent().trim().replace(".", "");
		String price = trim.substring(trim.lastIndexOf('$') + 2, trim.length()).replace(",", ".");
		return new BigDecimal(price);
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
