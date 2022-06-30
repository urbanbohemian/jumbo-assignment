package com.jumbo.assignment.exception;


import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal server error occured")
	public void handleServiceExceptions(Exception exception) {
		log.error(exception.getLocalizedMessage(), exception);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal argument")
	public void handleIllegalArgumentException(Exception exception) {
		log.error(exception.getLocalizedMessage(), exception);
	}

	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void handleConstraintViolationException(Exception exception) {
		log.error(exception.getLocalizedMessage(), exception);
	}

	@ExceptionHandler(DuplicateEntityException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Duplicate entity found.")
	public void handleDuplicateEntityException(Exception exception) {
		log.error(exception.getLocalizedMessage(), exception);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Could not find entity with id")
	public void handleEntityNotFoundException(Exception exception) {
		log.error(exception.getLocalizedMessage(), exception);
	}
}

