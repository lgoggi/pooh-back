package com.springboot.blog.controller;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/signin")
  public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {

    Boolean userExists = userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail()).isPresent();
    if (!userExists) {
      System.out.println("User not found: " + loginDto.getUsernameOrEmail() );
      return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
    };

      Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        
        
    System.out.println("authentication: " + authentication);
    if ( authentication.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
      System.out.println("User logged in: " + loginDto.getUsernameOrEmail());
      return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);
    }; 
    
    System.out.println("Invalid password!" + loginDto.getUsernameOrEmail() );
    return new ResponseEntity<>("Invalid password!", HttpStatus.BAD_REQUEST);

  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    
    //checks for fields 
    if (signUpDto.getUsername().isEmpty() || signUpDto.getEmail().isEmpty() || signUpDto.getPassword().isEmpty() ) {
      return new ResponseEntity<>("Invalid field!", headers ,HttpStatus.BAD_REQUEST);
    }

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