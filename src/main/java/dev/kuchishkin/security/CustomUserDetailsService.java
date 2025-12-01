package dev.kuchishkin.security;

import dev.kuchishkin.entity.UserEntity;
import dev.kuchishkin.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByLogin(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return User.withUsername(user.getLogin())
            .password(user.getPasswordHash())
            .authorities(user.getRole())
            .build();
    }
}
