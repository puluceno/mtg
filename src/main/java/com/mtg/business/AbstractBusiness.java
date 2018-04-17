package com.mtg.business;

import java.math.BigDecimal;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.jsoup.nodes.Node;

import com.mtg.crawler.Crawler;
import com.mtg.crawler.impl.JsoupCrawler;
import com.mtg.model.Result;

public abstract class AbstractBusiness<T> implements Business<Result> {

	private Crawler<Node> crawler = JsoupCrawler.getInstance();
	private Pattern digitOnly;

	public AbstractBusiness() {
		this.digitOnly = Pattern.compile("\\d+");
	}

	public final Pattern getDigitOnly() {
		return digitOnly;
	}

	protected Stream<Result> buildStream(String card, Class<?> cls) {
		return crawler.find(card).filter(cls::isInstance)
				.map(e -> addDetails(getStore(e), getEdition(e), getFoil(e), getPrice(e), getQty(e)));
	}

	private Result addDetails(String store, String edition, boolean foil, BigDecimal price, Integer qty) {
		return new Result(store, edition, foil, qty, price);
	}

	@Override
	public String getStore(Node n) {
		return n.childNode(1).childNode(0).childNode(0).attr("title");
	}

	@Override
	public boolean getFoil(Node n) {
		return n.childNode(3).childNode(0).childNode(1).childNodes().size() > 1;
	}

	@Override
	public String getEdition(Node n) {
		return n.childNode(3).childNode(0).childNode(1).childNode(0).toString().trim();
	}

	@Override
	public int getQty(Node n) {
		var m = getDigitOnly().matcher(n.childNode(7).childNode(0).toString().trim());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}

	@Override
	public BigDecimal getPrice(Node n) {
		var childNode = n.childNode(5).childNode(0);
		var price = childNode.childNode(childNode.childNodeSize() - 1).toString().trim().replace(".", "");
		return new BigDecimal(price.substring(price.lastIndexOf('$') + 2, price.length()).replace(",", "."));
	}
}
