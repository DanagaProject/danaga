package dao;

import dto.User;
import exception.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DBUtil;

/**
 * 회원 DAO 구현체
 */
public class UserDAOImpl implements UserDAO {

    private static UserDAO instance = new UserDAOImpl();

    private UserDAOImpl() {
    }

    public static UserDAO getInstance() {
        return instance;
    }

    @Override
    public void insert(User user) throws DatabaseException {
        String sql = "INSERT INTO users (user_id, password, balance, status, role) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getBalance());
            pstmt.setString(4, user.getStatus());
            pstmt.setString(5, user.getRole());

            int result = pstmt.executeUpdate();

            if (result == 0) {
                throw new DatabaseException("회원가입 실패: 데이터 저장 오류");
            }

        } catch (SQLException e) {
            throw new DatabaseException("회원가입 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt);
        }
    }

    @Override
    public User selectByIdAndPassword(String userId, String password) throws DatabaseException {
        String sql = "SELECT user_id, password, balance, status, role FROM users WHERE user_id = ? AND password = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getInt("balance"),
                        rs.getString("status"),
                        rs.getString("role"));
            }

            return null; // 로그인 실패

        } catch (SQLException e) {
            throw new DatabaseException("로그인 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public boolean existsById(String userId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;

        } catch (SQLException e) {
            throw new DatabaseException("아이디 중복 확인 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public User selectById(String userId) throws DatabaseException {
        String sql = "SELECT user_id, password, balance, status, role FROM users WHERE user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getInt("balance"),
                        rs.getString("status"),
                        rs.getString("role"));
            }

            return null;

        } catch (SQLException e) {
            throw new DatabaseException("회원 조회 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt, rs);
        }
    }
}
