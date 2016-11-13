package com.rouilleur.emcservices.exceptions;

/**
 * Created by Rouilleur on 06/11/2016.
 */

public class LockedResourceException extends GenericException  {

    public LockedResourceException(ErrorType errorType) {
        super(errorType);
    }

    public LockedResourceException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public LockedResourceException(ErrorType errorType, String message, Throwable initialException, boolean printStack){
        super(errorType, message, initialException, printStack);
    }
}
