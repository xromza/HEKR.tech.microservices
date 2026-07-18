package ru.xromza.user.exceptions;

public class NewPasswordMatchesOldException extends RuntimeException {
    public NewPasswordMatchesOldException(String message) {
        super(message);
    }
}
