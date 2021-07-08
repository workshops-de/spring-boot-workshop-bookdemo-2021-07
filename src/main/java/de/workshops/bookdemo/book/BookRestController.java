package de.workshops.bookdemo.book;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
@RequestMapping(BookRestController.REQUEST_URL)
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
	
}
