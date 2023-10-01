package com.citizendemo.provisionworker.services;

import com.citizendemo.provisionworker.models.Citizen;
import org.springframework.web.client.RestTemplate;

public class CitizenService {
    private static RestTemplate restTemplate = new RestTemplate();
    public static Citizen GetCitizenById(String citizenId, String citizenServiceURL) {
        return restTemplate.getForObject(citizenServiceURL + "/" + citizenId, Citizen.class);
    }
}