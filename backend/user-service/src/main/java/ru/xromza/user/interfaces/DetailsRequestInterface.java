package ru.xromza.user.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.xromza.user.dto.IndividualDetailsRequestDto;
import ru.xromza.user.dto.LegalDetailsRequestDto;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = IndividualDetailsRequestDto.class, name = "INDIVIDUAL"),
    @JsonSubTypes.Type(value = LegalDetailsRequestDto.class, name = "LEGAL")
})
public interface DetailsRequestInterface {
    
}