package service;

import dto.Comment;
import exception.DatabaseException;
import java.util.List;

/**
 * 댓글 서비스 인터페이스
 */
public interface CommentService {
    /**
     * 상품별 댓글 목록 조회
     */
    List<Comment> getCommentsByProductId(int productId) throws DatabaseException;

    /**
     * 댓글 등록
     */
    void addComment(Comment comment) throws DatabaseException;

    /**
     * 댓글 수정
     */
    void updateComment(Comment comment) throws DatabaseException;

    /**
     * 댓글 삭제
     */
    void deleteComment(int commentId) throws DatabaseException;
}
