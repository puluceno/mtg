package com.mtg.model.enumtype;

public enum State {

	M("Mint"), NM("Near Mint"), SP("Slightly Played"), MP("Moderately Played"), HP("Heavily Played"), DM("Damaged"), NA(
			"Not Available");

	private final String state;

	State(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

}
