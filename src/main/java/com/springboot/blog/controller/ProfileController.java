package com.springboot.blog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;

@RestController
public class ProfileController {
      @Autowired
  private UserRepository userRepository;

  @GetMapping("/profile/{username}")
  public ResponseEntity<Object>  profile(@PathVariable("username") String username) {
    User user = (User) userRepository.findByUsername(username);
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("username", user.getUsername());
    
    return new ResponseEntity<Object>( user, HttpStatus.OK);
  }
}
