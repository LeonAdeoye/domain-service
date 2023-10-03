package com.leon.services;

import com.leon.models.Configuration;
import java.util.List;

public interface MessagingService
{
    List<Configuration> loadAllConfigurations();
}