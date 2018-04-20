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
import com.mtg.model.enumtype.State;

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
		return crawler.find(card).map(e -> addDetails(getStore(e), getEdition(e), getFoil(e), getLanguage(e),
				getState(e), getQty(e), getPrice(e)));
	}

	private Result addDetails(String store, String edition, boolean foil, String language, String state, Integer qty,
			BigDecimal price) {
		return new Result(store, edition, foil, language, state, qty, price);
	}

	@Override
	public String getStore(Element e) {
		return e.selectFirst("[src]").attr("src").substring(2);
	}

	@Override
	public String getEdition(Element e) {
		String edition = e.getElementsByAttributeValue("class", "nomeedicao").text();
		return edition.isEmpty() ? e.getElementsByAttributeValue("class", "edicaoextras").text() : edition;
	}

	@Override
	public boolean getFoil(Element e) {
		return e.getElementsByAttributeValue("class", "extras").hasText();
	}

	@Override
	public String getLanguage(Element e) {
		return e.selectFirst("[title]").attr("title");
	}

	@Override
	public String getState(Element e) {
		return State.valueOf(e.selectFirst("[onclick]").text()).getState();
	}

	@Override
	public int getQty(Element e) {
		var m = getDigitOnly().matcher(e.getElementsByAttributeValue("class", "e-col5 e-col5-offmktplace").text());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}

	@Override
	public BigDecimal getPrice(Element e) {
		var price = e.getElementsByAttributeValue("class", "e-col3").text();
		return new BigDecimal(price.substring(price.lastIndexOf('$') + 2, price.length()).replace(",", "."));
	}

}
