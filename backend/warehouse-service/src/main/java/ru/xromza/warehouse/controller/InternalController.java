package ru.xromza.warehouse.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.xromza.warehouse.dto.StockResponseDto;
import ru.xromza.warehouse.service.StockService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/internal/stock")
@RequiredArgsConstructor
public class InternalController {
    private final StockService stockService;
    @GetMapping
    public List<StockResponseDto> getFullStocks(@RequestParam   List<Long> variantIds) {
        return stockService.getFullStocksByVariantIds(variantIds);
    }
    
}
