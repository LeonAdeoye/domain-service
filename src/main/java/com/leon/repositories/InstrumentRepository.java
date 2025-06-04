package com.leon.repositories;

import com.leon.models.Instrument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface InstrumentRepository extends MongoRepository<Instrument, UUID> {}