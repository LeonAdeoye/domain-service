package com.leon.services;

import com.leon.models.Exchange;
import java.util.List;

public interface ExchangeService
{
    List<Exchange> getAll();
    void reconfigure();

    Exchange createExchange(Exchange exchange);

    void deleteExchange(String exchangeId);
}

