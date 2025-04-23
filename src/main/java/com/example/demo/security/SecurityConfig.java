package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtFilter = jwtFilter;
        this.customUserDetailsService = customUserDetailsService;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()

                // Cho phép GET mọi người đều có thể xem các bộ FlashcardSet công khai
                .requestMatchers(HttpMethod.GET, "/api/flashcardsets/**").permitAll()

                // Các thao tác thêm/sửa/xóa thì cần xác thực bằng JWT
                .requestMatchers(HttpMethod.POST, "/api/flashcardsets/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/flashcardsets/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/flashcardsets/**").authenticated()

                // Mặc định các request khác cũng cần xác thực
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }   

    
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     UserDetails user = User.withUsername("admin")
    //         .password(passwordEncoder().encode("password"))
    //         .roles("USER")
    //         .build();

    //     return new InMemoryUserDetailsManager(user);
    // } vì đã có customUserDetailsService



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {//UserDetailsService userDetailsService
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService); //userDetailsService
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(List.of(authenticationProvider));
    }
}
