package service;

import dao.CommentDAO;
import dao.CommentDAOImpl;
import dto.Comment;
import exception.DatabaseException;
import java.util.List;

/**
 * 댓글 서비스 구현체
 */
public class CommentServiceImpl implements CommentService {
    private static final CommentService instance = new CommentServiceImpl();
    private final CommentDAO commentDAO;

    private CommentServiceImpl() {
        this.commentDAO = new CommentDAOImpl();
    }

    public static CommentService getInstance() {
        return instance;
    }

    @Override
    public List<Comment> getCommentsByProductId(int productId) throws DatabaseException {
        return commentDAO.selectCommentsByProductId(productId);
    }

    @Override
    public void addComment(Comment comment) throws DatabaseException {
        int result = commentDAO.insertComment(comment);
        if (result == 0) {
            throw new DatabaseException("댓글 등록에 실패했습니다.");
        }
    }

    @Override
    public void updateComment(Comment comment) throws DatabaseException {
        int result = commentDAO.updateComment(comment);
        if (result == 0) {
            throw new DatabaseException("댓글 수정에 실패했습니다.");
        }
    }

    @Override
    public void deleteComment(int commentId) throws DatabaseException {
        int result = commentDAO.deleteComment(commentId);
        if (result == 0) {
            throw new DatabaseException("댓글 삭제에 실패했습니다.");
        }
    }
}
