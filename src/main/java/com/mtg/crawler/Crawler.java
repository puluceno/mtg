package com.mtg.crawler;

import java.util.logging.Level;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Crawler {

	@BeforeClass
	public static void before() {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
	}

	@Test
	public void findPrices() throws Exception {
		try (final WebClient client = new WebClient(BrowserVersion.CHROME)) {
			client.getOptions().setHistorySizeLimit(1);
			client.getOptions().setCssEnabled(false);
			client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			client.getOptions().setThrowExceptionOnScriptError(false);
			client.getOptions().setPrintContentOnFailingStatusCode(false);
			client.getOptions().setUseInsecureSSL(true);
			client.getOptions().setDoNotTrackEnabled(true);
			client.getOptions().setJavaScriptEnabled(true);
			client.setJavaScriptTimeout(4500);

			HtmlPage home = client.getPage("https://www.ligamagic.com");

			HtmlTextInput query = (HtmlTextInput) home.getElementById("query");
			query.setValueAttribute("fatal push");
			DomElement nextElementSibling = query.getNextElementSibling();
			home = nextElementSibling.click();

			HtmlTable table = null;
			int tries = 5;

			while (tries > 0 && table == null) {
				tries--;
				table = (HtmlTable) home.getElementById("cotacao-1");
				synchronized (home) {
					home.wait(1500);
				}
			}

			for (int i = 1; i < table.getRowCount(); i++)
				for (final HtmlTableCell cell : table.getRow(i).getCells()) {
					HtmlImage img = (HtmlImage) cell
							.getByXPath("//*[@id=\"cotacao-1\"]/tbody/tr[" + i + "]/td[1]/a/img").get(0);
					System.out.println(img.getAttribute("title"));

					HtmlTableDataCell price = (HtmlTableDataCell) cell
							.getByXPath("//*[@id=\"cotacao-1\"]/tbody/tr[" + i + "]/td[3]").get(0);
					System.out.println(price.asText());

					HtmlTableDataCell stock = (HtmlTableDataCell) cell
							.getByXPath("//*[@id=\"cotacao-1\"]/tbody/tr[" + i + "]/td[4]").get(0);
					System.out.println(stock.asText());
				}

		}
	}
}
