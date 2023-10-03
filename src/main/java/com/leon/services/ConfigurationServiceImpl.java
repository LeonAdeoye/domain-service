package com.leon.services;
import com.leon.models.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConfigurationServiceImpl implements ConfigurationService
{
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationServiceImpl.class);
    private static Map<String, Map<String,List<Configuration>>> configurations;
    private static final String THIS_COMPONENT_NAME = "domain-service";
    @Autowired
    private MessagingService messagingService;

    public ConfigurationServiceImpl()
    {
    }

    @PostConstruct
    public void initialize()
    {
        loadAllConfigurations();
    }

    @Override
    public String getConfigurationValue(String owner, String key)
    {
        if(!configurations.containsKey(key) || !configurations.get(key).containsKey(owner) || configurations.get(key).get(owner).size() == 0)
            return "";

        return this.configurations.get(key).get(owner).get(0).getValue();
    }

    public void loadAllConfigurations()
    {
        List<Configuration> loadedConfigurations = this.messagingService.loadAllConfigurations();
        configurations = loadedConfigurations.stream().collect(Collectors.groupingBy(Configuration::getKey, Collectors.groupingBy(Configuration::getOwner)));
        logger.info("Retrieved configurations: " + configurations);
    }

    @Override
    public void reconfigure()
    {
        this.loadAllConfigurations();
    }
}