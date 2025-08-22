package com.spartaboys.newsfeed.domain.boards.service;

import com.spartaboys.newsfeed.domain.boards.dto.response.BoardResponse;
import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.boards.exception.BoardErrorCode;
import com.spartaboys.newsfeed.domain.boards.exception.InvalidBoardException;
import com.spartaboys.newsfeed.domain.boards.mapper.BoardMapper;
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
    private final BoardMapper boardMapper;

    // UserId로 유저의 모든 게시글 조회
    public Page<BoardResponse> getBoardsByUserId(Pageable pageable,
                                                 Long userId){

        // pageable 조건을 기준으로 특정 user의 모든 게시글 조회
        Page<Board> boards = boardRepository.findAllByUserIdAndDeletedAtFalse(pageable, userId);

        return boards.map(boardMapper::toDto);
    }

    // BoardId로 board 단건 조회
    public Board getBoardById(Long boardId){

        // BoardId 유효성 검증
        isBoardValid(boardId);

        return boardRepository.findById(boardId).orElseThrow(() -> new InvalidBoardException(BoardErrorCode.BOARD_NOT_FOUND));
    }

    // 헬퍼 메서드
    // BoardId 유효성 검증
    public boolean isBoardValid(Long boardId){
        return boardRepository.existsByIdAndDeletedIsFalse(boardId);
    }
}
