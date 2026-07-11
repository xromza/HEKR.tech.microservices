package ru.xromza.warehouse.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.warehouse.exceptions.NotFoundException;
import ru.xromza.warehouse.model.Warehouse;
import ru.xromza.warehouse.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public Warehouse findById(Long id) {
        return warehouseRepository.findById(id).orElseThrow(() -> new NotFoundException("Склад не найден"));
    }

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }
    @Transactional
    public Warehouse createNew(String address) {
        Warehouse warehouse = Warehouse.builder()
                .address(address).build();
        return warehouseRepository.save(warehouse);
    }
}
