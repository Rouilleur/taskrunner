package com.rouilleur.emcservices.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Rouilleur on 06/11/2016.
 */
public enum ErrorType {
    //Internal errors
    UNDOCUMENTED_INTERNAL(HttpStatus.INTERNAL_SERVER_ERROR,
            500666,
            "Undocumented internal error",
            "Something unexpected occurred on server side"),
    REPO_NOT_INITIALIZED(HttpStatus.INTERNAL_SERVER_ERROR,
            500001,
            "Uninitialized job repository",
            "Job repository wasn't correctly initialized"),
    REFRESH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
            500002,
            "Error while trying to read job info",
            "An error occurred while trying to read data to refresh job information"),

    //Bad request errors
    UNDOCUMENTED_BAD_REQUEST(HttpStatus.BAD_REQUEST,
            400666,
            "Undocumented request error",
            "Something is wrong with your request"),
    NULL_PARAMETER(HttpStatus.BAD_REQUEST,
            400001,
            "Unexpected null parameter",
            "Something was wrong with your request : a null parameter was passed"),

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,
            404001,
            "Resource not found",
            "The resource you specified doesn't exist"),

    RESOURCE_LOCK_TIMEOUT(HttpStatus.LOCKED,
            423001,
                "Timeout while trying to lock resource",
                "A timeout was reached while trying to acquire lock on the specified resource");





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
