package dev.nlu.moduleloginj2ee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_credentials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredential {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Builder.Default
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "verification_token", length = 64)
    private String verificationToken;

    @Column(name = "verification_sent_at")
    private LocalDateTime verificationSentAt;

    @Builder.Default
    @Column(name = "verification_attempts", nullable = false)
    private int verificationAttempts = 0;

    @Column(name = "reset_token", length = 64)
    private String resetToken;

    @Column(name = "reset_expires_at")
    private LocalDateTime resetExpiresAt;
}
