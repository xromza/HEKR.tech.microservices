package ru.xromza.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.xromza.user.utils.UserRole;

@Builder
@Getter
@Setter
public class AuthResultDto {
    private String accessToken;
    @Builder.Default
    private String type = "Bearer";
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String description;
}
