package com.mtg.model;

import java.math.BigDecimal;

public class Result {
	private final String store;
	private final String edition;
	private final boolean foil;
	private final String language;
	private final int qty;
	private final String state;
	private final BigDecimal price;

	public Result(final String store, final String edition, final boolean foil, final String language,
			final String state, final int qty, final BigDecimal price) {
		this.store = store;
		this.edition = edition;
		this.foil = foil;
		this.language = language;
		this.state = state;
		this.qty = qty;
		this.price = price;
	}

	public String getStore() {
		return store;
	}

	public String getEdition() {
		return edition;
	}

	public boolean isFoil() {
		return foil;
	}

	public String getLanguage() {
		return language;
	}

	public int getQty() {
		return qty;
	}

	public String getState() {
		return state;
	}

	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "Result [store=" + store + ", edition=" + edition + ", foil=" + foil + ", language=" + language
				+ ", qty=" + qty + ", state=" + state + ", price=" + price + "]";
	}

}
