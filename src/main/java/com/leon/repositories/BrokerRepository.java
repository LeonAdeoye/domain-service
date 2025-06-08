package com.leon.repositories;

import com.leon.models.Broker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface BrokerRepository extends MongoRepository<Broker, UUID> {}
