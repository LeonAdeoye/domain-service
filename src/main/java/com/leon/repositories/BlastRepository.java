package com.leon.repositories;

import com.leon.models.Blast;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface BlastRepository extends MongoRepository<Blast, UUID> {}