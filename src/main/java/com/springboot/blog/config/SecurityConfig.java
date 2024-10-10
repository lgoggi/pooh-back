package com.springboot.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.blog.service.SecurityFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Autowired
  SecurityFilter securityFilter;
  private UserDetailsService userDetailsService;

  public SecurityConfig(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
 
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf().disable()
      .sessionManagement( session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
      .authorizeHttpRequests( authorize -> authorize
        .requestMatchers(HttpMethod.POST, "/pooh/**").authenticated()
        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
        .anyRequest().authenticated()
      )
      .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
  
    return http.build();
  }
}