package com.spartaboys.newsfeed.domain.like.board.service;

import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.boards.service.BoardInternalService;
import com.spartaboys.newsfeed.domain.like.board.exception.BoardLikeErrorCode;
import com.spartaboys.newsfeed.domain.like.board.repository.BoardLikeRepository;
import com.spartaboys.newsfeed.domain.like.entity.BoardLike;
import com.spartaboys.newsfeed.domain.like.exception.AlreadyLikedException;
import com.spartaboys.newsfeed.domain.like.exception.LikeAccessDeniedException;
import com.spartaboys.newsfeed.domain.like.exception.LikeErrorCode;
import com.spartaboys.newsfeed.domain.like.exception.LikeNotFoundException;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardLikeCommandService {

    private final UserInternalService userInternalService;
    private final BoardInternalService boardInternalService;

    private final BoardLikeRepository boardLikeRepository;

    /**
     * 게시글에 좋아요를 등록합니다.
     * <p>
     * 이미 해당 사용자가 같은 게시글에 좋아요를 눌렀다면 예외가 발생합니다.
     * 성공적으로 등록되면 BoardLike 엔티티가 저장되고,
     * 게시글의 좋아요 수가 1 증가합니다.
     *
     * @param loginId 좋아요를 누른 사용자 ID
     * @param boardId 좋아요가 눌린 게시글 ID
     * @throws IllegalStateException 사용자가 이미 같은 게시글에 좋아요를 눌렀을 경우 발생
     */
    public void likeBoard(Long loginId, Long boardId) {
        User user = userInternalService.getUserObjectById(loginId);
        Board board = boardInternalService.getBoardById(boardId);

        // 좋아요 중복 방지
        if (boardLikeRepository.existsByUserAndBoard(user, board)) {
            throw new AlreadyLikedException(BoardLikeErrorCode.ALREADY_LIKED_BOARD);
        }

        boardLikeRepository.save(
                BoardLike.create(user, board)
        );

        board.increaseLikes();
    }

    /**
     * 게시글 좋아요 취소 메서드
     *
     * <p>주어진 사용자 ID(loginId)와 게시글 ID(boardId)를 기반으로
     * 해당 사용자가 누른 좋아요를 찾아 삭제한다.</p>
     *
     * <ul>
     *   <li>좋아요가 존재하지 않으면 {@link LikeNotFoundException} 발생</li>
     *   <li>좋아요 작성자가 아닌 경우 {@link LikeAccessDeniedException} 발생</li>
     * </ul>
     *
     * @param loginId 좋아요를 누른 사용자 ID
     * @param boardId 좋아요가 눌린 게시글 ID
     * @throws LikeNotFoundException     사용자가 해당 게시글에 좋아요를 누르지 않은 경우
     * @throws LikeAccessDeniedException 좋아요 작성자가 아닌 사용자가 삭제를 시도한 경우
     */
    public void unlikeBoard(long loginId, Long boardId) {
        User user = userInternalService.getUserObjectById(loginId);
        Board board = boardInternalService.getBoardById(boardId);

        BoardLike boardLike = boardLikeRepository.findByUserAndBoard(user, board)
                .orElseThrow(() -> new LikeNotFoundException(LikeErrorCode.LIKE_NOT_FOUND));

        // 좋아요 삭제 권한 체크 (좋아요를 누른 본인만 삭제 가능)
        if (!boardLike.isOwnerBy(user)) {
            throw new LikeAccessDeniedException(LikeErrorCode.NOT_LIKE_OWNER);
        }

        boardLikeRepository.delete(boardLike);
        board.decreaseLikes();
    }
}
