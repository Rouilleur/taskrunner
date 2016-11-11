package com.rouilleur.emcservices.Exceptions;

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
}
