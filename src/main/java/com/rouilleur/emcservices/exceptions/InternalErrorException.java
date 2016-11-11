package com.rouilleur.emcservices.exceptions;

/**
 * Created by Rouilleur on 06/11/2016.
 */

public class InternalErrorException extends GenericException  {


    public InternalErrorException(ErrorType errorType) {
        super(errorType);
    }

    public InternalErrorException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
