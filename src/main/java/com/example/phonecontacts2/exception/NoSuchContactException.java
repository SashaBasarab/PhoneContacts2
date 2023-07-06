package com.example.phonecontacts2.exception;

public class NoSuchContactException extends RuntimeException{

    public NoSuchContactException(String message) {
        super(message);
    }

}
