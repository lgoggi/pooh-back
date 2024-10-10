package com.springboot.blog.controller;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<String> authenticateUser(@RequestBody @Valid LoginDto loginDto) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));  
    String token = jwtService.generateToken( (User) authentication.getPrincipal() );
    
    return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody @Valid SignUpDto signUpDto) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");

    // add check for username exists in a DB
    if (userRepository.existsByUsername(signUpDto.getUsername())) {
      return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
    }

    // add check for email exists in DB
    if (userRepository.existsByEmail(signUpDto.getEmail())) {
      return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
    }

    // create user object
    User user = new User();
    user.setUsername(signUpDto.getUsername());
    user.setEmail(signUpDto.getEmail());
    user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

    Role roles = roleRepository.findByName("ROLE_ADMIN").get();
    user.setRoles(Collections.singleton(roles));

    userRepository.save(user);
    System.out.println("User created: " + user.getUsername());
    return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

  }
}