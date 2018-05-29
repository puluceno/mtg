/**
 *
 * @author Thiago Puluceno <thiago.puluceno@grupopan.com>
 */
package com.mtg.infrastructure.config.redis;

import lombok.Data;

@Data
public class RedisConfigImpl implements RedisConfig {

	private String host;
	private String type;
	private int port;
	private int ttl;
	private String unit;

}