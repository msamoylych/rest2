package org.java.rest.service.exception;

public abstract class AccountException extends Exception {

    AccountException(String message) {
        super(message);
    }
}
