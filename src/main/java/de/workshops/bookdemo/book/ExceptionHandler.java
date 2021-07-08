package de.workshops.bookdemo.book;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> exceptionHandler(NoSuchElementException ex) {
    	return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Fehler 🤯");
    }
    	
}
