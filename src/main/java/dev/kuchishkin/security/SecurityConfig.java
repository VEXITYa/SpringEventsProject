package dev.kuchishkin.security;

import dev.kuchishkin.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(
        CustomUserDetailsService customUserDetailsService,
        CustomAuthenticationEntryPoint authenticationEntryPoint,
        JwtTokenFilter jwtTokenFilter
    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.formLogin(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeHttpRequest ->
                authorizeHttpRequest
                    .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/openapi.yaml",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/configuration/**"
                    )
                        .permitAll()
                    .requestMatchers(HttpMethod.POST, "/users")
                        .permitAll()
                    .requestMatchers(HttpMethod.POST, "/users/auth")
                        .permitAll()
                    .requestMatchers(HttpMethod.GET, "/users/{userId}")
                        .hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/locations")
                        .hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/locations/{locationId}")
                        .hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/locations/{locationId}")
                        .hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/locations/**")
                        .hasAnyAuthority("ADMIN", "USER")
                    .requestMatchers(HttpMethod.POST, "/events")
                        .hasAnyAuthority("ADMIN", "USER")
                    .requestMatchers(HttpMethod.DELETE, "/events/{eventId}")
                        .hasAnyAuthority("ADMIN", "USER")
                    .requestMatchers(HttpMethod.PUT, "/events/{eventId}")
                        .hasAnyAuthority("ADMIN", "USER")
                    .requestMatchers(HttpMethod.GET, "/events/{eventId}")
                        .hasAnyAuthority("ADMIN", "USER")
                    .requestMatchers(HttpMethod.GET, "/events/my")
                        .hasAnyAuthority("ADMIN", "USER")
                    .requestMatchers(HttpMethod.GET, "/events/search")
                        .hasAnyAuthority("ADMIN", "USER")
                    .requestMatchers(HttpMethod.POST, "events/registrations/{eventId}")
                        .hasAnyAuthority("ADMIN", "USER")
                    .requestMatchers(HttpMethod.DELETE, "events/registrations/{eventId}")
                        .hasAnyAuthority("ADMIN", "USER")
                    .requestMatchers(HttpMethod.GET, "events/registrations/my")
                        .hasAnyAuthority("ADMIN", "USER")
                    .anyRequest()
                        .authenticated()
            )
            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(authenticationEntryPoint))
            .addFilterBefore(jwtTokenFilter, AnonymousAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(
        AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProviderBean() {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(true);
    }
}
