package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class EventTypeException extends AbstractException {
    private static final String MESSAGE = "Event type error";

    public EventTypeException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }

}
