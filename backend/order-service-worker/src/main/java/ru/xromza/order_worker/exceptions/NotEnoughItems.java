package ru.xromza.order_worker.exceptions;

import java.util.Map;

public class NotEnoughItems extends RuntimeException {
    Map<Long, String> errors;
    public NotEnoughItems(String message, Map<Long, String> errors) {
        super(message);
        this.errors = errors;
    }
    public Map<Long, String> getErrors() {
        return errors;
    }

}
