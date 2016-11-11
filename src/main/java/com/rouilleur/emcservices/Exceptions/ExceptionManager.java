package com.rouilleur.emcservices.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Rouilleur on 06/11/2016.
 */

//TODO : add info about the initial request in logs


@ControllerAdvice
public class ExceptionManager {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionManager.class);

    //TODO : Use same source for the HttpStatus (currently, it can be inconsistent between the annotation and the error message)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    //public void handleBadRequest(BadRequestException ex, ServerHttpResponse response) {
    public ErrorReport handleBadRequest(BadRequestException ex) {
        logger.warn("Bad Request : {}", ex.getErrorType().getTitle() + " -- " + ex.getMessage());
        ex.printStackTrace();
        return new ErrorReport(ex.getErrorType(), ex.getMessage());
    }

    //TODO : Use same source for the HttpStatus (currently, it can be inconsistent between the annotation and the error message)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalErrorException.class)
    @ResponseBody
    public ErrorReport handleInternalError(InternalErrorException ex) {
        logger.error("Internal Error : {}", ex.getErrorType().getTitle() + " -- " + ex.getMessage());
        return new ErrorReport(ex.getErrorType(), ex.getMessage());
    }

    //TODO : Use same source for the HttpStatus (currently, it can be inconsistent between the annotation and the error message)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ErrorReport handleResourceNotFoundError(ResourceNotFoundException ex) {
        logger.warn("Resource not found Error : {}", ex.getErrorType().getTitle() + " -- " + ex.getMessage());
        return new ErrorReport(ex.getErrorType(), ex.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorReport handleUnexpectedError(Exception ex) {

        logger.error("Unhandled Exception");
        ex.printStackTrace();
        return new ErrorReport(ErrorType.UNDOCUMENTED_INTERNAL, "This should not happen");
    }

}