package com.springboot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import com.springboot.blog.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
 UserDetails findByEmail(String email);

 UserDetails findByUsernameOrEmail(String username, String email);

 UserDetails findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

}