package com.spartaboys.newsfeed.domain.like.board.repository;

import com.spartaboys.newsfeed.domain.board.entity.Board;
import com.spartaboys.newsfeed.domain.User;
import com.spartaboys.newsfeed.domain.like.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    boolean existsByUserAndBoard(User user, Board board);

    Optional<BoardLike> findByUserAndBoard(User user, Board board);

}
