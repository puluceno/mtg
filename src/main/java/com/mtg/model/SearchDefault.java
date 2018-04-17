package com.mtg.model;

public class SearchDefault implements Search {

	private final String name;
	private final boolean foil;
	private final int qty;
	private final int limit;

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

}
