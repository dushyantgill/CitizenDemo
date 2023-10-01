package com.citizendemo.provisionworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProvisionWorkerApplication {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ProvisionWorkerApplication.class);
        logger.info("ProvisionWorker starting ...");
        SpringApplication.run(ProvisionWorkerApplication.class, args);
    }
}
