package com.citizendemo.provisionworker.services;

import com.citizendemo.provisionworker.models.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class ResourceService {
    private static RestTemplate restTemplate = new RestTemplate();
    public static Resource GetResourceById(String resourceId, String resourceServiceURL) {
        return restTemplate.getForObject(resourceServiceURL + "/" + resourceId, Resource.class);
    }
    public static void ProvisionResource(String resourceId, String resourceServiceURL) {
        Resource resource = new Resource(
                null,
                null,
                null,
                "provisioned"
        );
        restTemplate.put(resourceServiceURL + "/" + resourceId, resource);
    }
}