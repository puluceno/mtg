package com.mtg.business;

import java.math.BigDecimal;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import com.jsoniter.spi.JsoniterSpi;
import com.mtg.crawler.Crawler;
import com.mtg.crawler.impl.JsoupCrawler;
import com.mtg.model.Result;
import com.mtg.model.Search;
import com.mtg.model.SearchDefault;

public abstract class AbstractBusiness<T> implements Business<Result> {

	private Crawler<Node> crawler = JsoupCrawler.getInstance();
	private Pattern digitOnly;

	public AbstractBusiness() {
		this.digitOnly = Pattern.compile("\\d+");
		JsoniterSpi.registerTypeImplementation(Search.class, SearchDefault.class);
	}

	public final Pattern getDigitOnly() {
		return digitOnly;
	}

	protected Stream<Result> buildStream(String card) {
		return crawler.find(card).map(e -> addDetails(getStore(e), getEdition(e), getFoil(e), getPrice(e), getQty(e)));
	}

	private Result addDetails(String store, String edition, boolean foil, BigDecimal price, Integer qty) {
		return new Result(store, edition, foil, qty, price);
	}

	@Override
	public String getStore(Element n) {
		return n.selectFirst("[title]").attr("title");
	}

	@Override
	public String getEdition(Element n) {
		String edition = n.getElementsByAttributeValue("class", "nomeedicao").text();
		return edition.isEmpty() ? n.getElementsByAttributeValue("class", "edicaoextras").text() : edition;
	}

	@Override
	public boolean getFoil(Element n) {
		return n.getElementsByAttributeValue("class", "extras").hasText();
	}

	@Override
	public BigDecimal getPrice(Element n) {
		var price = n.getElementsByAttributeValue("class", "e-col3").text();
		return new BigDecimal(price.substring(price.lastIndexOf('$') + 2, price.length()).replace(",", "."));
	}

	@Override
	public int getQty(Element n) {
		var m = getDigitOnly().matcher(n.getElementsByAttributeValue("class", "e-col5 e-col5-offmktplace").text());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}
}
