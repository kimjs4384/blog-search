package com.blog.api.exceptions;

public class APIServerException extends RuntimeException {
   
    public APIServerException(String message) {
        super(message);
    }
}
