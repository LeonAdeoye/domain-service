package com.leon.services;

import com.leon.models.Book;
import com.leon.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService
{
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    
    @Autowired
    private BookRepository bookRepository;
    
    private List<Book> books = new ArrayList<>();

    @PostConstruct
    public void initialize()
    {
        List<Book> result = bookRepository.findAll();
        books.addAll(result);
        logger.info("Loaded book service with {} book(s).", result.size());
    }

    @Override
    public void reconfigure()
    {
        books.clear();
        initialize();
    }

    @Override
    public List<Book> getAll()
    {
        return books;
    }

    @Override
    public List<Book> getBooks()
    {
        return books;
    }

    @Override
    public Book getBookById(String bookId)
    {
        return books.stream()
                .filter(book -> book.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Book addNewBook(Book book)
    {
        if (!book.isValid())
        {
            throw new IllegalArgumentException("Book must have valid bookCode, bookName, and deskId");
        }
        
        if (book.getBookId() == null)
        {
            book.setBookId(UUID.randomUUID().toString());
        }
        
        Book savedBook = bookRepository.save(book);
        books.add(savedBook);
        logger.info("Successfully saved book: {}.", savedBook);
        return savedBook;
    }

    @Override
    public Book updateBook(Book book)
    {
        if (!book.isValid())
        {
            throw new IllegalArgumentException("Book must have valid bookCode, bookName, and deskId");
        }
        
        Book existingBook = bookRepository.findByBookId(book.getBookId()).orElse(null);
        if (existingBook == null)
        {
            logger.warn("Book with bookId {} does not exist. Cannot update.", book.getBookId());
            return null;
        }
        
        Book updatedBook = bookRepository.save(book);
        books.removeIf(b -> b.getBookId().equals(existingBook.getBookId()));
        books.add(updatedBook);
        logger.info("Updated book: {}.", updatedBook);
        return updatedBook;
    }

    @Override
    public void deleteBook(String bookId)
    {
        Book bookToDelete = bookRepository.findByBookId(bookId).orElse(null);
        if (bookToDelete != null)
        {
            bookRepository.delete(bookToDelete);
            books.removeIf(book -> book.getBookId().equals(bookId));
            logger.info("Successfully deleted book with bookId: {}", bookId);
        }
        else
        {
            logger.warn("Attempted to delete non-existing book with bookId: {}", bookId);
        }
    }

    @Override
    public void clear()
    {
        books.clear();
        logger.info("Cleared all books from memory cache.");
    }
}
