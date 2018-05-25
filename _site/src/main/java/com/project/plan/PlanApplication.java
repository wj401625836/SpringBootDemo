package com.project.plan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { RedisAutoConfiguration.class })
//@EnableCaching
public class PlanApplication {

	private static Logger logger = LoggerFactory.getLogger(PlanApplication.class);
	/**
	 */
	public static void main(String[] args) {
		SpringApplication.run(PlanApplication.class, args);
		logger.debug("启动成功");
	}
	
}
