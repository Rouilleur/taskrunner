package com.rouilleur.emcservices.Exceptions;

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
}
