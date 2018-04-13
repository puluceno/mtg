package com.mtg.crawler.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.pmw.tinylog.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.mtg.crawler.AbstractCrawler;
import com.mtg.crawler.Crawler;
import com.mtg.exception.CardNotFoundException;
import com.mtg.model.Card;

public class CrawlerDefault extends AbstractCrawler<Object> {

	private static class Helper {
		private static final Crawler<Object> INSTANCE = new CrawlerDefault();
	}

	public static Crawler<Object> getInstance() {
		return Helper.INSTANCE;
	}

	@Override
	public Stream<Object> find(String[] cards) {
		return Arrays.asList(cards).parallelStream().map(this::find);
	}

	private Card find(String cardName) {
		HtmlPage home = null;
		try {
			home = getBrowserInstance().getPage(config.getSearchUrl(cardName));
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		HtmlTable table = (HtmlTable) home.getElementById(config.getBaseTable());
		int tries = 4;
		while (tries > 0 && table == null) {
			tries--;
			table = (HtmlTable) home.getElementById(config.getBaseTable());
			synchronized (home) {
				// home.wait(500);
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

	private String getStore(Object r) {
		return ((HtmlTableRow) r).getCell(0).getFirstChild().getFirstChild().getAttributes().item(5).getNodeValue();
	}

	private boolean getFoil(Object r) {
		return ((HtmlTableRow) r).getCell(1).getLastChild().getLastChild().getLastChild() != null ? true : false;
	}

	private String getEdition(Object r) {
		return ((HtmlTableRow) r).getCell(1).getFirstChild().getLastChild().getTextContent().replace(" (Foil)", "");
	}

	private int getQty(Object r) {
		Matcher m = Pattern.compile("\\d+").matcher(((HtmlTableRow) r).getCell(3).getFirstChild().getTextContent());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}

	private BigDecimal getPrice(Object r) {
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
