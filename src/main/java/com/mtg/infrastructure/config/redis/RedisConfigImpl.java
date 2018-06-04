/**
 *
 * @author Thiago Puluceno <thiago.puluceno@grupopan.com>
 */
package com.mtg.infrastructure.config.redis;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Primary
@Component("redisConfig")
public class RedisConfigImpl implements RedisConfig {

	private boolean enabled;
	private String host;
	private String type;
	private int port;
	private int ttl;
	private String unit;

}