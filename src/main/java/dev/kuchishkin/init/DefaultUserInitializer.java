package dev.kuchishkin.init;

import dev.kuchishkin.enums.UserRole;
import dev.kuchishkin.model.User;
import dev.kuchishkin.service.UserService;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public DefaultUserInitializer(
        UserService userService,
        PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        if (!userService.isUserExistsByLogin("user")) {
            userService.save(new User(
                null,
                "user",
                passwordEncoder.encode("user"),
                UserRole.USER
            ));
        }
        if (!userService.isUserExistsByLogin("admin")) {
            userService.save(new User(
                null,
                "admin",
                passwordEncoder.encode("admin"),
                UserRole.ADMIN
            ));
        }
    }
}
