package com.leon.services;

import com.leon.models.Book;
import java.util.List;

public interface BookService
{
    void reconfigure();
    List<Book> getAll();
    List<Book> getBooks();
    Book getBookById(String bookId);
    Book addNewBook(Book book);
    Book updateBook(Book book);
    void deleteBook(String bookId);
    void clear();
}
