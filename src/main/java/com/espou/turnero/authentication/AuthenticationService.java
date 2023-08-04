package com.espou.turnero.authentication;

import com.espou.turnero.storage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public Mono<String> authenticate(String username, String password) {
        return userRepository.findByInternalId(username)
                .filter(user -> passwordMatches(password, user.getPassw()))
                .map(user -> jwtUtil.generateToken(user.getInternalId()))
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid username or password")));
    }

    private boolean passwordMatches(String rawPassword, String hashedPassword) {
        // Implement password hashing and matching logic here
        // You can use libraries like BCrypt to securely hash and match passwords
        // For simplicity, we are skipping password hashing in this example
        return rawPassword.equals(hashedPassword);
    }
}
