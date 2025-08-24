package com.spartaboys.newsfeed.domain.boards.repository;

import com.spartaboys.newsfeed.domain.boards.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByUserIdAndDeletedIsFalse(Pageable pageable, Long userId);

    boolean existsByIdAndDeletedIsFalse(Long boardId);

    Page<Board> findAllByDeletedIsFalse(Pageable pageable);

    Page<Board> findAllByTitleAndDeletedIsFalse(String title, Pageable pageable);

    Page<Board> findAllByContentAndDeletedIsFalse(String content, Pageable pageable);
}
