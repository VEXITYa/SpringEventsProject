package dev.kuchishkin.model;


public record Location(
    Long id,
    String name,
    String address,
    int capacity,
    String description
) {

}
