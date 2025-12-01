package dev.kuchishkin.entity_converters;

import dev.kuchishkin.entity.UserEntity;
import dev.kuchishkin.enums.UserRole;
import dev.kuchishkin.model.User;
import org.springframework.stereotype.Component;


@Component
public class UserEntityConverter {

    public UserEntity toEntity(User user) {
        return new UserEntity(
            user.id(),
            user.login(),
            user.passwordHash(),
            user.role().name()
        );
    }

    public User toModel(UserEntity userEntity) {
        return new User(
            userEntity.getId(),
            userEntity.getLogin(),
            userEntity.getPasswordHash(),
            UserRole.valueOf(userEntity.getRole())
        );
    }
}
