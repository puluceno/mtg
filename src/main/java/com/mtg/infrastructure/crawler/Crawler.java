package com.mtg.infrastructure.crawler;

import java.util.stream.Stream;

import org.jsoup.nodes.Element;

public interface Crawler {

	public Stream<Element> find(String card);

}
