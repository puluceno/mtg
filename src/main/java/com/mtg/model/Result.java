package com.mtg.model;

import java.math.BigDecimal;

public class Result {
	private final String store;
	private final String edition;
	private final boolean foil;
	private final int qty;
	private final BigDecimal price;

	public Result(String store, String edition, boolean foil, int qty, BigDecimal price) {
		this.store = store;
		this.edition = edition;
		this.foil = foil;
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

	public int getQty() {
		return qty;
	}

	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "Result [store=" + store + ", edition=" + edition + ", foil=" + foil + ", qty=" + qty + ", price="
				+ price + "]";
	}

}
