package com.espou.turnero.config;

import com.espou.turnero.authentication.JwtAuthenticationManager;
import com.espou.turnero.authentication.JwtAuthenticationWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges ->
                exchanges
                    .pathMatchers("/public/**").permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/auth/login").permitAll() // Allow login endpoint
                    .pathMatchers("/providers/**").permitAll()
                    .pathMatchers("/resources/**").permitAll()
                    .pathMatchers("/receivers/**").permitAll()
                    .pathMatchers("/tasks/**").permitAll()
                    .pathMatchers("/users/**").permitAll()
                    .pathMatchers("/meets/**").permitAll()
                    .pathMatchers( HttpMethod.GET,"/meetsForDate").permitAll()
                    .anyExchange().authenticated()
            )
            .csrf(csrf -> csrf.disable());

        return http
            .addFilterBefore(corsWebFilter(), SecurityWebFiltersOrder.CORS) // Apply CORS filter first
            .addFilterAt(new JwtAuthenticationWebFilter(new JwtAuthenticationManager()), SecurityWebFiltersOrder.AUTHENTICATION)
            .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:4200"); // Add your frontend's origin
        corsConfig.addAllowedMethod("*"); // Allow all HTTP methods
        corsConfig.addAllowedHeader("*"); // Allow all headers
        corsConfig.setAllowCredentials(true); // Allow sending cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
