package de.workshops.bookdemo.book;

import lombok.Data;

@Data
public class Book {

    private String title;
    private String description;
    private String author;
    private String isbn;
}