package ru.xromza.order_worker.exceptions;

public class EmptyException extends RuntimeException {
    public EmptyException(String message) {
        super(message);
    }
}