/**
 *
 * @author Thiago Puluceno <thiago.puluceno@grupopan.com>
 */
package com.mtg.infrastructure.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis")
public interface RedisConfig {

	public String getHost();

	public String getType();

	public int getPort();

	public int getTtl();

	public String getUnit();
}
