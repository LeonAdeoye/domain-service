package com.leon.repositories;

import com.leon.models.Desk;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface DeskRepository extends MongoRepository<Desk, UUID> {}