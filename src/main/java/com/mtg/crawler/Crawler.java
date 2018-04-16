package com.mtg.crawler;

import java.util.stream.Stream;

public interface Crawler<T> {

	public Stream<T> find(String card);

}
