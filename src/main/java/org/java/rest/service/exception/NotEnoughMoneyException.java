package org.java.rest.service.exception;

public class NotEnoughMoneyException extends AccountException {

    public NotEnoughMoneyException() {
        super("Not enough money");
    }
}
