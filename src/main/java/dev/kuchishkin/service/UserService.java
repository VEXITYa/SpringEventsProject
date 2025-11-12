package dev.kuchishkin.service;

import dev.kuchishkin.entity_converters.UserEntityConverter;
import dev.kuchishkin.model.User;
import dev.kuchishkin.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserEntityConverter userEntityConverter;
    private final UserRepository userRepository;

    public UserService(
        UserEntityConverter userEntityConverter,
        UserRepository userRepository
    ) {
        this.userEntityConverter = userEntityConverter;
        this.userRepository = userRepository;
    }

    public User save(User user) {
        log.info("Save user: login = {}", user.login());
        var entity = userEntityConverter.toEntity(user);
        var savedUser = userRepository.save(entity);
        return userEntityConverter.toModel(savedUser);
    }

    public boolean isUserExistsByLogin(String login) {
        return userRepository.findByLogin(login)
            .isPresent();
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
            .map(userEntityConverter::toModel)
            .orElseThrow(() -> new EntityNotFoundException("User wasn't found by login = %s"
                .formatted(login)));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .map(userEntityConverter::toModel)
            .orElseThrow(() -> new EntityNotFoundException("User wasn't found by id = %s"
                .formatted(userId)));
    }
}
