package dev.kuchishkin.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;


public record LocationDto(
    @Null
    Long id,

    @NotBlank
    String name,

    @NotBlank
    String address,

    @NotNull
    @Min(1)
    int capacity,

    @NotBlank
    @Size(max = 150)
    String description
) {

}
