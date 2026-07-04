package ru.xromza.user.mapper;

import ru.xromza.user.dto.IndividualDetailsResponseDto;
import ru.xromza.user.model.IndividualDetails;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IndividualDetailsResponseMapper {
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "midName", source = "midName")
    @Mapping(target = "birthDate", source = "birthDate")
    IndividualDetailsResponseDto toDto(IndividualDetails details);

    List<IndividualDetailsResponseDto> toDtoList(List<IndividualDetails> details);
}