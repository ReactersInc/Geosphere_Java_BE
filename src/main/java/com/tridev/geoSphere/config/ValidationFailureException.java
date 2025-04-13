package com.tridev.geoSphere.config;

public class ValidationFailureException extends RuntimeException {
    private static final long serialVersionUID = -7758625605372170496L;

    private String errorCode;

    private String errorDescription;

    private int httpStatusCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public ValidationFailureException(String invalidEmail) {
    }

    public ValidationFailureException(String errorCode, String errorDescription, int httpStatusCode) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ValidationException{");
        sb.append("errorCode='").append(errorCode).append('\'');
        sb.append(", errorDescription='").append(errorDescription).append('\'');
        sb.append(", httpStatusCode='").append(httpStatusCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
