package com.leon.repositories;

import com.leon.models.ClientInterest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientInterestRepository extends MongoRepository<ClientInterest, String> {}