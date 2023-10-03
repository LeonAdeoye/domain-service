package com.leon.services;

import com.leon.models.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessagingServiceImpl implements MessagingService
{
    private RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(MessagingServiceImpl.class);
    private static final String configurationServiceURL = "http://localhost:20001";

    public void saveConfiguration(Configuration configuration)
    {
        this.restTemplate.put(configurationServiceURL + "/configuration", configuration);
    }

    public List<Configuration> loadAllConfigurations()
    {
        try
        {
            logger.info("Loading all configurations...");

            ResponseEntity<List<Configuration>> response = this.restTemplate.exchange(
                    configurationServiceURL + "/configurations",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Configuration>>(){});

            List<Configuration> configurations = response.getBody();

            logger.info("Loaded configurations:" + configurations);

            return configurations;
        }
        catch(RestClientException rce)
        {
            logger.error(rce.getMessage());
        }

        return new ArrayList<>();
    }
}