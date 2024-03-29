package com.citizendemo.resourceapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ResourceAPIApplication {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ResourceAPIApplication.class);
        logger.info("ResourceAPI starting ...");
        SpringApplication.run(ResourceAPIApplication.class, args);
    }

}
