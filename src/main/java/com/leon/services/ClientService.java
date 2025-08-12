package com.leon.services;

import com.leon.models.Client;

import java.util.List;

public interface ClientService
{
    void reconfigure();

    void delete(String clientId);

    List<Client> getAll();

    Client save(Client clientToSave);

    Client update(Client clientToUpdate);
}
