package com.rouilleur.emcservices.exceptions;

/**
 * Created by Rouilleur on 06/11/2016.
 */

public class ResourceNotFoundException extends GenericException  {


    public ResourceNotFoundException(ErrorType errorType) {
        super(errorType);
    }

    public ResourceNotFoundException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public ResourceNotFoundException(ErrorType errorType, String message, Throwable initialException, boolean printStack){
        super(errorType, message, initialException, printStack);
    }
}
