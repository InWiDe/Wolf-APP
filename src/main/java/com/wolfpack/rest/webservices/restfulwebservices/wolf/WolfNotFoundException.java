package com.wolfpack.rest.webservices.restfulwebservices.wolf;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WolfNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WolfNotFoundException(String message) {
		super(message);
	}

}
