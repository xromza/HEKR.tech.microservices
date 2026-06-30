package ru.xromza.warehouse.exceptions;

public class EmptyException extends RuntimeException {
    public EmptyException(String message) {
        super(message);
    }
}