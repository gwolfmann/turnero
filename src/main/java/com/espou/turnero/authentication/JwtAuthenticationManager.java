package com.espou.turnero.authentication;
import com.espou.turnero.service.UserService;
import com.espou.turnero.storage.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        if (jwtUtil.validateToken(token)) {
            String userId = jwtUtil.extractUserId(token);

            UserDTO userDTO = userService.getUserByInternalId(userId).block(); // Block to get the user details synchronously
            UserDetails userDetails = new CustomUserDetails(userDTO);
            return Mono.just(new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities()));
        } else {
            return Mono.empty();
        }
    }
}
