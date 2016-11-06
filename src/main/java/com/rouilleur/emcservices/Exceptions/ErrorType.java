package com.rouilleur.emcservices.Exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Rouilleur on 06/11/2016.
 */
public enum ErrorType {
    //Internal errors
    UNDOCUMENTED_INTERNAL(HttpStatus.INTERNAL_SERVER_ERROR,
            500666,
            "Undocumented internal error",
            "Something unexpected occurred on server side, but we don't know what exactly"),
    REPO_NOT_INITIALIZED(HttpStatus.INTERNAL_SERVER_ERROR,
            500001,
            "Uninitialized job repository",
            "Job repository wasn't correctly initialized"),

    //Bad request errors
    UNDOCUMENTED_BAD_REQUEST(HttpStatus.BAD_REQUEST,
            400666,
            "Undocumented request error",
            "Something was wrong with your request, but we don't know what exactly"),
    NULL_PARAMETER(HttpStatus.BAD_REQUEST,
            400001,
            "Unexpected null parameter",
            "Something was wrong with your request : a null parameter was passed");




    private final HttpStatus httpReturn;
    private final int applicationErrorCode;
    private final String title;
    private final String description;

    ErrorType(HttpStatus httpReturn, int applicationErrorCode, String title, String description) {

        this.httpReturn = httpReturn;
        this.applicationErrorCode = applicationErrorCode;
        this.title = title;
        this.description = description;
    }

    public HttpStatus getHttpReturn() {
        return httpReturn;
    }

    public int getApplicationErrorCode() {
        return applicationErrorCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}