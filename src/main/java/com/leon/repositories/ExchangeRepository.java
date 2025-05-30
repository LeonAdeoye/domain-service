package com.leon.repositories;

import com.leon.models.Exchange;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends MongoRepository<Exchange, String> {}
