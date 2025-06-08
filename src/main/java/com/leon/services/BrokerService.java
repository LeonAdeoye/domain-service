package com.leon.services;
import com.leon.models.Broker;
import java.util.List;

public interface BrokerService {
    void reconfigure();
    List<Broker> getAll();
    void deleteBroker(String brokerId);
    Broker createBroker(Broker broker);
    Broker updateBroker(Broker broker);
}
