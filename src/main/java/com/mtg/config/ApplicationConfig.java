/**
 *
 * @author Thiago Puluceno <thiago.puluceno@grupopan.com>
 */
package com.mtg.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.mtg.infrastructure.config.redis.RedisConfigImpl;
import com.mtg.infrastructure.crawler.config.CrawlerConfigImpl;

@Configuration
@EnableConfigurationProperties(value = { RedisConfigImpl.class, CrawlerConfigImpl.class })
public class ApplicationConfig {

}
