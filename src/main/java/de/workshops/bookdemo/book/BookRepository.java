package de.workshops.bookdemo.book;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class BookRepository {

	private List<Book> books;
	
	@Autowired
	private ObjectMapper mapper ;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	

	@PostConstruct
	public void init() throws Exception {
	    this.books = Arrays.asList(mapper.readValue(new File("target/classes/books.json"), Book[].class));
	}

	public List<Book> findAllBooks() {
		String sql = "SELECT * FROM book";      
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class));
	}
	
	public Book findByIsbn(String isbn) {
		return this.books.stream().filter(book -> hasIsbn(book, isbn)).findFirst().orElseThrow(() ->  new NoSuchElementException("No book present"));
	}

	public Book findByAuthor(String author) {
		return this.books.stream().filter(book -> hasAuthor(book, author)).findFirst().orElseThrow(() ->  new NoSuchElementException("No book present"));
	}

	public List<Book> search(BookSearchRequest searchRequest) {
		return this.books.stream().filter(book -> search(book, searchRequest)).collect(Collectors.toList());
	}
	
	
	
	private boolean search(Book book, BookSearchRequest searchRequest) {
		return hasIsbn(book, searchRequest.getIsbn()) || hasAuthor(book, searchRequest.getAuthor());
	}

	private boolean hasIsbn(Book book, String isbn) {
        return book.getIsbn().equals(isbn);
    }
    
    private boolean hasAuthor(Book book, String author) {
        return book.getAuthor().contains(author);
    }
    
    public void deleteAll() {
    	//
    }

    


	
}
