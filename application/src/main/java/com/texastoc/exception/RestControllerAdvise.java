package com.texastoc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@ControllerAdvice
public class RestControllerAdvise extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    ErrorDetails errorDetails = new ErrorDetails(
      new Date(), "Validation Failed",
      ex.getBindingResult().toString());
    return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {AccessDeniedException.class})
  protected void handleAccessDenied(IncorrectResultSizeDataAccessException ex, HttpServletResponse response) throws IOException {
    log.info(ex.getMessage(), ex);
    response.sendError(HttpStatus.UNAUTHORIZED.value());
  }

  @ExceptionHandler(value = {IncorrectResultSizeDataAccessException.class})
  protected void handleConflict(IncorrectResultSizeDataAccessException ex, HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.NOT_FOUND.value());
  }

  @ExceptionHandler(value = {NotFoundException.class})
  protected void handleNotFoundException(NotFoundException ex, HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }

  @ExceptionHandler(value = {FinalizedException.class})
  protected void handleFinalizedException(FinalizedException ex, HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.CONFLICT.value(), ex.getMessage());
  }

  @ExceptionHandler(value = {RuntimeException.class})
  protected void handleRuntimeException(RuntimeException ex, HttpServletResponse response) throws IOException {
    log.error(ex.getMessage(), ex);
    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

}
