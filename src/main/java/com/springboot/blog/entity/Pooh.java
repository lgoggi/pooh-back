
package com.springboot.blog.entity;
import lombok.Data;
import jakarta.persistence.*;

import java.util.Optional;
import java.util.Set;

@Data
@Entity
@Table(name = "poohs")
public class Pooh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User users;
}
