package dao;

import dto.Comment;
import exception.DatabaseException;
import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 댓글 DAO 구현체
 */
public class CommentDAOImpl implements CommentDAO {

    @Override
    public List<Comment> selectCommentsByProductId(int productId) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Comment> list = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE product_id = ? ORDER BY created_at ASC";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Comment(
                        rs.getInt("comment_id"),
                        rs.getInt("product_id"),
                        rs.getString("user_id"),
                        rs.getString("content"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("댓글 조회 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps, rs);
        }
        return list;
    }

    @Override
    public int insertComment(Comment comment) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "INSERT INTO comments (product_id, user_id, content) VALUES (?, ?, ?)";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, comment.getProductId());
            ps.setString(2, comment.getUserId());
            ps.setString(3, comment.getContent());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("댓글 등록 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps);
        }
    }

    @Override
    public int updateComment(Comment comment) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE comments SET content = ?, updated_at = NOW() WHERE comment_id = ?";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, comment.getContent());
            ps.setInt(2, comment.getCommentId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("댓글 수정 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps);
        }
    }

    @Override
    public int deleteComment(int commentId) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM comments WHERE comment_id = ?";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, commentId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("댓글 삭제 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps);
        }
    }
}
