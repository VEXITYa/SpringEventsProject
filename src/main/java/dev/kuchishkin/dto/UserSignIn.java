package dev.kuchishkin.dto;

public record UserSignIn(
    String login,
    String password,
    Long id
) {

}
