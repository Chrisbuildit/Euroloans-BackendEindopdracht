package com.euroloans.eindopdracht.exception;

public class UserNameNotFoundException extends RuntimeException {
    public static final long serialVersionUID = 1L;
    public UserNameNotFoundException(String message) {
        super(message);
    }
}
