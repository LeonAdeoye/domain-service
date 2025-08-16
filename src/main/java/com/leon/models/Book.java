package com.leon.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

@Document(collection = "Book")
public class Book
{
    @Id
    private String bookId;
    private String bookCode;
    private String bookName;
    private String deskId;

    public Book()
    {
    }

    public Book(String bookCode, String bookName, String deskId)
    {
        this.bookCode = bookCode;
        this.bookName = bookName;
        this.deskId = deskId;
    }

    public Book(String bookId, String bookCode, String bookName, String deskId)
    {
        this.bookId = bookId;
        this.bookCode = bookCode;
        this.bookName = bookName;
        this.deskId = deskId;
    }

    public String getBookId()
    {
        return bookId;
    }

    public void setBookId(String bookId)
    {
        this.bookId = bookId;
    }

    public String getBookCode()
    {
        return bookCode;
    }

    public void setBookCode(String bookCode)
    {
        this.bookCode = bookCode;
    }

    public String getBookName()
    {
        return bookName;
    }

    public void setBookName(String bookName)
    {
        this.bookName = bookName;
    }

    public String getDeskId()
    {
        return deskId;
    }

    public void setDeskId(String deskId)
    {
        this.deskId = deskId;
    }



    public boolean isValid()
    {
        return bookCode != null && !bookCode.trim().isEmpty() &&
               bookName != null && !bookName.trim().isEmpty() &&
               deskId != null && !deskId.trim().isEmpty();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(bookId);
    }

    @Override
    public String toString()
    {
        return "Book{ bookId='" + bookId + '\'' +
                ", bookCode='" + bookCode + '\'' +
                ", bookName='" + bookName + '\'' +
                ", deskId='" + deskId + '\'' +
                '}';
    }
}
