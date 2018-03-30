package com.mtg.model;

import java.net.URI;
import java.net.URISyntaxException;

public class Config {
	private String baseUrl;
	private String searchUrl;
	private String queryInput;
	private String baseTable;

	public Config() {
		// intentionally left in blank
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getSearchUrl(String card) throws URISyntaxException {
		return new URI(searchUrl.concat(card.replaceAll(" ", "+"))).toASCIIString();
	}

	public String getQueryInput() {
		return queryInput;
	}

	public String getBaseTable() {
		return baseTable;
	}

}
