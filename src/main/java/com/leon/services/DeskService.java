package com.leon.services;

import com.leon.models.Desk;
import com.leon.models.Trader;
import java.util.List;

public interface DeskService
{
     List<Desk> getAllDesks();
     Desk getDeskById(String deskId);
     Desk createDesk(Desk desk);
     Desk updateDesk(Desk desk);
     void deleteDesk(String deskId);
     void addTraderToDesk(String deskId, String traderId);
     void removeTraderFromDesk(String deskId, String traderId);
     List<Trader> getTradersByDeskId(String deskId);
     void reconfigure();
     Desk getDesk(String traderId);
     boolean doesTraderBelongToDesk(String deskId, String traderId);
}
