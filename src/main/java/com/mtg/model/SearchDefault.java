package com.mtg.model;

public class SearchDefault implements Search {

	private String name;
	private boolean foil;
	private int qty;
	private int limit;

	public SearchDefault() {
	}

	public SearchDefault(String name, boolean foil, int qty, int limit) {
		this.name = name;
		this.foil = foil;
		this.qty = qty;
		this.limit = limit;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isFoil() {
		return foil;
	}

	@Override
	public int getQty() {
		return qty;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public String toString() {
		return "SearchDefault [name=" + name + ", foil=" + foil + ", qty=" + qty + ", limit=" + limit + "]";
	}

}
