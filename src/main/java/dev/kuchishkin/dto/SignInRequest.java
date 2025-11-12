package dev.kuchishkin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
    @NotBlank
    @Size(min = 3)
    String login,

    @NotBlank
    String password
) {

}
