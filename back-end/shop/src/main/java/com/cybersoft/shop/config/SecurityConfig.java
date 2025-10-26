package com.cybersoft.shop.config;

import com.cybersoft.shop.filter.JwtTokenFilter;
import com.cybersoft.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        return http
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowedOrigins(List.of("http://localhost:3000"));
                            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                            config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
                            config.setAllowCredentials(true);
                            return config;
                        }))
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/users/**").permitAll();
                    request.requestMatchers("/products/**").permitAll();
                    request.requestMatchers("/categories/**").permitAll();
                    request.requestMatchers("/brands/**").permitAll();
                    request.requestMatchers("/colors/**").permitAll();
                    request.requestMatchers("/sizes/**").permitAll();
                    request.requestMatchers("/variants/**").permitAll();
                    request.requestMatchers("/orders/**").permitAll();
                })
                .build();
    }
}
