package ru.xromza.warehouse.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.xromza.warehouse.dto.PreOrderWarehouseResponseDto;
import ru.xromza.warehouse.dto.WarehouseRequestDto;
import ru.xromza.warehouse.dto.WarehouseResponseDto;
import ru.xromza.warehouse.model.Warehouse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-01T02:30:16+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class WarehouseMapperImpl implements WarehouseMapper {

    @Override
    public Warehouse toEntity(WarehouseRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        Warehouse.WarehouseBuilder warehouse = Warehouse.builder();

        warehouse.address( requestDto.getAddress() );

        return warehouse.build();
    }

    @Override
    public WarehouseResponseDto toResponse(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }

        WarehouseResponseDto.WarehouseResponseDtoBuilder warehouseResponseDto = WarehouseResponseDto.builder();

        warehouseResponseDto.id( warehouse.getId() );
        warehouseResponseDto.address( warehouse.getAddress() );

        return warehouseResponseDto.build();
    }

    @Override
    public PreOrderWarehouseResponseDto toPreOrderResponse(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }

        PreOrderWarehouseResponseDto.PreOrderWarehouseResponseDtoBuilder preOrderWarehouseResponseDto = PreOrderWarehouseResponseDto.builder();

        preOrderWarehouseResponseDto.id( warehouse.getId() );
        preOrderWarehouseResponseDto.address( warehouse.getAddress() );

        return preOrderWarehouseResponseDto.build();
    }

    @Override
    public List<WarehouseResponseDto> toResponseList(List<Warehouse> warehouses) {
        if ( warehouses == null ) {
            return null;
        }

        List<WarehouseResponseDto> list = new ArrayList<WarehouseResponseDto>( warehouses.size() );
        for ( Warehouse warehouse : warehouses ) {
            list.add( toResponse( warehouse ) );
        }

        return list;
    }
}
