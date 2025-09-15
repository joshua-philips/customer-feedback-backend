package org.mesika.customerfeedback.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.util.NoSuchElementException;

import javax.security.auth.login.AccountNotFoundException;

import org.mesika.customerfeedback.dto.DefaultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
        private final Logger log = LoggerFactory.getLogger(getClass());

        @ExceptionHandler(NoSuchElementException.class)
        public ResponseEntity<Object> handleNoSuchElementException(HttpServletRequest request,
                        NoSuchElementException exception) {
                log.info(exception.getClass() + ": " + exception.getMessage());
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value()),
                                HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<Object> handleEntityNotFoundException(HttpServletRequest request,
                        EntityNotFoundException exception) {
                log.info(exception.getClass() + ": " + exception.getMessage());
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value()),
                                HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<Object> handleAuthenticationException(HttpServletRequest request,
                        AuthenticationException exception) {
                log.info(exception.getClass() + ": " + exception.getMessage());
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.FORBIDDEN.value()),
                                HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
        public ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(HttpServletRequest request,
                        SQLIntegrityConstraintViolationException exception) {
                log.info(exception.getClass() + ": " + exception.getMessage());
                return new ResponseEntity<Object>(
                                new DefaultDTO("There was a problem with your request",
                                                HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(AuthException.class)
        public ResponseEntity<Object> handleAuthException(HttpServletRequest request,
                        AuthException exception) {
                log.info(exception.getClass() + ": " + exception.getMessage());
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpClientErrorException.class)
        public ResponseEntity<Object> handleHttpClientErrorException(HttpServletRequest request,
                        HttpClientErrorException exception) {
                log.error(exception.getClass() + ": " + exception.getMessage());
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getResponseBodyAsString(),
                                                HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ParseException.class)
        public ResponseEntity<Object> handleParseException(HttpServletRequest request,
                        ParseException exception) {
                log.error(exception.getClass() + ": " + exception.getMessage());
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(),
                                                HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<Object> handleDataIntegrityViolationException(HttpServletRequest request,
                        AccountNotFoundException exception) {
                log.info(exception.getClass() + ": " + exception.getMessage());
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(),
                                                HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

}