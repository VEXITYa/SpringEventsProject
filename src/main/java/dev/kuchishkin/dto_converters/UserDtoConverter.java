package dev.kuchishkin.dto_converters;

import dev.kuchishkin.dto.UserDto;
import dev.kuchishkin.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public UserDto convertUserToDto(User user) {
        return new UserDto(
            user.id(),
            user.login(),
            user.role()
        );
    }
}
