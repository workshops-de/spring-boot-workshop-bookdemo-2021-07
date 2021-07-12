package de.workshops.bookdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import de.workshops.bookdemo.config.ApplicationConfig;

@SpringBootTest
@ActiveProfiles("test")
public class AppConfigTest {

	@Autowired
	ApplicationConfig config;
	
	@Test
	void testWiring() {
		assertEquals(23, config.getParam1());
		assertEquals("value2", config.getParam2());
	}
	
}
