package com.leon.services;

import com.leon.models.ClientInterest;
import com.leon.repositories.ClientInterestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientInterestServiceImpl implements ClientInterestService
{
    private static final Logger logger = LoggerFactory.getLogger(ClientInterestServiceImpl.class);
    private Map<String, List<ClientInterest>> clientInterestMap = new HashMap<>();
    @Autowired
    private ClientInterestRepository clientInterestRepository;

    @PostConstruct
    public void initialize()
    {
        List<ClientInterest> result = clientInterestRepository.findAll();
        result.forEach(clientInterest ->
        {
            if(!clientInterestMap.containsKey(clientInterest.getOwnerId()))
                clientInterestMap.put(clientInterest.getOwnerId(), new ArrayList<>());
            List<ClientInterest> interests = clientInterestMap.get(clientInterest.getOwnerId());
            interests.add(clientInterest);
        });
        logger.info("Loaded client interest service with {} client interest(s).", result.size());
    }

    @Override
    public void reconfigure()
    {
        clientInterestMap.clear();
        initialize();
    }

    @Override
    public List<ClientInterest> getAll(String ownerId)
    {
        return clientInterestMap.get(ownerId);
    }

    @Override
    public List<ClientInterest> getAllByClientId(String ownerId, String clientId)
    {
        List<ClientInterest> interests = clientInterestMap.get(ownerId);
        return interests.stream().filter(clientInterest -> clientInterest.getClientId().equals(clientId)).collect(Collectors.toList());
    }

    @Override
    public void delete(String ownerId, String clientInterestId)
    {
        clientInterestRepository.deleteById(clientInterestId);
        List<ClientInterest> interests = clientInterestMap.get(ownerId);
        interests.removeIf(clientInterest -> clientInterest.getClientInterestId().equals(clientInterestId));
    }

    @Override
    public ClientInterest save(ClientInterest clientInterestToSave)
    {
        ClientInterest result = clientInterestRepository.save(clientInterestToSave);
        if(!clientInterestMap.containsKey(clientInterestToSave.getOwnerId()))
            clientInterestMap.put(result.getOwnerId(), new ArrayList<>());
        List<ClientInterest> interests = clientInterestMap.get(result.getOwnerId());
        interests.add(result);
        return result;
    }

    @Override
    public ClientInterest update(ClientInterest clientInterestToUpdate)
    {
        ClientInterest result = clientInterestRepository.save(clientInterestToUpdate);
        List<ClientInterest> interests = clientInterestMap.get(clientInterestToUpdate.getOwnerId());
        interests.removeIf(clientInterest -> clientInterest.getClientInterestId().equals(result.getClientInterestId()));
        interests.add(result);
        return result;
    }
}
