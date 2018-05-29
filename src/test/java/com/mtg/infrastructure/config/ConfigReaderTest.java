/**
 *
 * @author Thiago Puluceno <thiago.puluceno@grupopan.com>
 */
package com.mtg.infrastructure.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.mtg.infrastructure.crawler.config.CrawlerConfig;

@ContextConfiguration
@TestPropertySource("classpath:application-dev.properties")
public class ConfigReaderTest {

	private CrawlerConfig crawlerConfig;

	public void init() {

	}

	@Test
	public void readCrawlerConfigFile() {
		assertNotNull(crawlerConfig);
	}
}
