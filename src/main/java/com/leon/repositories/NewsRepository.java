package com.leon.repositories;

import com.leon.models.News;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface NewsRepository  extends MongoRepository<News, UUID> {}
