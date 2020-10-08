package com.wolfAPP.rest.webservices.restfulwebservices.pack;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PackNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Own customised exception that is thrown if pack object is not found in database
	public PackNotFoundException(String message) {
		super(message);
	}
}
