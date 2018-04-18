package com.mtg.business;

import java.math.BigDecimal;

import org.jsoup.nodes.Element;

public interface Business<T> {

	public String findPrices(String cards);

	public String getStore(Element n);

	public boolean getFoil(Element n);

	public String getEdition(Element n);

	public int getQty(Element n);

	public BigDecimal getPrice(Element n);
}
