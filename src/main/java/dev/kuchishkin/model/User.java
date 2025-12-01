package dev.kuchishkin.model;

import dev.kuchishkin.enums.UserRole;

public record User(
    Long id,
    String login,
    String passwordHash,
    UserRole role
) {

}
