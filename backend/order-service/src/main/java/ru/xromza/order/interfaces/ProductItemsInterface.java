package ru.xromza.order.interfaces;

import java.util.List;

public interface ProductItemsInterface<T extends ProductInfoInterface> {
    List<T> getItems();
    void setItems(List<T> items);
}
