package com.mtg.business;

import java.math.BigDecimal;

import org.jsoup.nodes.Node;

public interface Business<T> {

	public String findPrices(String cards);

	public String getStore(Node n);

	public boolean getFoil(Node n);

	public String getEdition(Node n);

	public int getQty(Node n);

	public BigDecimal getPrice(Node n);
}
