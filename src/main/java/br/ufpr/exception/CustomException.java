package br.ufpr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomException extends ResponseStatusException {

	private static final long serialVersionUID = 1L;

	public CustomException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
