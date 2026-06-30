package ru.xromza.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.xromza.warehouse.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

}
