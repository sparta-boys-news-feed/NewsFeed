package com.spartaboys.newsfeed.domain.users.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spartaboys.newsfeed.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "users",
        // Unique 설정, 제약조건 이름 부여 및 DDL 반영
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = {"email"}),
                @UniqueConstraint(name = "uk_users_nickname", columnNames = {"nickname"})
        },
        // 인덱스 생성용, 검색 성능 향상
        indexes = {
                @Index(name = "idx_users_email", columnList = "email"),
                @Index(name = "idx_users_nickname", columnList = "nickname")
        }
)
@Getter
@ToString(exclude = "password")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false, length = 320)
    private String email;

    @NotBlank
    @Size(min = 60, max = 100) // bcrypt(60)
    @Column(nullable = false, length = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String nickname;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    // Create Update 시 이메일 정규화 진행
    @PrePersist
    @PreUpdate
    public void prePersistUpdate() {
        this.email = normalizeEmail(this.email);
        if(this.nickname != null) this.nickname = this.nickname.trim();
    }

    private static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
