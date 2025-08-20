package com.dutra.ordering.domain.exceptions;


public class CustomerArchivedException extends DomainException {

    public CustomerArchivedException(String message) {
        super(message);
    }

    public CustomerArchivedException(String message, Throwable cause) {
        super(message, cause);
    }
}
