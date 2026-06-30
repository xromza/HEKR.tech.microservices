package ru.xromza.warehouse.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.xromza.warehouse.dto.StockResponsePlainDto;
import ru.xromza.warehouse.model.Stock;
import ru.xromza.warehouse.model.StockId;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-01T02:30:16+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class StockResponsePlainMapperImpl implements StockResponsePlainMapper {

    @Override
    public StockResponsePlainDto toResponse(Stock stock) {
        if ( stock == null ) {
            return null;
        }

        StockResponsePlainDto.StockResponsePlainDtoBuilder stockResponsePlainDto = StockResponsePlainDto.builder();

        stockResponsePlainDto.variantId( stockIdVariantId( stock ) );
        stockResponsePlainDto.quantity( stock.getQuantity() );

        return stockResponsePlainDto.build();
    }

    @Override
    public List<StockResponsePlainDto> toResponseList(Collection<Stock> stock) {
        if ( stock == null ) {
            return null;
        }

        List<StockResponsePlainDto> list = new ArrayList<StockResponsePlainDto>( stock.size() );
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
}
