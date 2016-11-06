package com.rouilleur.emcservices.Exceptions;

/**
 * Created by Rouilleur on 06/11/2016.
 */

public class InternalErrorException extends Exception  {


    private final ErrorType errorType;


    public InternalErrorException(ErrorType errorType) {
        this.errorType = errorType;
    }


    public ErrorType getErrorType() {
        return errorType;
    }
}
