package com.spartaboys.newsfeed.domain.boards.service;

import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.boards.exception.BoardErrorCode;
import com.spartaboys.newsfeed.domain.boards.exception.InvalidBoardException;
import com.spartaboys.newsfeed.domain.boards.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardInternalService {

    private final BoardRepository boardRepository;

    // UserId로 유저의 모든 게시글 조회 (Using At users)
    public Page<Board> getBoardsByUserId(Pageable pageable,
                                                 Long userId) {

        // pageable 조건을 기준으로 특정 user의 모든 게시글 조회
        Page<Board> boards = boardRepository.findAllByUserIdAndDeletedAtFalse(pageable, userId);

        return boards;
    }

    // BoardId로 board 단건 조회 (Using At like)
    public Board getBoardById(Long boardId) {

        // BoardId 유효성 검증
        isBoardValid(boardId);

        return boardRepository.findById(boardId).get();
    }

    // BoardId 유효성 검증
    public void isBoardValid(Long boardId) {
        if (!boardRepository.existsByIdAndDeletedIsFalse(boardId)) {
            throw new InvalidBoardException(BoardErrorCode.BOARD_NOT_FOUND);
        }
        ;
    }
}
