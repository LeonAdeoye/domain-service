package com.leon.services;

import com.leon.models.Trader;
import java.util.List;
import java.util.UUID;

public interface TraderService
{
     List<Trader> getAllTraders();
     Trader getTraderById(String traderId);
     Trader createTrader(Trader trader);
     Trader updateTrader(Trader trader);
     void deleteTrader(String traderId);
     boolean doesTraderExist(String traderId);
     void reconfigure();
}
