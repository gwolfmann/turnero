package com.espou.turnero.authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationWebFilter extends AuthenticationWebFilter {

    public JwtAuthenticationWebFilter(JwtAuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

//    @Override
//    protected Mono<Void> onAuthenticationSuccess(Authentication authentication, ServerWebExchange exchange) {
//        return ReactiveSecurityContextHolder.clearContext().then(super.onAuthenticationSuccess(authentication, exchange));
//    }
}
