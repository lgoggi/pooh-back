package com.springboot.blog.controller;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.entity.User;

@RestController
public class UsersController {
    @Autowired
  private UserRepository userRepository;

  @GetMapping("/users")
  public String index() {
    List<User> list = userRepository.findAll();
    List<String> filteredList = new ArrayList<String>();
    for (User i : list) {
      filteredList.add(i.getUsername());
    }
    return ("usuarios: " + filteredList);
  }
}
