package com.espou.turnero.authentication;

import com.espou.turnero.storage.UserDTO;
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

    public Mono<AuthenticationResponse> authenticate(LoginCredentials loginCredentials){
        return userRepository.findByInternalId(loginCredentials.getUsername())
            .flatMap(user -> passwordMatches(loginCredentials.getPassword(), user))
            .flatMap(user-> Mono.just(AuthenticationResponse.builder()
                .token(getMapToken(user.getInternalId()))
                .profile(user.getProfile())
                .internalId(user.getInternalId())
                .build()));
    }

    private Mono<UserDTO> passwordMatches(String rawPassword, UserDTO userInternalId ) {
        // Implement password hashing and matching logic here
        // You can use libraries like BCrypt to securely hash and match passwords
        // For simplicity, we are skipping password hashing in this example
        if (rawPassword.equals(userInternalId.getPassw())) {
            return Mono.just(userInternalId);
        }
        return Mono.error(new RuntimeException("Do not match passwords"));
    }

    private String getMapToken(String internalId){
        return jwtUtil.generateToken(internalId);
        //return Collections.singletonMap("token", tk);
    }
}
