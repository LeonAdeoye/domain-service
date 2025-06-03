package com.leon.repositories;

import com.leon.models.Trader;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TraderRepository extends MongoRepository<Trader, UUID> {}
