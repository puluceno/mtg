package com.mtg.business;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.jsoup.nodes.Node;

public interface Business<T> {

	public Stream<T> buildStream(String card, Class<?> cls);

	public String findPrices(String[] cards);

	public String getStore(Node n);

	public boolean getFoil(Node n);

	public String getEdition(Node n);

	public int getQty(Node n);

	public BigDecimal getPrice(Node n);
}
