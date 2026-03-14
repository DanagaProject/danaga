package dao;

import dto.Comment;
import exception.DatabaseException;
import java.util.List;

/**
 * 댓글 DAO 인터페이스
 */
public interface CommentDAO {
    /**
     * 상품 번호로 댓글 목록 조회
     */
    List<Comment> selectCommentsByProductId(int productId) throws DatabaseException;

    /**
     * 댓글 등록
     */
    int insertComment(Comment comment) throws DatabaseException;
}
