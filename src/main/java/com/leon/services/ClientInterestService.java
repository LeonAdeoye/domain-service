package com.leon.services;

import com.leon.models.ClientInterest;

import java.util.List;

public interface ClientInterestService
{
    void reconfigure();

    List<ClientInterest> getAll();

    List<ClientInterest> getAllByClientId(String clientId);

    void delete(String interestId);

    ClientInterest save(ClientInterest clientInterestToSave);

    ClientInterest update(ClientInterest clientInterestToUpdate);
}
