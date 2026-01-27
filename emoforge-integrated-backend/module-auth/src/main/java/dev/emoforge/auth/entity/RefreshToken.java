package dev.emoforge.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberUuid;

    @Column(length = 512, nullable = false, unique = true)
    private String token;

    private boolean expired;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
