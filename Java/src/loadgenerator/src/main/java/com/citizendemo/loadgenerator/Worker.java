package com.citizendemo.loadgenerator;

import com.citizendemo.loadgenerator.models.*;
import com.citizendemo.loadgenerator.services.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Component
public class Worker {

    private SampleData sampleData = new SampleData();
    @Value("${citizenAPI.url}")
    private String citizenServiceURL;
    private Logger logger = LoggerFactory.getLogger(Worker.class);

    public Worker() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<String>> stringListTypeReference = new TypeReference<List<String>>() {
        };
        TypeReference<List<CityData>> cityListTypeReference = new TypeReference<List<CityData>>() {
        };

        InputStream givenNamesInputStream = TypeReference.class.getResourceAsStream("/sampledata/givenNames.json");
        InputStream surnamesInputStream = TypeReference.class.getResourceAsStream("/sampledata/surnames.json");
        InputStream streetNamesInputStream = TypeReference.class.getResourceAsStream("/sampledata/streetNames.json");
        InputStream citiesInputStream = TypeReference.class.getResourceAsStream("/sampledata/cities.json");

        try {
            sampleData.GivenNames = mapper.readValue(givenNamesInputStream, stringListTypeReference);
            logger.info("Loaded " + sampleData.GivenNames.size() + " samples of given names.");
            sampleData.Surnames = mapper.readValue(surnamesInputStream, stringListTypeReference);
            logger.info("Loaded " + sampleData.Surnames.size() + " samples of surnames.");
            sampleData.StreetNames = mapper.readValue(streetNamesInputStream, stringListTypeReference);
            logger.info("Loaded " + sampleData.StreetNames.size() + " samples of street names.");
            sampleData.Cities = mapper.readValue(citiesInputStream, cityListTypeReference);
            logger.info("Loaded " + sampleData.Cities.size() + " samples of city data.");
        } catch (Exception e) {
            logger.warn("Loading sample data failed ", e);
        }
    }

    @Scheduled(fixedRate = 15000)
    public void createCitizens() {
        int noctbc = new Random().nextInt(10);
        int nocc = 0;
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) { noctbc = noctbc / 3; }
        for (int count = 0; count < 5*noctbc; count++) {
            try {
                CityData cityData = sampleData.Cities.get(new Random().nextInt(sampleData.Cities.size()));
                String streetName = sampleData.StreetNames.get(new Random().nextInt(sampleData.StreetNames.size()));

                Citizen citizen = new Citizen();
                citizen.citizenId = java.util.UUID.randomUUID().toString();
                citizen.givenName = sampleData.GivenNames.get(new Random().nextInt(sampleData.GivenNames.size()));
                citizen.surname = sampleData.Surnames.get(new Random().nextInt(sampleData.Surnames.size()));
                StringBuilder phoneNumber = new StringBuilder();
                phoneNumber.append("(");
                phoneNumber.append(cityData.AreaCode);
                phoneNumber.append(")");
                phoneNumber.append(" ");
                phoneNumber.append(new Random().nextInt(9));
                phoneNumber.append(new Random().nextInt(9));
                phoneNumber.append(new Random().nextInt(9));
                phoneNumber.append("-");
                phoneNumber.append(new Random().nextInt(9));
                phoneNumber.append(new Random().nextInt(9));
                phoneNumber.append(new Random().nextInt(9));
                phoneNumber.append(new Random().nextInt(9));
                citizen.phoneNumber = phoneNumber.toString();
                StringBuilder streetAddress = new StringBuilder();
                streetAddress.append(new Random().nextInt(9));
                streetAddress.append(new Random().nextInt(9));
                streetAddress.append(new Random().nextInt(9));
                streetAddress.append(" ");
                streetAddress.append(sampleData.StreetNames.get(new Random().nextInt(sampleData.StreetNames.size())));
                citizen.streetAddress = streetAddress.toString();
                citizen.city = cityData.City;
                citizen.state = cityData.State;
                citizen.postalCode = cityData.PostalCode;
                citizen.country = cityData.Country;

                CitizenService.CreateCitizen(citizen, citizenServiceURL);
                nocc++;
            } catch (Exception e) {
                logger.warn("Error while creating citizen " + e.getMessage());
            }
        }
        logger.info("Created " + nocc + " citizens.");
    }

    @Scheduled(fixedRate = 180000)
    public void deleteCitizens() {
        List<Citizen> citizens = null;
        try {
            citizens = CitizenService.GetCitizens(citizenServiceURL);
        } catch (Exception e) {
            logger.warn("Error while getting citizens to delete " + e.getMessage());
        }
        int noctbd = new Random().nextInt(10);
        int nocd = 0;
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) { noctbd = noctbd / 3; }

        for (int count = 0; count < 10*noctbd; count++) {
            try {
                CitizenService.DeleteCitizens(citizens.get(new Random().nextInt(citizens.size())), citizenServiceURL);
                nocd++;
            } catch (Exception e) {
                logger.warn("Error while deleting citizen " + e.getMessage());
            }
        }
        logger.info("Deleted " + nocd + " citizens.");
    }

    @Scheduled(fixedRate = 5000)
    public void searchCitizens() {
        int noctbs = new Random().nextInt(10);
        int nocs = 0;
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) { noctbs = noctbs / 3; }

        for (int count = 0; count < 10*noctbs; count++) {
            try {
                String givenName = sampleData.GivenNames.get(new Random().nextInt(sampleData.GivenNames.size()));
                CitizenService.SearchCitizens(givenName, "", "", "", "", citizenServiceURL);
                nocs++;
            } catch (Exception e) {
                logger.warn("Error while searching citizens with name " + e.getMessage());
            }
        }
        for (int count = 0; count < 10*noctbs; count++) {
            try {
                String surname = sampleData.Surnames.get(new Random().nextInt(sampleData.Surnames.size()));
                CitizenService.SearchCitizens(surname, "", "", "", "", citizenServiceURL);
                nocs++;
            } catch (Exception e) {
                logger.warn("Error while searching citizens with name " + e.getMessage());
            }
        }
        for (int count = 0; count < 10*noctbs; count++) {
            try {
                CityData cityData = sampleData.Cities.get(new Random().nextInt(sampleData.Cities.size()));
                CitizenService.SearchCitizens("", cityData.PostalCode, "", "", "", citizenServiceURL);
                nocs++;
            } catch (Exception e) {
                logger.warn("Error while searching citizens with postal code " + e.getMessage());
            }
        }
        for (int count = 0; count < 10*noctbs; count++) {
            try {
                CityData cityData = sampleData.Cities.get(new Random().nextInt(sampleData.Cities.size()));
                CitizenService.SearchCitizens("", "", cityData.City, "", "", citizenServiceURL);
                nocs++;
            } catch (Exception e) {
                logger.warn("Error while searching citizens with city " + e.getMessage());
            }
        }
        for (int count = 0; count < 10*noctbs; count++) {
            try {
                CityData cityData = sampleData.Cities.get(new Random().nextInt(sampleData.Cities.size()));
                CitizenService.SearchCitizens("", "", "", cityData.State, "", citizenServiceURL);
                nocs++;
            } catch (Exception e) {
                logger.warn("Error while searching citizens with state " + e.getMessage());
            }
        }
        logger.info("Searched " + nocs + " citizens.");
    }
}
