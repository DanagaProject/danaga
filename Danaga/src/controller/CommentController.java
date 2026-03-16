package controller;

import dto.Comment;
import exception.DatabaseException;
import service.CommentService;
import service.CommentServiceImpl;
import view.FailView;
import view.SuccessView;
import java.util.Collections;
import java.util.List;

/**
 * 댓글 Controller
 */
public class CommentController {
    private final CommentService commentService;

    public CommentController() {
        this.commentService = CommentServiceImpl.getInstance();
    }

    /**
     * 상품별 댓글 목록 조회
     */
    public List<Comment> getCommentsByProductId(int productId) {
        try {
            return commentService.getCommentsByProductId(productId);
        } catch (DatabaseException e) {
            FailView.printMessage("댓글 조회 중 오류 발생: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 댓글 등록
     */
    public void addComment(int productId, String userId, String content) {
        try {
            Comment comment = new Comment();
            comment.setProductId(productId);
            comment.setUserId(userId);
            comment.setContent(content);

            commentService.addComment(comment);
            SuccessView.printMessage("댓글이 등록되었습니다.");
        } catch (DatabaseException e) {
            FailView.printMessage("댓글 등록 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 댓글 수정
     */
    public void updateComment(int commentId, String content) {
        try {
            Comment comment = new Comment();
            comment.setCommentId(commentId);
            comment.setContent(content);

            commentService.updateComment(comment);
            SuccessView.printMessage("댓글이 수정되었습니다.");
        } catch (DatabaseException e) {
            FailView.printMessage("댓글 수정 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 댓글 삭제
     */
    public void deleteComment(int commentId) {
        try {
            commentService.deleteComment(commentId);
            SuccessView.printMessage("댓글이 삭제되었습니다.");
        } catch (DatabaseException e) {
            FailView.printMessage("댓글 삭제 중 오류 발생: " + e.getMessage());
        }
    }
}
