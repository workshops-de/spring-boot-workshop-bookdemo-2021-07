package de.workshops.bookdemo.book;

import java.util.List;
import java.util.NoSuchElementException;

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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(BookRestController.REQUEST_URL)
@Slf4j
public class BookRestController {

	public static final String REQUEST_URL = "/book";
	
	@Autowired
	private BookService bookService;
	
	    
	@GetMapping
	public List<Book> getAllBooks() {
		return bookService.loadAllBooks();
	}

	@GetMapping("/{isbn}")
	public Book getBookByIsbn(@PathVariable String isbn) {
		return this.bookService.loadBookByIsbn(isbn);
	}
	
	@GetMapping(params = {"author"})
	public Book getAllBooks(@RequestParam String author) {
		return this.bookService.loadBookByAuthor(author);
	}
	
	@PostMapping("/search")
	public List<Book> searchBooks(@RequestBody BookSearchRequest searchRequest) {
		return this.bookService.search(searchRequest);
	}
	
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> exceptionHandler(IllegalAccessError ex) {
    	return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Fehler 🤯");
    }
	
}
