package com.ecommerceserver.controllers;


import com.ecommerceserver.service.exception.BadImageSizeException;
import com.ecommerceserver.service.utils.ApiError;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LogManager.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex)
    {
        logger.error(ex);
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalAccessException.class)
    protected ResponseEntity<Object> handleIllegalAccessException(
            IllegalAccessException ex)
    {
        logger.error(ex);
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(BadImageSizeException.class)
    protected ResponseEntity<Object> handleBadImageSizeException(
            BadImageSizeException ex)
    {
        logger.error(ex);
        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InterruptedException.class)
    protected ResponseEntity<Object> handleInterruptedException(
            InterruptedException ex)
    {
        logger.error(ex);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Object> handleIOException(
            IOException ex)
    {
        logger.error(ex);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(
            Exception ex)
    {
        logger.error("Returned Internal Server Error, because", ex);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError)
    {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
