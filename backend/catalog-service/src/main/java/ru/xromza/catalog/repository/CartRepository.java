package ru.xromza.catalog.repository;

import org.springframework.data.repository.CrudRepository;

import ru.xromza.catalog.model.Cart;

public interface CartRepository extends CrudRepository<Cart, Long> {
}
