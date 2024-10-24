package org.fsq.blogapi.config;

import jakarta.servlet.http.HttpServletResponse;
import org.fsq.blogapi.auth.BlogApiAuthenticationProvider;
import org.fsq.blogapi.auth.filter.JwtAuthFilter;
import org.fsq.blogapi.model.Response;
import org.fsq.blogapi.service.JwtService;
import org.fsq.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {
    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService(); // Ensure UserInfoService implements UserDetailsService
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable);
            http
                .authorizeHttpRequests(request ->
                    request
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/blogs").permitAll()
                        .requestMatchers("/api/users").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/blogs/**").authenticated()

                )
                 .authenticationProvider(new BlogApiAuthenticationProvider())
                 .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
//                    .exceptionHandling(exceptionHandler -> exceptionHandler
//                        .accessDeniedHandler((request, response, accessDeniedException) ->
//                            authenticationErrorHandler(response, HttpStatus.FORBIDDEN))
//                       .authenticationEntryPoint((request, response, accessDeniedException) ->
//                            authenticationErrorHandler(response, HttpStatus.UNAUTHORIZED)));

        return http.build();
    }

    private void authenticationErrorHandler(HttpServletResponse response, HttpStatus status) throws IOException {
        Response.buildHttpResponse(status,"Authentication Failed.",response);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}