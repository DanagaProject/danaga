package dao;

import dto.User;
import exception.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * 회원가입 - 새로운 회원 정보 저장
     * status_id는 기본값 12(code 테이블의 ACTIVE)로 설정
     * @param user 저장할 회원 정보
     * @throws DatabaseException DB 오류 발생 시
     */
    @Override
    public void insert(User user) throws DatabaseException {
        String sql = "INSERT INTO users (user_id, password, balance, status_id, role) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getBalance());
            pstmt.setInt(4, 12); // 기본값: 12 (code 테이블의 ACTIVE)
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

    /**
     * 로그인 - 아이디와 비밀번호로 회원 조회
     * code 테이블과 JOIN하여 status_id에 해당하는 value를 status로 조회
     * @param userId 사용자 ID
     * @param password 비밀번호
     * @return 조회된 회원 정보, 없으면 null
     * @throws DatabaseException DB 오류 발생 시
     */
    @Override
    public User selectByIdAndPassword(String userId, String password) throws DatabaseException {
        String sql = "SELECT u.user_id, u.password, u.balance, c.value AS status, u.role " +
                     "FROM users u " +
                     "INNER JOIN code c ON u.status_id = c.id " +
                     "WHERE u.user_id = ? AND u.password = ?";

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
                        rs.getString("status"), // code 테이블의 value 값
                        rs.getString("role"));
            }

            return null; // 로그인 실패

        } catch (SQLException e) {
            throw new DatabaseException("로그인 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt, rs);
        }
    }

    /**
     * 아이디 중복 확인
     * @param userId 확인할 사용자 ID
     * @return 아이디가 존재하면 true, 없으면 false
     * @throws DatabaseException DB 오류 발생 시
     */
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

    /**
     * 아이디로 회원 조회
     * code 테이블과 JOIN하여 status_id에 해당하는 value를 status로 조회
     * @param userId 조회할 사용자 ID
     * @return 조회된 회원 정보, 없으면 null
     * @throws DatabaseException DB 오류 발생 시
     */
    @Override
    public User selectById(String userId) throws DatabaseException {
        String sql = "SELECT u.user_id, u.password, u.balance, c.value AS status, u.role " +
                     "FROM users u " +
                     "INNER JOIN code c ON u.status_id = c.id " +
                     "WHERE u.user_id = ?";

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
                        rs.getString("status"), // code 테이블의 value 값
                        rs.getString("role"));
            }

            return null;

        } catch (SQLException e) {
            throw new DatabaseException("회원 조회 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt, rs);
        }
    }

    /**
     * 차단된 회원 목록 조회 (status_id = 13, code 테이블의 BANNED)
     * code 테이블과 JOIN하여 status_id에 해당하는 value를 status로 조회
     * @return 차단된 회원 목록
     * @throws DatabaseException DB 오류 발생 시
     */
    @Override
    public List<User> selectBlockedUsers() throws DatabaseException {
        String sql = "SELECT u.user_id, u.password, u.balance, c.value AS status, u.role " +
                     "FROM users u " +
                     "INNER JOIN code c ON u.status_id = c.id " +
                     "WHERE u.status_id = 13";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> blockedUsers = new ArrayList<>();

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getInt("balance"),
                        rs.getString("status"), // code 테이블의 value 값
                        rs.getString("role"));
                blockedUsers.add(user);
            }

            return blockedUsers;

        } catch (SQLException e) {
            throw new DatabaseException("차단 회원 목록 조회 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt, rs);
        }
    }

    /**
     * 회원 차단하기 (status_id = 13, code 테이블의 BANNED로 변경)
     * @param userId 차단할 사용자 ID
     * @throws DatabaseException DB 오류 발생 시 또는 회원이 존재하지 않는 경우
     */
    @Override
    public void blockUser(String userId) throws DatabaseException {
        String sql = "UPDATE users SET status_id = 13 WHERE user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, userId);

            int result = pstmt.executeUpdate();

            if (result == 0) {
                throw new DatabaseException("회원 차단 실패: 회원이 존재하지 않습니다");
            }

        } catch (SQLException e) {
            throw new DatabaseException("회원 차단 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt);
        }
    }

    /**
     * 회원 차단 풀기 (status_id = 12, code 테이블의 ACTIVE로 변경)
     * @param userId 차단 해제할 사용자 ID
     * @throws DatabaseException DB 오류 발생 시 또는 회원이 존재하지 않는 경우
     */
    @Override
    public void unblockUser(String userId) throws DatabaseException {
        String sql = "UPDATE users SET status_id = 12 WHERE user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, userId);

            int result = pstmt.executeUpdate();

            if (result == 0) {
                throw new DatabaseException("차단 해제 실패: 회원이 존재하지 않습니다");
            }

        } catch (SQLException e) {
            throw new DatabaseException("차단 해제 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt);
        }
    }

    /**
     * 잔액 충전 (balance 증가)
     * @param userId 사용자 ID
     * @param amount 충전할 금액
     * @throws DatabaseException DB 오류 발생 시 또는 회원이 존재하지 않는 경우
     */
    @Override
    public void chargeBalance(String userId, int amount) throws DatabaseException {
        String sql = "UPDATE users SET balance = balance + ? WHERE user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, amount);
            pstmt.setString(2, userId);

            int result = pstmt.executeUpdate();

            if (result == 0) {
                throw new DatabaseException("잔액 충전 실패: 회원이 존재하지 않습니다");
            }

        } catch (SQLException e) {
            throw new DatabaseException("잔액 충전 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt);
        }
    }

    /**
     * 잔액 차감 (balance 감소)
     * @param userId 사용자 ID
     * @param amount 차감할 금액
     * @throws DatabaseException DB 오류 발생 시 또는 회원이 존재하지 않는 경우
     */
    @Override
    public void deductBalance(String userId, int amount) throws DatabaseException {
        String sql = "UPDATE users SET balance = balance - ? WHERE user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, amount);
            pstmt.setString(2, userId);

            int result = pstmt.executeUpdate();

            if (result == 0) {
                throw new DatabaseException("잔액 차감 실패: 회원이 존재하지 않습니다");
            }

        } catch (SQLException e) {
            throw new DatabaseException("잔액 차감 중 DB 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, pstmt);
        }
    }
}
