package com.leon.services;

import com.leon.models.Client;
import com.leon.repositories.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService
{
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    private Map<UUID, Client> clientMap = new HashMap<>();
    @Autowired
    private ClientRepository clientRepository;

    @PostConstruct
    public void initialize()
    {
        List<Client> result = clientRepository.findAll();
        result.forEach(client -> clientMap.put(client.getClientId(), client));
        logger.info("Loaded client service with {} client(s).", result.size());
    }

    @Override
    public void reconfigure()
    {
       clientMap.clear();
       initialize();
    }

    @Override
    public void delete(String clientId)
    {
        clientRepository.deleteById(UUID.fromString(clientId));
        clientMap.remove(UUID.fromString(clientId));
    }

    @Override
    public List<Client> getAll()
    {
        return clientMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Client save(Client clientToSave)
    {
        Client result = clientRepository.save(clientToSave);
        clientMap.put(result.getClientId(), result);
        return result;
    }
}
