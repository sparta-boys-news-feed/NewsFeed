package com.spartaboys.newsfeed.domain.follow.entity;

import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "follows",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_follower_followee", columnNames = {"followee_id", "follower_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id", updatable = false, nullable = false)
    private User followee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", updatable = false, nullable = false)
    private User follower;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private Follow(User followee, User follower) {
        this.followee = followee;
        this.follower = follower;
    }

    public static Follow create(User follower, User followee) {
        return Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();
    }
}
