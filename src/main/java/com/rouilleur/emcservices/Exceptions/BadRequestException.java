package com.rouilleur.emcservices.Exceptions;

/**
 * Created by Rouilleur on 06/11/2016.
 */

public class BadRequestException extends Exception  {

    private final ErrorType errorType;

    public BadRequestException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
