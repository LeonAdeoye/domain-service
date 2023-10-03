package com.leon.services;

import com.leon.models.ClientInterest;

import java.util.List;

public interface ClientInterestService
{
    void reconfigure();

    List<ClientInterest> getAll(String ownerId);

    List<ClientInterest> getAllByClientId(String ownerId, String clientId);

    void delete(String ownerId, String clientInterestId);

    ClientInterest save(ClientInterest clientInterestToSave);

    ClientInterest update(ClientInterest clientInterestToUpdate);
}
