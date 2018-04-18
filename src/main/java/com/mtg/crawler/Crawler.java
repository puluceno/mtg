package com.mtg.crawler;

import java.util.stream.Stream;

import org.jsoup.nodes.Element;

public interface Crawler<T> {

	public Stream<Element> find(String card);

}
