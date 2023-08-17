package com.espou.turnero.config;

import com.espou.turnero.authentication.JwtAuthenticationManager;
import com.espou.turnero.authentication.JwtAuthenticationWebFilter;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${spring.profiles.active}")
    private String activeProfile;

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
                    .pathMatchers("/ping/**").permitAll() // Allow access to /ping
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
        String allowedOrigin="*";

        if ("local".equals(activeProfile)) {
            allowedOrigin = "http://localhost:4200";
        } else if ("cloud".equals(activeProfile)) {
            allowedOrigin = "http://d32ix83j9g8sl2.cloudfront.net/";
        }

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin(allowedOrigin); // Add your frontend's origin
        corsConfig.addAllowedMethod("*"); // Allow all HTTP methods
        corsConfig.addAllowedHeader("*"); // Allow all headers
        corsConfig.setAllowCredentials(true); // Allow sending cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
