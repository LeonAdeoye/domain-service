package com.leon.services;

import com.leon.models.Book;
import com.leon.repositories.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest
{
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private List<Book> mockBooks;
    private Book mockBook1;
    private Book mockBook2;

    @Before
    public void setUp()
    {
        mockBooks = new ArrayList<>();
        mockBook1 = new Book();
        mockBook1.setId(UUID.randomUUID().toString());
        mockBook1.setBookId("BK001");
        mockBook1.setBookCode("CODE1");
        mockBook1.setBookName("Test Book 1");
        mockBook1.setDeskId("DESK1");

        mockBook2 = new Book();
        mockBook2.setId(UUID.randomUUID().toString());
        mockBook2.setBookId("BK002");
        mockBook2.setBookCode("CODE2");
        mockBook2.setBookName("Test Book 2");
        mockBook2.setDeskId("DESK2");

        mockBooks.add(mockBook1);
        mockBooks.add(mockBook2);

        when(bookRepository.findAll()).thenReturn(mockBooks);
        bookService.initialize();
    }

    @Test
    public void reconfigure_shouldClearAndReloadBooks()
    {
        // Arrange
        List<Book> newBooks = new ArrayList<>();
        Book newBook = new Book();
        newBook.setId(UUID.randomUUID().toString());
        newBook.setBookId("BK003");
        newBook.setBookCode("CODE3");
        newBook.setBookName("New Book");
        newBook.setDeskId("DESK3");
        newBooks.add(newBook);
        when(bookRepository.findAll()).thenReturn(newBooks);

        // Act
        bookService.reconfigure();

        // Assert
        List<Book> result = bookService.getAll();
        assertEquals(1, result.size());
        assertEquals("BK003", result.get(0).getBookId());
        assertEquals("CODE3", result.get(0).getBookCode());
        assertEquals("New Book", result.get(0).getBookName());
        assertEquals("DESK3", result.get(0).getDeskId());
        verify(bookRepository, times(2)).findAll();
    }

    @Test
    public void getAll_shouldReturnAllBooks()
    {
        // Act
        List<Book> result = bookService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("BK001", result.get(0).getBookId());
        assertEquals("BK002", result.get(1).getBookId());
    }

    @Test
    public void getBooks_shouldReturnAllBooks()
    {
        // Act
        List<Book> result = bookService.getBooks();

        // Assert
        assertEquals(2, result.size());
        assertEquals("BK001", result.get(0).getBookId());
        assertEquals("BK002", result.get(1).getBookId());
    }

    @Test
    public void getBookById_shouldReturnBook_whenBookExists()
    {
        // Act
        Book result = bookService.getBookById("BK001");

        // Assert
        assertNotNull(result);
        assertEquals("BK001", result.getBookId());
        assertEquals("CODE1", result.getBookCode());
        assertEquals("Test Book 1", result.getBookName());
        assertEquals("DESK1", result.getDeskId());
    }

    @Test
    public void getBookById_shouldReturnNull_whenBookDoesNotExist()
    {
        // Act
        Book result = bookService.getBookById("NONEXISTENT");

        // Assert
        assertNull(result);
    }

    @Test
    public void addNewBook_shouldSaveAndReturnBook_whenValidBook()
    {
        // Arrange
        Book newBook = new Book("CODE3", "New Book", "DESK3");
        Book savedBook = new Book();
        savedBook.setId(UUID.randomUUID().toString());
        savedBook.setBookId("BK003");
        savedBook.setBookCode("CODE3");
        savedBook.setBookName("New Book");
        savedBook.setDeskId("DESK3");
        
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Act
        Book result = bookService.addNewBook(newBook);

        // Assert
        assertNotNull(result);
        assertEquals("BK003", result.getBookId());
        assertEquals("CODE3", result.getBookCode());
        assertEquals("New Book", result.getBookName());
        assertEquals("DESK3", result.getDeskId());
        verify(bookRepository).save(any(Book.class));
        
        // Verify it was added to in-memory list
        List<Book> allBooks = bookService.getAll();
        assertEquals(3, allBooks.size());
        assertTrue(allBooks.stream().anyMatch(b -> b.getBookId().equals("BK003")));
    }

    @Test
    public void addNewBook_shouldGenerateBookId_whenBookIdIsNull()
    {
        // Arrange
        Book newBook = new Book("CODE3", "New Book", "DESK3");
        newBook.setBookId(null);
        Book savedBook = new Book();
        savedBook.setId(UUID.randomUUID().toString());
        savedBook.setBookId("BK003");
        savedBook.setBookCode("CODE3");
        savedBook.setBookName("New Book");
        savedBook.setDeskId("DESK3");
        
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Act
        Book result = bookService.addNewBook(newBook);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getBookId());
        verify(bookRepository).save(any(Book.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNewBook_shouldThrowException_whenInvalidBook()
    {
        // Arrange
        Book invalidBook = new Book();
        invalidBook.setBookCode(""); // Empty bookCode
        invalidBook.setBookName("Test Book");
        invalidBook.setDeskId("DESK1");

        // Act & Assert
        bookService.addNewBook(invalidBook);
    }

    @Test
    public void updateBook_shouldUpdateAndReturnBook_whenBookExists()
    {
        // Arrange
        Book bookToUpdate = new Book();
        bookToUpdate.setBookId("BK001");
        bookToUpdate.setBookCode("CODE1_UPDATED");
        bookToUpdate.setBookName("Updated Book 1");
        bookToUpdate.setDeskId("DESK1");
        
        when(bookRepository.findByBookId("BK001")).thenReturn(Optional.of(mockBook1));
        when(bookRepository.save(any(Book.class))).thenReturn(bookToUpdate);

        // Act
        Book result = bookService.updateBook(bookToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals("BK001", result.getBookId());
        assertEquals("CODE1_UPDATED", result.getBookCode());
        assertEquals("Updated Book 1", result.getBookName());
        verify(bookRepository).findByBookId("BK001");
        verify(bookRepository).save(any(Book.class));
        
        // Verify in-memory list was updated
        Book updatedInMemory = bookService.getBookById("BK001");
        assertEquals("CODE1_UPDATED", updatedInMemory.getBookCode());
        assertEquals("Updated Book 1", updatedInMemory.getBookName());
    }

    @Test
    public void updateBook_shouldReturnNull_whenBookDoesNotExist()
    {
        // Arrange
        Book bookToUpdate = new Book();
        bookToUpdate.setBookId("NONEXISTENT");
        bookToUpdate.setBookCode("CODE1");
        bookToUpdate.setBookName("Test Book");
        bookToUpdate.setDeskId("DESK1");
        
        when(bookRepository.findByBookId("NONEXISTENT")).thenReturn(Optional.empty());

        // Act
        Book result = bookService.updateBook(bookToUpdate);

        // Assert
        assertNull(result);
        verify(bookRepository).findByBookId("NONEXISTENT");
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateBook_shouldThrowException_whenInvalidBook()
    {
        // Arrange
        Book invalidBook = new Book();
        invalidBook.setBookId("BK001");
        invalidBook.setBookCode(""); // Empty bookCode
        invalidBook.setBookName("Test Book");
        invalidBook.setDeskId("DESK1");

        // Act & Assert
        bookService.updateBook(invalidBook);
    }

    @Test
    public void deleteBook_shouldDeleteBook_whenBookExists()
    {
        // Arrange
        when(bookRepository.findByBookId("BK001")).thenReturn(Optional.of(mockBook1));

        // Act
        bookService.deleteBook("BK001");

        // Assert
        verify(bookRepository).findByBookId("BK001");
        verify(bookRepository).delete(mockBook1);
        
        // Verify it was removed from in-memory list
        List<Book> allBooks = bookService.getAll();
        assertEquals(1, allBooks.size());
        assertFalse(allBooks.stream().anyMatch(b -> b.getBookId().equals("BK001")));
    }

    @Test
    public void deleteBook_shouldNotDelete_whenBookDoesNotExist()
    {
        // Arrange
        when(bookRepository.findByBookId("NONEXISTENT")).thenReturn(Optional.empty());

        // Act
        bookService.deleteBook("NONEXISTENT");

        // Assert
        verify(bookRepository).findByBookId("NONEXISTENT");
        verify(bookRepository, never()).delete(any(Book.class));
        
        // Verify in-memory list unchanged
        List<Book> allBooks = bookService.getAll();
        assertEquals(2, allBooks.size());
    }

    @Test
    public void clear_shouldClearAllBooks()
    {
        // Act
        bookService.clear();

        // Assert
        List<Book> result = bookService.getAll();
        assertEquals(0, result.size());
    }
}
