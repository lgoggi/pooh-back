
package com.springboot.blog.entity;
import lombok.Data;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "poohs")
public class Pooh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;
    @CreatedDate
    private Instant createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User users;
}
