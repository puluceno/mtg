package com.mtg.crawler;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmw.tinylog.Logger;

import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.jsoniter.JsonIterator;
import com.mtg.config.StaticConfig;
import com.mtg.model.Config;

public abstract class AbstractCrawler implements Crawler {

	protected Config config;

	public AbstractCrawler() {
		try {
			Logger.info("Fetching crawler config...");
			this.config = buildConfig();
		} catch (IOException e) {
			Logger.trace(e, "Could not read config file.");
			System.exit(500);
		}
	}

	private Config buildConfig() throws IOException {
		Path path = Paths.get(StaticConfig.CONFIG_RESOURCE);
		return JsonIterator.deserialize(Files.readAllBytes(path), Config.class);
	}

	public Config getConfig() {
		return this.config;
	}

	protected Map<String, Object> addDetails(String store, String edition, boolean foil, BigDecimal price,
			Integer qty) {
		Map<String, Object> details = new HashMap<>();
		details.put("store", store);
		details.put("edition", edition);
		details.put("foil", foil);
		details.put("price", price);
		details.put("qty", qty);
		return details;
	}

	protected String getStore(HtmlTableRow r) {
		return r.getCell(0).getFirstChild().getFirstChild().getAttributes().item(5).getNodeValue();
	}

	protected boolean getFoil(HtmlTableRow r) {
		return r.getCell(1).getLastChild().getLastChild().getLastChild() != null ? true : false;
	}

	protected String getEdition(HtmlTableRow r) {
		return r.getCell(1).getFirstChild().getLastChild().getTextContent().replace(" (Foil)", "");
	}

	protected int getQty(HtmlTableRow r) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher m = pattern.matcher(r.getCell(3).getFirstChild().getTextContent());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}

	protected BigDecimal getPrice(HtmlTableRow r) {
		String trim = r.getCell(2).getFirstChild().getTextContent().trim().replace(".", "");
		String price = trim.substring(trim.lastIndexOf('$') + 2, trim.length()).replace(",", ".");
		return new BigDecimal(price);
	}

}
