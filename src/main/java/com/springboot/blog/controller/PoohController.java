package com.springboot.blog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.springboot.blog.entity.Pooh;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.PoohDto;
import com.springboot.blog.repository.PoohRepository;
import com.springboot.blog.repository.UserRepository;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class PoohController {
  @Autowired
  private PoohRepository poohRepository;

  @Autowired
  private UserRepository userRepository;

  @PostMapping("/publish")
  public ResponseEntity<String> publish(@RequestBody PoohDto poohDto) throws Exception {
    User user = userRepository.findByUsername(poohDto.getAuthorID()).orElseThrow(() -> new Exception("User not found"));
    Pooh pooh = new Pooh();
    System.out.println("user: " + user + "pooh: " + pooh);
    pooh.setText(poohDto.getText());
    pooh.setUsers(user);
    poohRepository.save(pooh);
    return new ResponseEntity<String>(HttpStatus.OK);
  }

  @GetMapping("/see")
  public String see() {
     List<Pooh> list = poohRepository.findAll();
    List<String> filteredList = new ArrayList<String>();
    for (Pooh i : list) {
      filteredList.add(i.getText());
    }
    return ("poohs: " + filteredList);
  }
}
