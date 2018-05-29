package com.mtg.infrastructure.crawler.config;

import java.net.URI;
import java.net.URISyntaxException;

public class CrawlerConfigImpl implements CrawlerConfig {

	private String searchUrl;
	private String baseTable;

	public CrawlerConfigImpl() {
		// intentionally left in blank
	}

	@Override
	public String getSearchUrl(String card) throws URISyntaxException {
		return new URI(searchUrl.concat(card.replaceAll(" ", "+"))).toASCIIString();
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	@Override
	public String getBaseTable() {
		return baseTable;
	}

	public void setBaseTable(String baseTable) {
		this.baseTable = baseTable;
	}

}
