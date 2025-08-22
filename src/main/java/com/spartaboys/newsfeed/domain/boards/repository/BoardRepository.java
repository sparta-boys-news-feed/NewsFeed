package com.spartaboys.newsfeed.domain.boards.repository;

import com.spartaboys.newsfeed.domain.boards.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByTitle(String title, Pageable pageable);

    Page<Board> findAllByContent(String content, Pageable pageable);

    Page<Board> findAllByUserIdAndDeletedAtFalse(Pageable pageable, Long userId);

    boolean existsByIdAndDeletedIsFalse(Long boardId);
}
