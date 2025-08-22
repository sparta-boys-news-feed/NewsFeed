package com.spartaboys.newsfeed.domain.like.board.repository;

import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.like.entity.BoardLike;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    boolean existsByUserAndBoard(User user, Board board);

    Optional<BoardLike> findByUserAndBoard(User user, Board board);

    List<BoardLike> findAllByUser(User user);

    Page<BoardLike> findAllByUser(User user, Pageable pageable);
}
