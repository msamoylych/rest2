package org.java.rest.service.exception;

public class AccountNotFoundException extends AccountException {

    public AccountNotFoundException(Long id) {
        super("Account <" + id + "> not found");
    }
}
