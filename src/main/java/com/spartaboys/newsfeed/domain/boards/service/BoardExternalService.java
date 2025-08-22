package com.spartaboys.newsfeed.domain.boards.service;

import com.spartaboys.newsfeed.domain.boards.dto.request.BoardRequest;
import com.spartaboys.newsfeed.domain.boards.dto.response.BoardResponse;
import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.boards.exception.BoardErrorCode;
import com.spartaboys.newsfeed.domain.boards.exception.InvalidBoardException;
import com.spartaboys.newsfeed.domain.boards.mapper.BoardMapper;
import com.spartaboys.newsfeed.domain.boards.repository.BoardRepository;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardExternalService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final UserInternalService userInternalService;


    @Transactional
    public BoardResponse getBoardByUserId(BoardRequest request,
                                          Long loginUserID) {
        User user = userInternalService.getUserObjectById(loginUserID);

        // DB에 게시글 저장
        Board board = boardRepository.save(boardMapper.toEntity(request, user));

        return boardMapper.toDto(board);
    }

    public Page<BoardResponse> getAllBoards(Pageable pageable) {

        // pageable 조건을 기준으로 모든 게시글 조회
        Page<Board> boards = boardRepository.findAllByDeletedIsFalse(pageable);

        return boards.map(boardMapper::toDto);
    }

    public BoardResponse getBoardByBoardId(Long boardId) {

        Board board = isBoardNullOrDeleted(boardId);

        return boardMapper.toDto(board);
    }

    public Page<BoardResponse> getBoardsByTitleOrContent(Pageable pageable, String title, String content) {
        // TODO: 리팩토링이 필요할 것으로 사료
        // content만 있을 경우 content로 조회
        if (title != null && content == null) {
            // 게시글 제목을 기준으로 페이징 조회
            Page<Board> boards = boardRepository.findAllByTitleAndDeletedIsFalse(title, pageable);

            return boards.map(boardMapper::toDto);
        }
        // title만 있을 경우 title로 조회
        else if (title == null && content != null) {
            // 게시글 내용을 기준으로 페이징 조회
            Page<Board> boards = boardRepository.findAllByContentAndDeletedIsFalse(content, pageable);

            return boards.map(boardMapper::toDto);
        }
        // title과 content가 모두 있거나 모두 없는 경우 예외처리
        else throw new InvalidBoardException(BoardErrorCode.BOARD_INVALID_SEARCH_NOT_ALLOWED);
    }

    @Transactional
    public BoardResponse updateBoardDetailsByBoardId(Long boardId, Long loginUserId, BoardRequest request) {

        Board board = isBoardNullOrDeleted(boardId);

        // boardId 작성자 Id와 로그인 유저의 Id가 다를 경우 예외처리
        if (isNotSameUserId(loginUserId, board.getUser().getId())) {
            throw new InvalidBoardException(BoardErrorCode.BOARD_FORBIDDEN);
        }

        // 게시글 수정사항 반영
        board.updateBoard(request.title(), request.content());

        return boardMapper.toDto(board);
    }

    @Transactional
    public void deleteBoardByBoardId(Long boardId, Long loginUserId) {

        Board board = isBoardNullOrDeleted(boardId);
        // boardId 작성자 Id와 로그인 유저의 Id가 다를 경우 예외처리
        if (isNotSameUserId(loginUserId, board.getUser().getId())) {
            throw new InvalidBoardException(BoardErrorCode.BOARD_FORBIDDEN);
        }

        boardRepository.deleteById(boardId);
    }

    // 헬퍼메서드
    // LoginUser와 Board를 작성한 User가 같은지 검증
    public boolean isNotSameUserId(Long loginUserId, Long boardUserId) {
        return !loginUserId.equals(boardUserId);
    }

    // boardId가 존재하지만 삭제됐을 경우 예외처리
    public Board isBoardNullOrDeleted(Long boardId) {
        // DB에 boardId가 없을 경우 예외처리
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new InvalidBoardException(BoardErrorCode.BOARD_NOT_FOUND));
        // boardId가 존재하지만 삭제됐을 경우 예외처리
        if (board.isDeleted()) {
            throw new InvalidBoardException(BoardErrorCode.BOARD_ALREADY_DELETED);
        }

        return board;
    }
}
