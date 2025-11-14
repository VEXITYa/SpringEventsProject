package dev.kuchishkin.security.jwt;

import dev.kuchishkin.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;

    public JwtTokenFilter(JwtTokenManager jwtTokenManager, @Lazy UserService userService) {
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        var jwt = authHeader.substring(7);

        String login;
        try {
            login = jwtTokenManager.getLoginFromToken(jwt);
        } catch (Exception e) {
            logger.error("Error reading jwt", e);
            filterChain.doFilter(request, response);
            return;
        }

        var user = userService.findByLogin(login);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            user,
            null,
            List.of(new SimpleGrantedAuthority(user.role().name()))
        );
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }
}
