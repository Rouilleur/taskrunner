package com.rouilleur.emcservices.exceptions;

/**
 * Created by Rouilleur on 06/11/2016.
 */

public class GenericException extends Exception  {

    private final ErrorType errorType;

    public GenericException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public GenericException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
