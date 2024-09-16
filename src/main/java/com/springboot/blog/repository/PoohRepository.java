package com.springboot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blog.entity.Pooh;

import java.util.Optional;

public interface PoohRepository extends JpaRepository<Pooh, Long>  {
  Optional<Pooh> findById(String id);
  // Optional<Pooh> findByUserID(String authorID);
}
