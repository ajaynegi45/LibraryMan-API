package com.ajaynegi.SpringBoot.RESTBookAPI.ExceptionHandler;

import org.springframework.http.HttpStatus;

public class ErrorResponse
{
    private String message;
    private HttpStatus status;
    private String trace;

    public ErrorResponse()
    {
    }

    public ErrorResponse(String message, HttpStatus status, String trace) {
        this.message = message;
        this.status = status;
        this.trace = trace;
    }

    @Override
    public String toString() {
        return "{'message':'"+message+"','status':'"+status+"','trace':'"+trace+"'}";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }
}
