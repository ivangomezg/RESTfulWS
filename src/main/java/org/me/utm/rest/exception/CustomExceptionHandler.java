package org.me.utm.rest.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.me.utm.model.Error;
import org.me.utm.model.ErrorInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class CustomExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error handleNotFound(HttpServletRequest req, HttpServletResponse res, ResourceNotFoundException e) {
		Error errors = new Error();
		errors.addError(new ErrorInfo(req.getRequestURL().toString(), HttpStatus.NOT_FOUND.toString(), e));
		return errors;
	}

	@ExceptionHandler(MethodNotAllowedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public Error handleNotAllowed(HttpServletRequest req, HttpServletResponse res, MethodNotAllowedException e) {
		Error errors = new Error();
		errors.addError(new ErrorInfo(req.getRequestURL().toString(), HttpStatus.METHOD_NOT_ALLOWED.toString(), e));
		return errors;
	}

}
