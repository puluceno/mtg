package com.mtg.infrastructure.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.stereotype.Component;

import com.mtg.infrastructure.config.redis.RedisConfig;

@Component
public class CacheConfig extends CachingConfigurerSupport {

	private final RedisConfig config;

	public CacheConfig(RedisConfig config) {
		this.config = config;
	}

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	@Override
	@Bean(name = "cacheManager")
	public CacheManager cacheManager() {
		RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.disableCachingNullValues().entryTtl(Duration.ofSeconds(config.getTtl()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()));
		return RedisCacheManager.builder(redisConnectionFactory()).cacheDefaults(cacheConfiguration).build();
	}

}
