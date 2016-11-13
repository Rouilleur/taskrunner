package com.rouilleur.emcservices.exceptions;

/**
 * Created by Rouilleur on 06/11/2016.
 */

public class BadRequestException extends GenericException  {

    public BadRequestException(ErrorType errorType) {
        super(errorType);
    }

    public BadRequestException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public BadRequestException(ErrorType errorType, String message, Throwable initialException, boolean printStack){
        super(errorType, message, initialException, printStack);
    }
}
