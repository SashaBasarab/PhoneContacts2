package com.example.phonecontacts2.exception;

public class UserWithProvidedPhoneNumbersAlreadyExists extends RuntimeException{

    public UserWithProvidedPhoneNumbersAlreadyExists(String message) {
        super(message);
    }

}
