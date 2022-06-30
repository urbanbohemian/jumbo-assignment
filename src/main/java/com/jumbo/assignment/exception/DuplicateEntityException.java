package com.jumbo.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Duplicate entity found.")
public class DuplicateEntityException extends RuntimeException
{
	static final long serialVersionUID = -3387516993334229948L;

	public DuplicateEntityException(String message)
	{
		super(message);
	}

}
