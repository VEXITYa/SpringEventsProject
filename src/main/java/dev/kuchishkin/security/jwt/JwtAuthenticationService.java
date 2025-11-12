package dev.kuchishkin.security.jwt;

import dev.kuchishkin.dto.SignInRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;

    public JwtAuthenticationService(AuthenticationManager authenticationManager,
        JwtTokenManager jwtTokenManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String authenticateUser(SignInRequest signInRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signInRequest.login(),
                signInRequest.password()
            )
        );
        return jwtTokenManager.generateToken(signInRequest.login());
    }
}
