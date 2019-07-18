package com.game.project.kalahgame.dto;

public class GlobalErrorResponse {
    private String errorCode;
    private String message;
    private Object data;

    public GlobalErrorResponse() {
    }

    public GlobalErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }


    public GlobalErrorResponse(String errorCode, String message, Object data) {
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public static GlobalErrorResponse createGlobalErrorWithBasicsInformation(String errorCode, String message) {
        GlobalErrorResponse globalErrorResponse = new GlobalErrorResponse();
        globalErrorResponse.setErrorCode(errorCode);
        globalErrorResponse.setMessage(message);
        return globalErrorResponse;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GlobalErrorResponse)) {
            return false;
        }
        GlobalErrorResponse other = (GlobalErrorResponse) o;
        if (!this.getErrorCode().equals(other.getErrorCode()) || !this.getData().equals(other.getData()) || !this.getMessage().equals(other.getMessage())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (this.getErrorCode() != null) {
            hashCode = this.getErrorCode().hashCode();
        }
        if (this.getMessage() != null) {
            hashCode = hashCode + this.getMessage().hashCode();
        }
        if (this.getData() != null) {
            hashCode = hashCode + this.getData().hashCode();
        }
        return hashCode;

    }
}
