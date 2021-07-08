package de.workshops.bookdemo.book;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(BookRestController.REQUEST_URL)
@Slf4j
public class BookRestController {

	public static final String REQUEST_URL = "/book";
	
	private List<Book> books;
	
	@Autowired
	private ObjectMapper mapper;
	    
	@PostConstruct
	public void init() throws Exception {
	    this.books = Arrays.asList(mapper.readValue(new File("target/classes/books.json"), Book[].class));
	    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}
	
	@GetMapping
	public List<Book> getAllBooks() {
		return this.books;
	}

	@GetMapping("/{isbn}")
	public Book getBookByIsbn(@PathVariable String isbn, HttpServletRequest request) {
		return this.books.stream().filter(book -> hasIsbn(book, isbn)).findFirst().orElseThrow(() ->  new NoSuchElementException("No book present"));
	}
	
	@GetMapping(params = {"author"})
	public ResponseEntity<Book> getAllBooks(@RequestParam String author) {
		
		Book bookResult = this.books.stream().filter(book -> hasAuthor(book, author)).findFirst().orElseThrow();
		return ResponseEntity.status(201).body(bookResult);
	}
	
	@PostMapping("/search")
	public List<Book> searchBooks(@RequestBody BookSearchRequest searchRequest) {
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
    
    @ExceptionHandler(IllegalAccessError.class)
    public ResponseEntity<String> exceptionHandler(IllegalAccessError ex) {
    	return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Fehler 🤯");
    }
	
}
