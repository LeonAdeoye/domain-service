package com.leon.services;

public interface ConfigurationService
{
    String getConfigurationValue(String owner, String key);
    void loadAllConfigurations();
    void reconfigure();
}