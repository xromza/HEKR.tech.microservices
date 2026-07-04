package ru.xromza.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.xromza.user.dto.AuthResult;
import ru.xromza.user.dto.AuthResultDto;

@Mapper(componentModel = "spring")
public interface AuthResultMapper {
    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "type", source = "type")
    public AuthResultDto toDto(AuthResult authResult);
} 