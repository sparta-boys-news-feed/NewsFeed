package com.spartaboys.newsfeed.domain.like.comments.repository;

import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.like.entity.BoardLike;
import com.spartaboys.newsfeed.domain.like.entity.CommentLike;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByUserAndComment(User user, Comment comment);

    Optional<CommentLike> findByUserAndComment(User user, Comment comment);

    List<CommentLike> findAllByUser(User user);

    Page<CommentLike> findAllByUser(User user, Pageable pageable);
}
