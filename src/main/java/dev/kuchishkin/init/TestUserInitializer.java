package dev.kuchishkin.init;

import dev.kuchishkin.dto.SignUpRequest;
import dev.kuchishkin.repository.UserRepository;
import dev.kuchishkin.service.UserRegistrationService;
import dev.kuchishkin.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class TestUserInitializer {

    private final UserService userService;
    private final UserRegistrationService userRegistrationService;
    private final UserRepository userRepository;

    public TestUserInitializer(UserService userService,
        UserRegistrationService userRegistrationService, UserRepository userRepository) {
        this.userService = userService;
        this.userRegistrationService = userRegistrationService;
        this.userRepository = userRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        if (!userService.isUserExistsByLogin("user")) {
            userRegistrationService.registerUser(
                new SignUpRequest(
                    "user",
                    "user"
                )
            );
        }
        if (!userService.isUserExistsByLogin("admin")) {
            userRegistrationService.registerUser(
                new SignUpRequest(
                    "admin",
                    "admin"
                )
            );

            var user = userRepository.findByLogin("admin")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
            user.setRole("ADMIN");

            userRepository.save(user);
        }
    }
}
