package dao;

import dto.Notification;
import exception.DatabaseException;
import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 알림 DAO 구현체
 */
public class NotificationDAOImpl implements NotificationDAO {

    @Override
    public int insertNotification(Notification notification) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "INSERT INTO notification (user_id, message, is_read) VALUES (?, ?, '0')";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, notification.getUserId());
            ps.setString(2, notification.getMessage());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("알림 등록 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps);
        }
    }

    @Override
    public Notification selectNotificationById(int notificationId) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM notification WHERE notification_id = ?";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, notificationId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return new Notification(
                        rs.getInt("notification_id"),
                        rs.getString("user_id"),
                        rs.getString("message"),
                        rs.getString("is_read"),
                        rs.getString("created_at")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("알림 조회 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps, rs);
        }
    }

    @Override
    public List<Notification> selectNotificationsByUserId(String userId) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE user_id = ? ORDER BY created_at DESC";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("notification_id"),
                        rs.getString("user_id"),
                        rs.getString("message"),
                        rs.getString("is_read"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("알림 목록 조회 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps, rs);
        }
        return list;
    }

    @Override
    public List<Notification> selectUnreadNotificationsByUserId(String userId) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE user_id = ? AND is_read = '0' ORDER BY created_at DESC";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("notification_id"),
                        rs.getString("user_id"),
                        rs.getString("message"),
                        rs.getString("is_read"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("안 읽은 알림 목록 조회 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps, rs);
        }
        return list;
    }

    @Override
    public int markAsRead(int notificationId) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE notification SET is_read = '1' WHERE notification_id = ?";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, notificationId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("알림 읽음 처리 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps);
        }
    }

    @Override
    public int updateNotification(Notification notification) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE notification SET message = ? WHERE notification_id = ?";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, notification.getMessage());
            ps.setInt(2, notification.getNotificationId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("알림 수정 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps);
        }
    }

    @Override
    public int deleteNotification(int notificationId) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM notification WHERE notification_id = ?";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, notificationId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("알림 삭제 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps);
        }
    }

    @Override
    public int deleteNotificationsByUserId(String userId) throws DatabaseException {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM notification WHERE user_id = ?";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("사용자 알림 전체 삭제 중 오류 발생: " + e.getMessage());
        } finally {
            DBUtil.close(con, ps);
        }
    }
}
