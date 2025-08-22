package com.spartaboys.newsfeed.domain.like.entity;

import com.spartaboys.newsfeed.domain.like.dto.ContentType;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "Likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Like(User user) {
        this.user = user;
    }

    public boolean isOwnerBy(User user) {
        return ObjectUtils.nullSafeEquals(this.user, user);
    }

    public abstract ContentType getContentType();
    public abstract Long getContentId();
}
