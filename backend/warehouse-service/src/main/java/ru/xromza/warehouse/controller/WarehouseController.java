package ru.xromza.warehouse.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.xromza.warehouse.dto.WarehouseResponseDto;
import ru.xromza.warehouse.mapper.WarehouseMapper;
import ru.xromza.warehouse.service.WarehouseService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final WarehouseMapper warehouseMapper;
    
    @GetMapping
    public List<WarehouseResponseDto> getWarehouses() {
        return warehouseMapper.toResponseList(warehouseService.findAll());
    }
    
}
