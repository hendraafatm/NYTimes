package com.app.nytimes.helper;

public class AppResponse {
    // fixed in all responses
    private String errorMsg;

    public String getMessage() {
        return errorMsg;
    }

    public void setMessage(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
