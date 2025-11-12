package dev.kuchishkin.service;

import dev.kuchishkin.dto.SignUpRequest;
import dev.kuchishkin.enums.UserRole;
import dev.kuchishkin.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(SignUpRequest signUpRequest) {
        if (userService.isUserExistsByLogin(signUpRequest.login())) {
            throw new IllegalArgumentException("User with login already exists");
        }

        var hashedPassword = passwordEncoder.encode(signUpRequest.password());
        var user = new User(
            null,
            signUpRequest.login(),
            hashedPassword,
            UserRole.USER
        );

        return userService.save(user);
    }

}
