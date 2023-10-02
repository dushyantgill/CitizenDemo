package com.citizendemo.provisionworker;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProvisionWorkerApplication {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ProvisionWorkerApplication.class);
        Metrics.globalRegistry.add(new SimpleMeterRegistry());
        logger.info("ProvisionWorker starting ...");
        SpringApplication.run(ProvisionWorkerApplication.class, args);
    }
}
