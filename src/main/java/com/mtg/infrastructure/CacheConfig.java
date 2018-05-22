package com.mtg.infrastructure;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class CacheConfig extends CachingConfigurerSupport {

	// @Value("${redis.hostname}")
	// private String redisHostName;
	//
	// @Value("${redis.port}")
	// private int redisPort;

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		return new JedisConnectionFactory();
	}
	//
	// @Bean
	// public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf)
	// {
	// RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
	// redisTemplate.setConnectionFactory(cf);
	// return redisTemplate;
	// }

	@Override
	@Bean(name = "cacheManager")
	public CacheManager cacheManager() {
		RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofSeconds(1)).disableCachingNullValues()
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()));
		return RedisCacheManager.builder(redisConnectionFactory()).cacheDefaults(cacheConfiguration).build();
	}

}
