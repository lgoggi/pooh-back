package com.springboot.blog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pooh")
public class PoohController {
  @Autowired
  private PoohRepository poohRepository;

  @Autowired
  private UserRepository userRepository;

  @PostMapping("/publish")
  public ResponseEntity<String> publish(@RequestBody @Valid  PoohDto poohDto) throws Exception {
    User user = (User) userRepository.findByUsername(poohDto.getAuthorID());
    Pooh pooh = new Pooh();
    pooh.setText(poohDto.getText());
    pooh.setUsers(user);
    poohRepository.save(pooh);
    return new ResponseEntity<String>(HttpStatus.OK);
  }

  @GetMapping("/see")
  public ResponseEntity<Object> see() {
     List<Pooh> list = poohRepository.findAllByOrderByCreatedDateDesc();
    List<Object> filteredList = new ArrayList<Object>();
    for (Pooh i : list) {
       Map<String, Object> map = new HashMap<String, Object>();
       map.put("id", i.getId());
       map.put("text", i.getText());
       map.put("author", i.getUsers().getUsername());
      filteredList.add(map);
    }
    return new ResponseEntity<Object>(filteredList, HttpStatus.OK);
  }
}
