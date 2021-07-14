package de.workshops.bookdemo;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.workshops.bookdemo.book.Book;
import de.workshops.bookdemo.book.BookRestController;
import de.workshops.bookdemo.book.BookService;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class BookRestControllerTest {

	@Autowired
	BookRestController controller;
	
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
    private WebApplicationContext context;
	
	
	@BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }
	
	
	@Test
	@WithMockUser(username = "userA")
	void testDirectMethodCall() {
		assertNotNull(controller);
		assertEquals(3, controller.getAllBooks().size());
	}
	
	@Test
	@WithMockUser(username = "userA")
	void testMockkMvc() throws Exception {
		mockMvc.perform(get(BookRestController.REQUEST_URL))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	@WithMockUser(username = "userA")
	void testMockkMvcResult() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(BookRestController.REQUEST_URL))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
		String jsonPayload = mvcResult.getResponse().getContentAsString();
		Book[] books = objectMapper.readValue(jsonPayload, Book[].class);
		assertEquals(3, books.length);
		assertEquals("Clean Code", books[1].getTitle());
	}
	
	@Test
	@WithMockUser(username = "userA")
	void testRestAssuredMockMvc() {
		RestAssuredMockMvc.standaloneSetup(controller);
		given()
			.mockMvc(mockMvc)
			.log().all()
		.when()
			.get(BookRestController.REQUEST_URL)
		.then()
			.log().all()
			.statusCode(200)
			.body("author[0]",equalTo("Erich Gamma"));
	}

	@Test
	void testRestAssuredRealHttp() {
		RestAssured.given()
			.log().all()
			.auth().basic("alice", "password")
		.when()
			.get(BookRestController.REQUEST_URL)
		.then()
			.log().all()
			.statusCode(200)
			.body("author[0]",equalTo("Erich Gamma"));
	}
	
	@Test
	void testWithMockito() {
		Book book = Book.builder()
				.author("Dominik Hirt")
				.title("Titel")
				.isbn("1234")
				.build();
		List<Book> booklist = Collections.singletonList(book); 
		BookService mock = Mockito.mock(BookService.class);
		Mockito.when(mock.loadAllBooks()).thenReturn(booklist);
		ReflectionTestUtils.setField(controller, "bookService", mock);
		
		assertEquals(1, controller.getAllBooks().size());
	}

	
}
