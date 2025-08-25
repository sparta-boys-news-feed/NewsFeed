package com.spartaboys.newsfeed.domain.boards.service;

import com.spartaboys.newsfeed.domain.boards.dto.request.BoardRequest;
import com.spartaboys.newsfeed.domain.boards.dto.response.BoardResponse;
import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.boards.exception.BoardErrorCode;
import com.spartaboys.newsfeed.domain.boards.exception.InvalidBoardException;
import com.spartaboys.newsfeed.domain.boards.mapper.BoardMapper;
import com.spartaboys.newsfeed.domain.boards.repository.BoardRepository;
import com.spartaboys.newsfeed.domain.follow.entity.Follow;
import com.spartaboys.newsfeed.domain.follow.service.internal.FollowInternalService;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardExternalService {

    // 의존성
    // Mapper
    private final BoardMapper boardMapper;

    // Repository
    private final BoardRepository boardRepository;

    // InternalService
    private final UserInternalService userInternalService;
    private final FollowInternalService followInternalService;


    @Transactional
    public BoardResponse createBoardByUserId(BoardRequest request,
                                             Long loginUserId) {
        User user = userInternalService.getUserObjectById(loginUserId);

        // DB에 게시글 저장
        Board board = boardRepository.save(boardMapper.toEntity(request, user));

        return boardMapper.toDto(board);
    }

    public Page<BoardResponse> getAllBoards(Pageable pageable) {

        // pageable 조건을 기준으로 모든 게시글 조회
        Page<Board> boards = boardRepository.findAllByDeletedIsFalse(pageable);

        return boards.map(boardMapper::toDto);
    }

    public Page<BoardResponse> getAllFolloweesBoards(Pageable pageable, Long loginUserId) {

        // 현재 로그인한 사용자가 팔로우하고 있는 유저 목록 조회
        List<Follow> followees = followInternalService.getFolloweesByUserId(loginUserId);

        List<Long> followeeIds = followees.stream().map(Follow::getId).toList();

        Page<Board> boards = boardRepository.findAllByUserIdInAndDeletedIsFalse(followeeIds, pageable);

        return boards.map(boardMapper::toDto);
    }

    public BoardResponse getBoardByBoardId(Long boardId) {

        Board board = isBoardNullOrDeleted(boardId);

        return boardMapper.toDto(board);
    }

    public Page<BoardResponse> getBoardsByTitleOrContent(Pageable pageable, String title, String content) {

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
        else {
            throw new InvalidBoardException(BoardErrorCode.BOARD_INVALID_SEARCH_NOT_ALLOWED);
        }
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
