package com.mtg.crawler;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Crawler {
	private static WebClient client;
	private static HtmlPage home;
	private static HtmlTextInput query;
	private static DomElement submitButton;

	@BeforeClass
	public static void before() throws IOException {
		long begin = System.nanoTime();
		init();
		System.out.println("---- Startup time: " + (System.nanoTime() - begin) / 1000000 + "ms");
	}

	@AfterClass
	public static void after() {
		destroy();
	}

	@Test
	public void findPrices() throws IOException, InterruptedException {
		long begin = System.nanoTime();

		find("fatal push");
		System.out.println("******Elapsed time: " + (System.nanoTime() - begin) / 1000000 + "ms");

		begin = System.nanoTime();
		find("ripjaw raptor");
		System.out.println("******Elapsed time: " + (System.nanoTime() - begin) / 1000000 + "ms");
	}

	private void find(String cardName) throws IOException, InterruptedException {
		query.setValueAttribute(cardName);
		home = submitButton.click();

		HtmlTable table = null;
		int tries = 5;

		while (tries > 0 && table == null) {
			tries--;
			table = (HtmlTable) home.getElementById("cotacao-1");
			synchronized (home) {
				home.wait(500);
			}
		}

		long begin = System.nanoTime();
		List<String> collect = table.getRows().stream().filter(r -> !(r.getCell(0) instanceof HtmlTableHeaderCell))
				.map(r -> r.getCell(0).getFirstChild().getFirstChild().getAttributes().item(5).getNodeValue())
				.collect(Collectors.toList());

		System.out.println(collect.size() + "----------------------------------" + (begin - System.nanoTime()));
	}

	private static void init() throws IOException {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies")
				.setLevel(Level.OFF);

		client = new WebClient(BrowserVersion.CHROME);
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

		home = client.getPage("https://www.ligamagic.com");
		query = (HtmlTextInput) home.getElementById("query");
		submitButton = query.getNextElementSibling();
	}

	private static void destroy() {
		client.close();
	}
}
