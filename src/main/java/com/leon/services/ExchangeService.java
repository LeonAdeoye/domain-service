package com.leon.services;

import com.leon.models.Exchange;
import java.util.List;

public interface ExchangeService
{
    List<Exchange> getAll();
    void reconfigure();
}

