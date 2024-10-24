package org.fsq.blogapi.controller;

import org.fsq.blogapi.model.Response;
import org.fsq.blogapi.model.Token;
import org.fsq.blogapi.model.User;
import org.fsq.blogapi.model.UserLoginRequest;
import org.fsq.blogapi.repository.UserRepository;
import org.fsq.blogapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Token> login(@RequestBody UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            Token t = new Token();
            User user = userRepository.findUserByUsername(userLoginRequest.getUsername());
            String token = jwtService.generateToken(userLoginRequest.getUsername());
            t.setUsername(user.getUsername());
            t.setName(user.getName());
            t.setToken(token);
            return ResponseEntity.status(HttpStatus.OK).body(t);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
