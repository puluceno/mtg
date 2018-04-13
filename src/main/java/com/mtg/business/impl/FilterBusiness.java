package com.mtg.business.impl;

import com.mtg.business.AbstractBusiness;
import com.mtg.business.Business;

public class FilterBusiness extends AbstractBusiness {

	private static class Helper {
		private static final Business INSTANCE = new FilterBusiness();
	}

	public static Business getInstance() {
		return Helper.INSTANCE;
	}
}
