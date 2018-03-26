package com.mtg.crawler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.jsoniter.output.JsonStream;
import com.mtg.exception.CardNotFoundException;
import com.mtg.model.Card;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class Crawler {
	private WebClient client;
	private HtmlPage home;
	private HtmlTextInput query;
	private DomElement submitButton;

	private static class Helper {
		private static final Crawler INSTANCE = new Crawler();
	}

	public static Crawler getInstance() {
		return Helper.INSTANCE;
	}

	private Crawler() {
		((Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.OFF);

		client = new WebClient(BrowserVersion.CHROME);
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

		try {
			home = client.getPage("https://www.ligamagic.com");
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
			System.out.println("Failed to fetch home page!");
			System.exit(404);
		}
		query = (HtmlTextInput) home.getElementById("query");
		submitButton = query.getNextElementSibling();
	}

	public String findPrices(String cardName) {
		try {
			return JsonStream.serialize(find(cardName));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return "Error!";
		} catch (CardNotFoundException e) {
			return e.getMessage();
		}
	}

	private Card find(String cardName) throws IOException, InterruptedException {
		query.setValueAttribute(cardName);
		home = submitButton.click();

		HtmlTable table = null;
		int tries = 10;
		while (tries > 0 && table == null) {
			tries--;
			table = (HtmlTable) home.getElementById("cotacao-1");
			synchronized (home) {
				home.wait(500);
			}
		}

		if (table == null)
			throw new CardNotFoundException(cardName.concat(" not found!"));

		return new Card(cardName,
				table.getRows().parallelStream().filter(r -> !(r.getCell(0) instanceof HtmlTableHeaderCell))
						.map(r -> addDetails(getStore(r), getEdition(r), getFoil(r), getPrice(r), getQty(r)))
						.collect(Collectors.toList()));
	}

	private String getStore(HtmlTableRow r) {
		return r.getCell(0).getFirstChild().getFirstChild().getAttributes().item(5).getNodeValue();
	}

	private boolean getFoil(HtmlTableRow r) {
		return r.getCell(1).getLastChild().getLastChild().getLastChild() != null ? true : false;
	}

	private String getEdition(HtmlTableRow r) {
		return r.getCell(1).getFirstChild().getLastChild().getTextContent().replace(" (Foil)", "");
	}

	private int getQty(HtmlTableRow r) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher m = pattern.matcher(r.getCell(3).getFirstChild().getTextContent());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}

	private BigDecimal getPrice(HtmlTableRow r) {
		String trim = r.getCell(2).getFirstChild().getTextContent().trim().replace(".", "");
		String price = trim.substring(trim.lastIndexOf('$') + 2, trim.length()).replace(",", ".");
		return new BigDecimal(price);
	}

	private Map<String, Object> addDetails(String store, String edition, boolean foil, BigDecimal price, Integer qty) {
		Map<String, Object> details = new HashMap<>();
		details.put("store", store);
		details.put("edition", edition);
		details.put("foil", foil);
		details.put("price", price);
		details.put("qty", qty);
		return details;
	}

	public void destroy() {
		client.close();
	}
}
