package de.workshops.bookdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.workshops.bookdemo.book.Book;
import de.workshops.bookdemo.book.BookJpaRepository;
import de.workshops.bookdemo.book.BookRepository;

@SpringBootTest
class BookJpaRepositoryTest {

	@Autowired
	private BookJpaRepository repository;
	
	@Autowired
	private BookRepository bookRepository;
	
	
	@Test
	void testSave() {
		 Book book = Book.builder()
            .title("Title")
            .author("Author")
            .description("Description")
            .isbn("123-4567890")
            .build();
		 assertNull(book.getId());
		 Book result = repository.save(book);
		 assertNotNull(result.getId());
	}

	@Test
	void testFindAll() {
		List<Book> result = new ArrayList<>();
	    repository.findAll().forEach(result::add);

		assertEquals(3, result.size());
	}

	@Test
	void testFindByIsbn() {
		assertNotNull(repository.findByIsbn("978-0201633610"));
		assertNotNull(repository.findByIsbnNative("978-0201633610"));
	}

	@Test
	void testNamedParamTemplate() {
		assertNotNull(bookRepository.findByIsbn("978-0201633610"));
	}

}
