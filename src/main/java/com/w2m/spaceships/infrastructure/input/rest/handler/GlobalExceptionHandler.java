package com.w2m.spaceships.infrastructure.input.rest.handler;

import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.BadRequestSpaceshipException;
import com.w2m.spaceships.infrastructure.input.rest.handler.exceptions.NotFoundSpaceshipException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundSpaceshipException.class)
  public ResponseEntity<Object> handleNotFoundSpaceshipException(
      final NotFoundSpaceshipException ex, final WebRequest webRequest) {

    return handleExceptionInternal(
        ex,
        new GlobalHandlerErrorResponse(
            ErrorConstants.NOT_FOUND_ERROR_CODE,
            "ERROR",
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ErrorConstants.NOT_FOUND_ERROR_DESC),
        new HttpHeaders(),
        HttpStatus.NOT_FOUND,
        webRequest);
  }

  @ExceptionHandler(BadRequestSpaceshipException.class)
  public ResponseEntity<Object> handleBadRequestSpaceshipException(
      final BadRequestSpaceshipException ex, final WebRequest webRequest) {

    return handleExceptionInternal(
        ex,
        new GlobalHandlerErrorResponse(
            ErrorConstants.BAD_REQUEST_ERROR_CODE,
            "ERROR",
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ErrorConstants.BAD_REQUEST_ERROR_DESC),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        webRequest);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGeneralException(
      final Exception ex, final WebRequest webRequest) {
    return handleExceptionInternal(
        ex,
        new GlobalHandlerErrorResponse(
            ErrorConstants.INTERNAL_SERVER_ERROR_CODE,
            "ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            ErrorConstants.INTERNAL_SERVER_ERROR_DESC),
        new HttpHeaders(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        webRequest);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(
      final AccessDeniedException ex, final WebRequest webRequest) {
    return handleExceptionInternal(
        ex,
        new GlobalHandlerErrorResponse(
            ErrorConstants.FORBIDDEN_ERROR_CODE,
            "ERROR",
            HttpStatus.FORBIDDEN.getReasonPhrase(),
            ErrorConstants.FORBIDDEN_ERROR_DESC),
        new HttpHeaders(),
        HttpStatus.FORBIDDEN,
        webRequest);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Object> handleAuthenticationException(
      final AuthenticationException ex, final WebRequest webRequest) {
    return handleExceptionInternal(
        ex,
        new GlobalHandlerErrorResponse(
            ErrorConstants.UNAUTHORIZED_ERROR_CODE,
            "ERROR",
            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
            ErrorConstants.UNAUTHORIZED_ERROR_DESC),
        new HttpHeaders(),
        HttpStatus.UNAUTHORIZED,
        webRequest);
  }
}
