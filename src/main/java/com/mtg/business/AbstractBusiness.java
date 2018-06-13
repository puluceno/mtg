package com.mtg.business;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.jsoup.nodes.Element;

import com.mtg.infrastructure.crawler.Crawler;
import com.mtg.model.Card;
import com.mtg.model.Result;
import com.mtg.model.Search;
import com.mtg.model.enumtype.State;

public abstract class AbstractBusiness implements Business<Result> {

	private static final String CLASS = "class";
	private final Crawler crawler;
	private Pattern digitOnly;

	public AbstractBusiness(Crawler crawler) {
		this.crawler = crawler;
		this.digitOnly = Pattern.compile("\\d+");
	}

	public final Pattern getDigitOnly() {
		return digitOnly;
	}

	protected Stream<Card> buildStream(Search card) {
		return crawler.find(card.getName()).map(e -> {
			BigDecimal price = getPrice(e);
			return addDetails(card.getName(), getStore(e), getEdition(e), getFoil(e), getLanguage(e),
					getState(e), getQty(e), price);
		});
	}

	private Card addDetails(String name, String store, String edition, boolean foil, String language,
			String state, Integer qty, BigDecimal price) {
		return new Card(name, store, edition, foil, language, state, qty, price);
	}

	// TODO: add getName()

	@Override
	public String getStore(Element e) {
		return e.selectFirst("[title]").attr("title");
	}

	@Override
	public String getEdition(Element e) {
		String edition = e.getElementsByAttributeValue(CLASS, "nomeedicao").text();
		return edition.isEmpty() ? e.getElementsByAttributeValue(CLASS, "edicaoextras").text() : edition;
	}

	@Override
	public boolean getFoil(Element e) {
		return e.getElementsByAttributeValue(CLASS, "extras").hasText();
	}

	@Override
	public String getLanguage(Element e) {
		return e.select("[title]").get(1).attr("title");
	}

	@Override
	public String getState(Element e) {
		return State.valueOf(e.selectFirst("[onclick]").text().replace("/", "")).getDescription();
	}

	@Override
	public int getQty(Element e) {
		Matcher m = getDigitOnly()
				.matcher(e.getElementsByAttributeValue(CLASS, "e-col5 e-col5-offmktplace").text());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}

	@Override
	public BigDecimal getPrice(Element e) {
		String price = e.getElementsByAttributeValue(CLASS, "e-col3").text();
		return new BigDecimal(price.substring(price.lastIndexOf('$') + 2, price.length()).replace(".", "")
				.replace(",", "."));
	}

	@Override
	public BigDecimal getTotalPrice(BigDecimal price, int qty) {
		return price.multiply(new BigDecimal(qty == 0 ? 1 : qty));
	}
}
