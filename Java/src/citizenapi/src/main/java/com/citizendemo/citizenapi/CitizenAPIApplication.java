package com.citizendemo.citizenapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class CitizenAPIApplication {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(CitizenAPIApplication.class);
        logger.info("CitizenAPI starting ...");
        SpringApplication.run(CitizenAPIApplication.class, args);
    }

}
