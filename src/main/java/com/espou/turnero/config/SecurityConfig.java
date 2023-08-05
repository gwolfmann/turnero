package com.espou.turnero.config;
import com.espou.turnero.authentication.JwtAuthenticationManager;
import com.espou.turnero.authentication.JwtAuthenticationWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/public/**").permitAll()
                                .pathMatchers(HttpMethod.POST, "/api/auth/login").permitAll() // Allow login endpoint
                                .pathMatchers(HttpMethod.GET, "/providers").permitAll() // Allow GET requests to /providers
                                .anyExchange().authenticated()
                )
                .addFilterAt(new JwtAuthenticationWebFilter(new JwtAuthenticationManager()), SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
