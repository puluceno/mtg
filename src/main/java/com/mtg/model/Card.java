package com.mtg.model;

import java.math.BigDecimal;

public class Card {

	private final String name;
	private final String store;
	private final String edition;
	private final boolean foil;
	private final String language;
	private final int qty;
	private final String state;
	private final BigDecimal price;

	public Card(final String name, final String store, final String edition, final boolean foil,
			final String language, final String state, final int qty, final BigDecimal price) {
		this.name = name;
		this.store = store;
		this.edition = edition;
		this.foil = foil;
		this.language = language;
		this.state = state;
		this.qty = qty;
		this.price = price;
	}

	public String getName() {
		return name;
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
		return "Result [name=" + name + ", store=" + store + ", edition=" + edition + ", foil=" + foil
				+ ", language=" + language + ", qty=" + qty + ", state=" + state + ", price=" + price + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Card other = (Card) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
