package com.mtg.model;

public class Search {

	private String name;
	private boolean foil;
	private int qty;
	private int limit;

	public Search() {
	}

	public Search(String name, boolean foil, int qty, int limit) {
		this.name = name;
		this.foil = foil;
		this.qty = qty;
		this.limit = limit;
	}

	public String getName() {
		return name;
	}

	public boolean isFoil() {
		return foil;
	}

	public int getQty() {
		return qty;
	}

	public int getLimit() {
		return limit;
	}

	@Override
	public String toString() {
		return "Search [name=" + name + ", foil=" + foil + ", qty=" + qty + ", limit=" + limit + "]";
	}

}
