package org.fsq.blogapi.auth;

import org.fsq.blogapi.model.User;
import org.fsq.blogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BlogApiAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        User user = userRepository.findUserByUsername(userName);
        if (user != null && user.getUsername().equals(userName) && passwordEncoder.matches(authentication.getCredentials().toString(),user.getPasswordHash())) {
            return new UsernamePasswordAuthenticationToken(user.getUsername(),user.getName(),null);
        } else {
            throw new BadCredentialsException("Invalid Credentials.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
