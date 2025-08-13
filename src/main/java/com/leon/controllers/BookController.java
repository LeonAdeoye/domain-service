package com.leon.controllers;

import com.leon.models.Book;
import com.leon.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController()
@RequestMapping("/book")
public class BookController
{
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    
    @Autowired
    private BookService bookService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure book service.");
        bookService.reconfigure();
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=GET, produces = "application/json")
    public ResponseEntity<List<Book>> getAll()
    {
        logger.info("Received request to get all books.");
        return new ResponseEntity<>(bookService.getAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/{bookId}", method=GET, produces = "application/json")
    public ResponseEntity<Book> getBookById(@PathVariable String bookId)
    {
        if (bookId == null || bookId.isEmpty())
        {
            logger.error("Received request to get book but book ID was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to get book with ID: {}", bookId);
        Book book = bookService.getBookById(bookId);
        if (book != null)
        {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        else
        {
            logger.warn("Book with ID {} not found.", bookId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @RequestMapping(method=POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Book> addNewBook(@RequestBody Book book)
    {
        if (book == null || !book.isValid())
        {
            logger.error("Attempted to create a book with invalid data: {}", book);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to create book: {}", book);
        try
        {
            Book createdBook = bookService.addNewBook(book);
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e)
        {
            logger.error("Validation error when creating book: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @RequestMapping(method=PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Book> updateBook(@RequestBody Book book)
    {
        if (book == null || !book.isValid())
        {
            logger.error("Attempted to update a book with invalid data: {}", book);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (book.getBookId() == null || book.getBookId().trim().isEmpty())
        {
            logger.error("Attempted to update a book with null or empty book ID.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to update book: {}", book);
        Book updatedBook = bookService.updateBook(book);
        if (updatedBook != null)
        {
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        }
        else
        {
            logger.warn("Book with ID {} not found. Cannot update.", book.getBookId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/{bookId}", method = DELETE)
    public ResponseEntity<Void> deleteBook(@PathVariable String bookId)
    {
        if (bookId == null || bookId.isEmpty())
        {
            logger.error("Received request to delete book but book ID was null or empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to delete book with ID: {}", bookId);
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(value="/clear", method=POST)
    public ResponseEntity<Void> clear()
    {
        logger.info("Received request to clear all books.");
        bookService.clear();
        return ResponseEntity.noContent().build();
    }
}
