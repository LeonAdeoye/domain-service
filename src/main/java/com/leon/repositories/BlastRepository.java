package com.leon.repositories;

import com.leon.models.Blast;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlastRepository extends MongoRepository<Blast, String> {}