package org.java.rest.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.java.rest.service.exception.AccountException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public final ResponseEntity<?> handleException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.unprocessableEntity().build();
    }
}
