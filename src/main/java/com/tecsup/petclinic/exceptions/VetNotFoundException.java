package com.tecsup.petclinic.exception;

public class VetNotFoundException extends Exception {
    public VetNotFoundException(String message) {
        super(message);
    }
}