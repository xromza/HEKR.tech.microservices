package ru.xromza.order_worker.exceptions;

import java.util.List;

public class CatalogUnavailableException extends Exception {
    private final List<Long> ids;

    public CatalogUnavailableException(String message, List<Long> ids) {
        super(message);
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }
}
