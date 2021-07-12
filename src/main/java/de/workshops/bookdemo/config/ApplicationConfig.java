package de.workshops.bookdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "myapp")
@Data
public class ApplicationConfig {

	private int param1;
	private String param2;
}
