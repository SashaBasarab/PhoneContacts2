package com.example.phonecontacts2.exception;

public class UserWithProvidedContactNameAlreadyExists extends RuntimeException{

    public UserWithProvidedContactNameAlreadyExists(String message) {
        super(message);
    }
}
