package ru.xromza.warehouse.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.warehouse.dto.ItemDto;
import ru.xromza.warehouse.dto.OrderItemEventDto;
import ru.xromza.warehouse.dto.StockResponseDto;
import ru.xromza.warehouse.dto.StockResponsePlainDto;
import ru.xromza.warehouse.exceptions.BadRequestException;
import ru.xromza.warehouse.exceptions.NotEnoughItems;
import ru.xromza.warehouse.exceptions.NotFoundException;
import ru.xromza.warehouse.mapper.StockResponseMapper;
import ru.xromza.warehouse.mapper.StockResponsePlainMapper;
import ru.xromza.warehouse.model.Stock;
import ru.xromza.warehouse.model.StockId;
import ru.xromza.warehouse.model.Warehouse;
import ru.xromza.warehouse.repository.StockRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    private final StockRepository stockRepository;
    private final StockResponsePlainMapper stockResponsePlainMapper;
    private final WarehouseService warehouseService;
    private final StockResponseMapper stockResponseMapper;

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

    @Transactional
    public boolean reserveItems(Long warehouseId, List<OrderItemEventDto> items) throws NotEnoughItems {
        List<Long> ids = items.stream()
                .map(OrderItemEventDto::getVariantId)
                .toList();
        Map<Long, Stock> stocks = getStocksMapByVariantIdsAndWarehouseId(warehouseId, ids);
        List<Stock> newStocks = new ArrayList<>();
        boolean canCheckout = true;
        Map<Long, String> errors = new HashMap<>();
        log.info("Проверяю остатки склада {} для заказа", warehouseId);
        for (OrderItemEventDto item : items) {
            Long variantId = item.getVariantId();
            Stock stock = stocks.get(variantId);
            Stock newStock = stock.toBuilder().build();
            if (item.getQuantity() > stock.getQuantity()) {
                canCheckout = false;
                errors.put(item.getVariantId(), "Недостаточно товара. Доступно: " + stock.getQuantity());
            } else {
                newStock.setQuantity(stock.getQuantity() - item.getQuantity());
                newStocks.add(newStock);
            }
        }
        if (!canCheckout) {
            log.warn("Товара не хватает. Отменяем заказ");
            throw new NotEnoughItems("NotEnoughItems", errors);
        } else {
            log.info("Товара достаточно. Сохраняю остатки");
            stockRepository.saveAll(newStocks);
        }
        return true;
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

    @Transactional(readOnly = true)
    public List<StockResponseDto> getFullStocksByVariantIds(List<Long> variantIds) {
        List<Stock> stocks = stockRepository.findAllByVariantIdsInWithWarehouse(variantIds);
        return stockResponseMapper.toResponseList(stocks);
    }
}
