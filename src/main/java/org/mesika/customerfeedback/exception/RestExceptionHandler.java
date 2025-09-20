package org.mesika.customerfeedback.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.util.NoSuchElementException;

import org.mesika.customerfeedback.dto.DefaultDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(NoSuchElementException.class)
        public ResponseEntity<Object> handleNoSuchElementException(HttpServletRequest request,
                        NoSuchElementException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value()),
                                HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<Object> handleEntityNotFoundException(HttpServletRequest request,
                        EntityNotFoundException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value()),
                                HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<Object> handleAuthenticationException(HttpServletRequest request,
                        AuthenticationException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.FORBIDDEN.value()),
                                HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
        public ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(HttpServletRequest request,
                        SQLIntegrityConstraintViolationException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO("There was a problem with your request",
                                                HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(AuthException.class)
        public ResponseEntity<Object> handleAuthException(HttpServletRequest request,
                        AuthException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpClientErrorException.class)
        public ResponseEntity<Object> handleHttpClientErrorException(HttpServletRequest request,
                        HttpClientErrorException exception) {

                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getResponseBodyAsString(),
                                                HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ParseException.class)
        public ResponseEntity<Object> handleParseException(HttpServletRequest request,
                        ParseException exception) {

                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(),
                                                HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<Object> handleDataIntegrityViolationException(HttpServletRequest request,
                        DataIntegrityViolationException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(),
                                                HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<Object> handleAuthorizationDeniedException(HttpServletRequest request,
                        AuthorizationDeniedException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(),
                                                HttpStatus.UNAUTHORIZED.value()),
                                HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(UsernameAlreadyExistsException.class)
        public ResponseEntity<Object> handleUsernameAlreadyExistsException(HttpServletRequest request,
                        UsernameAlreadyExistsException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(JwtException.class)
        public ResponseEntity<Object> handleJwtException(HttpServletRequest request,
                        JwtException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.UNAUTHORIZED.value()),
                                HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(ExpiredJwtException.class)
        public ResponseEntity<Object> handleExpiredJwtException(HttpServletRequest request,
                        ExpiredJwtException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.UNAUTHORIZED.value()),
                                HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Object> handleRuntimeException(HttpServletRequest request,
                        RuntimeException exception) {
                return new ResponseEntity<Object>(
                                new DefaultDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                                HttpStatus.BAD_REQUEST);
        }

}