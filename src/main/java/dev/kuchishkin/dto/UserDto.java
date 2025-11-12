package dev.kuchishkin.dto;

import dev.kuchishkin.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public record UserDto(
    @Null
    Long id,

    @NotBlank
    @Size(min = 3)
    String login,

    @NotBlank
    UserRole role
) {

}
