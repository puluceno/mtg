/**
 *
 * @author Thiago Puluceno <thiago.puluceno@grupopan.com>
 */
package com.mtg.infrastructure.crawler.config;

import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("crawler")
public interface CrawlerConfig {

	public String getSearchUrl(String card) throws URISyntaxException;

	public String getBaseTable();
}
