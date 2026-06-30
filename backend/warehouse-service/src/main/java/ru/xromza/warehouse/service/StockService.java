package ru.xromza.warehouse.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.warehouse.dto.ItemDto;
import ru.xromza.warehouse.dto.StockResponsePlainDto;
import ru.xromza.warehouse.exceptions.BadRequestException;
import ru.xromza.warehouse.exceptions.NotFoundException;
import ru.xromza.warehouse.mapper.StockResponsePlainMapper;
import ru.xromza.warehouse.model.Stock;
import ru.xromza.warehouse.model.StockId;
import ru.xromza.warehouse.model.Warehouse;
import ru.xromza.warehouse.repository.StockRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final StockResponsePlainMapper stockResponsePlainMapper;
    private final WarehouseService warehouseService;

    public List<Stock> getByVariantId(Long variantId) {
        return stockRepository.findAllByVariantId(variantId);
    }

    public Stock getByVariantIdAndWarehouseId(Long variantId, Long warehouseId) {
        return stockRepository.findById(new StockId(variantId, warehouseId))
                .orElseThrow(() -> new NotFoundException("Данный вариант товара не найден на складе"));
    }

    public List<StockResponsePlainDto> getAllByWarehouseId(Long warehouseId) {
        return stockResponsePlainMapper.toResponseList(stockRepository.findAllByWarehouseId(warehouseId));
    }

    @Transactional(readOnly = true)
    public Map<Long, Stock> getStocksMapByVariantIdsAndWarehouseId(Long warehouseId, List<Long> variantIds) {
        if (variantIds == null || variantIds.isEmpty()) {
            return Map.of();
        }

        List<Stock> stocks = stockRepository.findAllByWarehouseIdAndVariantIdsIn(warehouseId, variantIds);
        return stocks.stream().collect(Collectors.toMap(stock -> stock.getId().variantId(), stock -> stock));
    }

    @Transactional(readOnly = true)
    public Map<Long, List<Stock>> getStocksMapByVariantIds(List<Long> variantIds) {
        if (variantIds == null || variantIds.isEmpty()) {
            return Map.of();
        }

        List<Stock> stocks = stockRepository.findAllByVariantIdsIn(variantIds);
        return stocks.stream()
                .collect(Collectors.groupingBy(stock -> stock.getId().variantId()));
    }

    @Transactional
    public void saveStock(Stock stock) {
        stockRepository.save(stock);
    }

    @Transactional
    public StockResponsePlainDto upsertStock(Long variantId, Long warehouseId, Integer quantity) {

        StockId stockId = new StockId(variantId, warehouseId);

        Stock stock = stockRepository.findById(stockId)
                .orElseGet(() -> createNewStock(stockId, warehouseId, variantId));
        stock.setQuantity(quantity);
        if (stock.getVersion() == null)
            stock = stockRepository.save(stock);
        return stockResponsePlainMapper.toResponse(stock);
    }

    @Transactional
    public void revertStocks(Set<ItemDto> items, Long warehouseId) {
        for (ItemDto item : items) {
            addStock(item.getVariantId(), warehouseId, item.getQuantity());
        }
    }

    @Transactional
    public StockResponsePlainDto addStock(Long variantId, Long warehouseId, Integer diff) {
        StockId stockId = new StockId(variantId, warehouseId);
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new NotFoundException("Склад не найден"));
        Integer curQuantity = stock.getQuantity();
        if (diff < -curQuantity) {
            throw new BadRequestException("На складе не может быть отрицательное количество товара");
        }
        stock.setQuantity(curQuantity + diff);
        return stockResponsePlainMapper.toResponse(stockRepository.save(stock));
    }

    private Stock createNewStock(StockId stockId, Long warehouseId, Long variantId) {
        Warehouse warehouse = warehouseService.findById(warehouseId);

        return Stock.builder()
                .id(stockId)
                .warehouse(warehouse)
                .build();
    }
}
