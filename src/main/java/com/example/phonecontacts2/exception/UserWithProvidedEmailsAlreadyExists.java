package com.example.phonecontacts2.exception;

public class UserWithProvidedEmailsAlreadyExists extends RuntimeException{

    public UserWithProvidedEmailsAlreadyExists(String message) {
        super(message);
    }
}
