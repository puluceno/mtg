package com.mtg.crawler;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.pmw.tinylog.Logger;

import com.jsoniter.JsonIterator;
import com.mtg.config.StaticConfig;
import com.mtg.model.Config;
import com.mtg.model.Result;

public abstract class AbstractCrawler<T> implements Crawler<T> {

	protected Config config;

	public AbstractCrawler() {
		try {
			Logger.info("Fetching crawler config...");
			this.config = buildConfig();
		} catch (IOException e) {
			Logger.trace(e, "Could not read config file.");
			System.exit(500);
		}
	}

	private final Config buildConfig() throws IOException {
		var path = Paths.get(StaticConfig.CONFIG_RESOURCE);
		return JsonIterator.deserialize(Files.readAllBytes(path), Config.class);
	}

	public final Config getConfig() {
		return this.config;
	}

	protected Result addDetails(String store, String edition, boolean foil, BigDecimal price, Integer qty) {
		return new Result(store, edition, foil, qty, price);
	}

}
