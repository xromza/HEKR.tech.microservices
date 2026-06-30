package ru.xromza.warehouse.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.xromza.warehouse.dto.StockResponseDto;
import ru.xromza.warehouse.model.Stock;
import ru.xromza.warehouse.model.StockId;
import ru.xromza.warehouse.model.Warehouse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-01T02:30:16+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class StockResponseMapperImpl implements StockResponseMapper {

    @Override
    public StockResponseDto toResponse(Stock stock) {
        if ( stock == null ) {
            return null;
        }

        StockResponseDto.StockResponseDtoBuilder stockResponseDto = StockResponseDto.builder();

        stockResponseDto.variantId( stockIdVariantId( stock ) );
        stockResponseDto.warehouseId( stockIdWarehouseId( stock ) );
        stockResponseDto.address( stockWarehouseAddress( stock ) );
        if ( stock.getQuantity() != null ) {
            stockResponseDto.quantity( stock.getQuantity().longValue() );
        }

        return stockResponseDto.build();
    }

    @Override
    public List<StockResponseDto> toResponseList(Collection<Stock> stock) {
        if ( stock == null ) {
            return null;
        }

        List<StockResponseDto> list = new ArrayList<StockResponseDto>( stock.size() );
        for ( Stock stock1 : stock ) {
            list.add( toResponse( stock1 ) );
        }

        return list;
    }

    private Long stockIdVariantId(Stock stock) {
        if ( stock == null ) {
            return null;
        }
        StockId id = stock.getId();
        if ( id == null ) {
            return null;
        }
        Long variantId = id.variantId();
        if ( variantId == null ) {
            return null;
        }
        return variantId;
    }

    private Long stockIdWarehouseId(Stock stock) {
        if ( stock == null ) {
            return null;
        }
        StockId id = stock.getId();
        if ( id == null ) {
            return null;
        }
        Long warehouseId = id.warehouseId();
        if ( warehouseId == null ) {
            return null;
        }
        return warehouseId;
    }

    private String stockWarehouseAddress(Stock stock) {
        if ( stock == null ) {
            return null;
        }
        Warehouse warehouse = stock.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        String address = warehouse.getAddress();
        if ( address == null ) {
            return null;
        }
        return address;
    }
}
