package com.game.project.kalahgame.exception;

import com.game.project.kalahgame.controller.GameController;
import com.game.project.kalahgame.controller.PlayController;
import com.game.project.kalahgame.controller.PlayerController;
import com.game.project.kalahgame.dto.GlobalErrorResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;


@ControllerAdvice(assignableTypes = {GameController.class, PlayController.class, PlayerController.class})
public class AdminExceptionHandler  {


private static final Log LOGGER = LogFactory.getLog(AdminExceptionHandler.class);



@ExceptionHandler
public ResponseEntity onHttpMessageNotReadable(final HttpMessageNotReadableException e) throws Throwable {
        return createBadRequest();
        }

@ExceptionHandler(HttpMessageConversionException.class)
public ResponseEntity passThroughHttpMessageConversionException(HttpMessageConversionException ex) {
        return createBadRequest();
        }

private ResponseEntity createBadRequest() {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        HttpHeaders headers = getJsonHttpHeaders();
        return new ResponseEntity<>(new GlobalErrorResponse(responseStatus.toString(), responseStatus.getReasonPhrase()), headers, responseStatus);
        }

@ExceptionHandler(Exception.class)
public ResponseEntity handleException(Exception exception) {
        HttpHeaders headers = getJsonHttpHeaders();

        LOGGER.error("Global exception caught.", exception);
        return new ResponseEntity<>(new GlobalErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

/**
 * Need to pass through AccessDeniedException as HTTP FORBIDDEN
 */
@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity handleAccessDeniedException(HttpServletRequest req, AccessDeniedException ex) {
        HttpStatus httpStatusForResponse;
        if (req.getUserPrincipal() == null) {
        // if not logged in then 401 should be returned
        httpStatusForResponse = HttpStatus.UNAUTHORIZED;
        } else {
        httpStatusForResponse = HttpStatus.FORBIDDEN;
        }
        HttpHeaders headers = getJsonHttpHeaders();
        return new ResponseEntity<>(new GlobalErrorResponse(httpStatusForResponse.toString(), httpStatusForResponse.getReasonPhrase()), headers, httpStatusForResponse);
        }


protected HttpHeaders getJsonHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
        }



}