package com.mtg.model;

public interface Search {

	public String getName();

	public boolean isFoil();

	public int getQty();

	public int getLimit();

	@Override
	public String toString();
}
