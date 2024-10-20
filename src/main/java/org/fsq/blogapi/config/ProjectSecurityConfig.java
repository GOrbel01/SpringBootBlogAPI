package org.fsq.blogapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

        @Bean
        SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
            System.out.println("GO_DEBUG: Running Default Security Filter");
            http.csrf(CsrfConfigurer::disable);
//            http
//                .csrf(cs -> cs.ignoringRequestMatchers("/api/**"))
//                .authorizeHttpRequests(request ->
//                        request
                                //.requestMatchers("/dashboard").authenticated()
                                //.requestMatchers("/displayMessages/**").hasRole("ADMIN")
//                                .requestMatchers("/api/**").permitAll())
//                                .formLogin(formLogin ->
//                                        formLogin
//                                                .loginPage("/login")
//                                                .defaultSuccessUrl("/dashboard")
//                                                .failureUrl("/login?error=true")
//                                                .permitAll()
//                                )
//                .httpBasic(Customizer.withDefaults());

            return http.build();
        }
}