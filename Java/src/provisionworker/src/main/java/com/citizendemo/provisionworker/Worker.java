package com.citizendemo.provisionworker;

import com.citizendemo.provisionworker.models.Citizen;
import com.citizendemo.provisionworker.models.Resource;
import com.citizendemo.provisionworker.services.CitizenService;
import com.citizendemo.provisionworker.services.ResourceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.kafka.annotation.KafkaListener;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


@Component
public class Worker {
    private Logger logger = LoggerFactory.getLogger(Worker.class);
    @Value("${resourceAPI.url}")
    private String resourceServiceURL;
    @Value("${citizenAPI.url}")
    private String citizenServiceURL;
    @KafkaListener(topics = "citizendemo", groupId = "citizendemo")
    public void Worker(String message) {
        Resource resource = null;
        Citizen citizen = null;
        Long latency;
        try {
            resource = ResourceService.GetResourceById(message, resourceServiceURL);
            if(resource != null) {
                citizen = CitizenService.GetCitizenById(resource.citizenId, citizenServiceURL);
                if(citizen != null) {
                    ResourceService.ProvisionResource(resource.resourceId, resourceServiceURL);
                    Thread.sleep(new Random().nextInt(500));
                    latency = Date.from(Instant.now()).getTime() - citizen.dateCreated.getTime();
                    logger.info("Provisioned resource " + resource.resourceId + " for citizen " + citizen.citizenId + " with " + latency + " ms latency.");
                }
            }
        } catch (Exception e) {
            logger.warn("Provisioning failed for resource " + message, e);
        }
    }
}
