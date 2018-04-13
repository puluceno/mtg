package com.mtg.crawler;

import java.math.BigDecimal;
import java.util.stream.Stream;

public interface Crawler<T> {

	public Stream<T> findPrices(String... cards);

	public String getStore(Object r);

	public boolean getFoil(Object r);

	public String getEdition(Object r);

	public int getQty(Object r);

	public BigDecimal getPrice(Object r);

}
