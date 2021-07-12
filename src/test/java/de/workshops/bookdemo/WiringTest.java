package de.workshops.bookdemo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import de.workshops.bookdemo.book.BookRestController;

@SpringBootTest
public class WiringTest {

	@Autowired
	BookRestController controller;
	
	@Test
	void testWiring() {
		assertNotNull(controller);
		assertNotNull(ReflectionTestUtils.getField(controller, "bookService"));
	}
	
}
