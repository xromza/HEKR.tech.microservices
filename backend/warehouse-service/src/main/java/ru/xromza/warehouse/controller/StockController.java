package ru.xromza.warehouse.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.xromza.warehouse.dto.StockResponseDto;
import ru.xromza.warehouse.mapper.StockResponseMapper;
import ru.xromza.warehouse.service.StockService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    private final StockResponseMapper stockResponseMapper;
    
    @GetMapping("/{variantId}")
    public List<StockResponseDto> getMethodName(@PathVariable Long variantId) {
        return stockResponseMapper.toResponseList(stockService.getByVariantId(variantId));
    }
    
}
