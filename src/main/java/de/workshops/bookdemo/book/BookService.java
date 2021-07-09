package de.workshops.bookdemo.book;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private ObjectMapper mapper;
	
	
	public List<Book> loadAllBooks() {
		return this.bookRepository.findAllBooks();
	}

	public Book loadBookByIsbn(String isbn) {
		return this.bookRepository.findByIsbn(isbn);
	}

	public Book loadBookByAuthor(String author) {
		return this.bookRepository.findByAuthor(author);
	}

	public List<Book> search(BookSearchRequest searchRequest) {
		return this.bookRepository.search(searchRequest);
	}

}
