package com.rouilleur.emcservices.exceptions;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by Rouilleur on 09/11/2016.
 */
public class InternalErrorMatcher extends TypeSafeMatcher<InternalErrorException> {

    public static InternalErrorMatcher isOfType(ErrorType item) {
        return new InternalErrorMatcher(item);
    }

    private ErrorType foundErrorType;
    private final ErrorType expectedErrorType;

    private InternalErrorMatcher(ErrorType expectedErrorType) {
        this.expectedErrorType = expectedErrorType;
    }

    @Override
    protected boolean matchesSafely(final InternalErrorException exception) {
        foundErrorType = exception.getErrorType();
        return foundErrorType.equals(expectedErrorType);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(foundErrorType)
                .appendText(" was not found instead of ")
                .appendValue(expectedErrorType);
    }
}