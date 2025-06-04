package com.leon.repositories;

import com.leon.models.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AccountRepository extends MongoRepository<Account, UUID> {}