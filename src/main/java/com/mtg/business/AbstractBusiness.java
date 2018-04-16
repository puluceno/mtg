package com.mtg.business;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.jsoup.nodes.Element;
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

	@Override
	public Stream<Result> buildStream(String card) {
		Stream<Node> findPrices = crawler.find(card);

		return findPrices.filter(e -> e instanceof Element)
				.map(e -> addDetails(getStore(e), getEdition(e), getFoil(e), getPrice(e), getQty(e)));
	}

	private Result addDetails(String store, String edition, boolean foil, BigDecimal price, Integer qty) {
		return new Result(store, edition, foil, qty, price);
	}

	@Override
	public String getStore(Node n) {
		return n.childNode(1).childNode(1).childNode(0).childNode(0).attr("title");
	}

	@Override
	public boolean getFoil(Node n) {
		return n.childNode(1).childNode(3).childNode(0).childNode(1).childNodes().size() > 1;
	}

	@Override
	public String getEdition(Node n) {
		return n.childNode(1).childNode(3).childNode(0).childNode(1).childNode(0).toString().trim();
	}

	@Override
	public int getQty(Node n) {
		Matcher m = getDigitOnly().matcher(n.childNode(1).childNode(7).childNode(0).toString().trim());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}

	@Override
	public BigDecimal getPrice(Node n) {
		Node childNode = n.childNode(1).childNode(5).childNode(0);
		String sPrice = childNode.childNode(childNode.childNodeSize() - 1).toString().trim().replace(".", "");
		return new BigDecimal(sPrice.substring(sPrice.lastIndexOf('$') + 2, sPrice.length()).replace(",", "."));
	}
}
