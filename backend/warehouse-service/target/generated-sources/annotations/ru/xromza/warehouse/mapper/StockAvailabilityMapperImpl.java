package ru.xromza.warehouse.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.xromza.warehouse.dto.ItemWarehouseAvailabilityResponseDto;
import ru.xromza.warehouse.model.Stock;
import ru.xromza.warehouse.model.Warehouse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-01T02:30:16+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class StockAvailabilityMapperImpl implements StockAvailabilityMapper {

    @Override
    public ItemWarehouseAvailabilityResponseDto toResponse(Stock stock) {
        if ( stock == null ) {
            return null;
        }

        ItemWarehouseAvailabilityResponseDto itemWarehouseAvailabilityResponseDto = new ItemWarehouseAvailabilityResponseDto();

        itemWarehouseAvailabilityResponseDto.warehouseId = stockWarehouseId( stock );
        itemWarehouseAvailabilityResponseDto.availableQuantity = stock.getQuantity();

        return itemWarehouseAvailabilityResponseDto;
    }

    private Long stockWarehouseId(Stock stock) {
        if ( stock == null ) {
            return null;
        }
        Warehouse warehouse = stock.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        Long id = warehouse.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
