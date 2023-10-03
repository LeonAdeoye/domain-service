package com.leon.services;

import com.leon.models.ClientInterest;
import com.leon.repositories.ClientInterestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientInterestServiceImpl implements ClientInterestService
{
    private static final Logger logger = LoggerFactory.getLogger(ClientInterestServiceImpl.class);
    private Map<String, ClientInterest> clientInterestMap = new HashMap<>();
    @Autowired
    ClientInterestRepository clientInterestRepository;

    @PostConstruct
    public void initialize()
    {
        logger.info("Loading client interest service.");
        List<ClientInterest> result = clientInterestRepository.findAll();
        result.forEach(clientInterest -> clientInterestMap.put(clientInterest.getClientInterestId(), clientInterest));
    }

    @Override
    public void reconfigure()
    {
        clientInterestMap.clear();
        initialize();
    }

    @Override
    public List<ClientInterest> getAll()
    {
        return clientInterestMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<ClientInterest> getAllByClientId(String clientId)
    {
        return clientInterestMap.values().stream().filter(clientInterest -> clientInterest.getClientId().equals(clientId)).collect(Collectors.toList());
    }

    @Override
    public void delete(String clientInterestId)
    {
        clientInterestRepository.deleteById(clientInterestId);
        clientInterestMap.remove(clientInterestId);
    }

    @Override
    public ClientInterest save(ClientInterest clientInterestToSave)
    {
        ClientInterest result = clientInterestRepository.save(clientInterestToSave);
        clientInterestMap.put(result.getClientInterestId(), result);
        return result;
    }

    @Override
    public ClientInterest update(ClientInterest clientInterestToUpdate)
    {
        ClientInterest result = clientInterestRepository.save(clientInterestToUpdate);
        clientInterestMap.put(result.getClientInterestId(), result);
        return result;
    }
}
