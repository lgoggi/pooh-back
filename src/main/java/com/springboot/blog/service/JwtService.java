package com.springboot.blog.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.springboot.blog.entity.User;


@Service
public class JwtService {
  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${security.jwt.expiration-time}")
  private long jwtExpiration;
  
   public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secretKey);
      String token = JWT.create()
        .withIssuer("pooh-project")
        .withSubject(user.getUsername())
        .withExpiresAt(genExpirationDate())
        .sign((algorithm));
      return token;
    } catch (JWTCreationException exception) {
        throw new RuntimeException("Error while generating token", exception);
    }
   }

   public String validateToken(String token) {
      try {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.require(algorithm)
          .withIssuer("pooh-project")
          .build()
          .verify(token)
          .getSubject();
      } catch (JWTVerificationException exception) {
        return "";
      }
   }

   private Instant genExpirationDate() {
      return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
   }
}