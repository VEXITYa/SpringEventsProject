package dev.kuchishkin.controller;

import dev.kuchishkin.dto.JwtTokenResponse;
import dev.kuchishkin.dto.UserDto;
import dev.kuchishkin.model.User;
import dev.kuchishkin.security.jwt.JwtAuthenticationService;
import dev.kuchishkin.service.UserRegistrationService;
import dev.kuchishkin.service.UserService;
import dev.kuchishkin.dto.SignInRequest;
import dev.kuchishkin.dto.SignUpRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final JwtAuthenticationService jwtAuthenticationService;
    private final UserRegistrationService userRegistrationService;

    public UserController(UserService userService,
        JwtAuthenticationService jwtAuthenticationService,
        UserRegistrationService userRegistrationService
    ) {
        this.userService = userService;
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
        @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        log.info("Post request createUser: login = {}", signUpRequest.login());

        var user = userRegistrationService.registerUser(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(convertUserToDto(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserInfo(
        @PathVariable Long userId
    ) {
        log.info("Get request getUserInfo: userId = {}", userId);
        var user = userService.findById(userId);
        return ResponseEntity.ok(convertUserToDto(user));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
        @RequestBody @Valid SignInRequest signInRequest
    ) {
        log.info("Post request authenticate: login = {}", signInRequest.login());

        var token = jwtAuthenticationService.authenticateUser(signInRequest);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }


    private UserDto convertUserToDto(User user) {
        return new UserDto(
            user.id(),
            user.login(),
            user.role()
        );
    }

}
