package dev.kuchishkin.model;

public record FieldModification<T>(
    T oldField,
    T newField
) {

}
