package com.mtg.business;

import java.math.BigDecimal;

import org.jsoup.nodes.Element;

public interface Business<T> {

	public Object findPrices(String cards);

	public String getStore(Element e);

	public boolean getFoil(Element e);

	public String getEdition(Element e);

	public String getLanguage(Element e);

	public String getState(Element e);

	public int getQty(Element e);

	public BigDecimal getPrice(Element e);

	public BigDecimal getTotalPrice(BigDecimal price, int qty);

}
