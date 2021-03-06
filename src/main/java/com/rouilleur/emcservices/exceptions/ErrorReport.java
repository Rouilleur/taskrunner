package com.rouilleur.emcservices.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * Created by Rouilleur on 06/11/2016.
 */
public class ErrorReport {

    private final HttpStatus httpReturn;
    private final int httpCode;
    private final int applicationErrorCode;
    private final String title;
    private final String description;
    @JsonFormat(pattern="dd/MM/yy HH:mm:ss")
    private final Date errorDate;
    private final String additionalDetails;


    public ErrorReport(ErrorType error, String additionalDetails) {
        this.httpReturn = error.getHttpReturn();
        this.httpCode = error.getHttpReturn().value();
        this.applicationErrorCode = error.getApplicationErrorCode();
        this.title = error.getTitle();
        this.description = error.getDescription();
        this.errorDate = new Date();
        this.additionalDetails = additionalDetails;
    }

    public HttpStatus getHttpReturn() {
        return httpReturn;
    }

    public int getHttpCode() {
        return httpCode;
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

    public Date getErrorDate() {
        return errorDate;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }
}
