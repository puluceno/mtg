package com.mtg.crawler;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.pmw.tinylog.Logger;

import com.jsoniter.JsonIterator;
import com.mtg.config.StaticConfig;
import com.mtg.model.Config;
import com.mtg.model.Result;

public abstract class AbstractCrawler implements Crawler {

	protected Config config;
	private Pattern digitOnly;

	public AbstractCrawler() {
		try {
			Logger.info("Fetching crawler config...");
			this.config = buildConfig();
			this.digitOnly = Pattern.compile("\\d+");
		} catch (IOException e) {
			Logger.trace(e, "Could not read config file.");
			System.exit(500);
		}
	}

	private final Config buildConfig() throws IOException {
		Path path = Paths.get(StaticConfig.CONFIG_RESOURCE);
		return JsonIterator.deserialize(Files.readAllBytes(path), Config.class);
	}

	public final Config getConfig() {
		return this.config;
	}

	public final Pattern getDigitOnly() {
		return digitOnly;
	}

	protected Result addDetails(String store, String edition, boolean foil, BigDecimal price, Integer qty) {
		return new Result(store, edition, foil, qty, price);
	}

}
