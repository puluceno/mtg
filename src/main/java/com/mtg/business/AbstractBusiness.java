package com.mtg.business;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.jsoup.nodes.Node;

import com.jsoniter.output.JsonStream;
import com.mtg.crawler.Crawler;
import com.mtg.crawler.impl.JsoupCrawler;

public abstract class AbstractBusiness implements Business {

	private Crawler<Node> crawler = JsoupCrawler.getInstance();
	private Pattern digitOnly;

	public AbstractBusiness() {
		this.digitOnly = Pattern.compile("\\d+");
	}

	public final Pattern getDigitOnly() {
		return digitOnly;
	}

	@Override
	public String findPrices(String[] cards) {
		Stream<Node> findPrices = crawler.find(cards);

		// Stream<Result> stream = stream2.filter(e -> e instanceof Element)
		// .map(e -> addDetails(getStore(e), getEdition(e), getFoil(e), getPrice(e),
		// getQty(e)));
		//
		// return new Card(cardName, stream.collect(Collectors.toList()));

		return JsonStream.serialize("cards");
	}

	// private List<Object> find(String[] cards) {
	// return Arrays.asList(cards).parallelStream().map(c -> {
	// try {
	// return find(c);
	// } catch (IOException | InterruptedException e) {
	// Logger.trace(e);
	// return new ErrorMessage("Error when creating data.", ErrorCode.DATA_PARSE);
	// } catch (CardNotFoundException e) {
	// Logger.info(e.getMessage());
	// return new ErrorMessage(e.getMessage(), ErrorCode.CARD_NOT_FOUND);
	// } catch (URISyntaxException e) {
	// Logger.trace(e);
	// return new ErrorMessage("Error when creating searching query.",
	// ErrorCode.INVALID_URL);
	// }
	// }).collect(Collectors.toList());
	// }

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
		Matcher m = getDigitOnly().matcher(n.childNode(7).childNode(0).toString().trim());
		return Integer.parseInt(m.find() ? m.group() : "0");
	}

	@Override
	public BigDecimal getPrice(Node n) {
		Node childNode = n.childNode(5).childNode(0);
		String sPrice = childNode.childNode(childNode.childNodeSize() - 1).toString().trim().replace(".", "");
		return new BigDecimal(sPrice.substring(sPrice.lastIndexOf('$') + 2, sPrice.length()).replace(",", "."));
	}
}
