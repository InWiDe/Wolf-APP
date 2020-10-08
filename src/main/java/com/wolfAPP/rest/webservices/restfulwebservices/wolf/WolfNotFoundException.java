package com.wolfAPP.rest.webservices.restfulwebservices.wolf;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WolfNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	// Own customised exception that is thrown if wolf object is not found in database
	public WolfNotFoundException(String message) {
		super(message);
	}

}
