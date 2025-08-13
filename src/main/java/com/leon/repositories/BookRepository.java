package com.leon.repositories;

import com.leon.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String>
{
    List<Book> findByBookCode(String bookCode);
    List<Book> findByBookName(String bookName);
    List<Book> findByDeskId(String deskId);

    Optional<Book> findByBookId(String bookId);
    boolean existsByBookCode(String bookCode);
    boolean existsByBookId(String bookId);
}
