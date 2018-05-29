package com.mtg.server;

import org.pmw.tinylog.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = { "com.mtg" })
@PropertySource({ "classpath:application-${profile:dev}.properties" })
@EnableCaching
public class Server {

	public static void main(String[] args) {
		Logger.info("Starting server...");
		SpringApplication.run(Server.class, args);
	}
}