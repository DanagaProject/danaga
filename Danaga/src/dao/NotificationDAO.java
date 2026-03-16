package dao;

import dto.Notification;
import exception.DatabaseException;
import java.util.List;

/**
 * 알림 DAO 인터페이스
 */
public interface NotificationDAO {
    /**
     * 알림 등록
     */
    int insertNotification(Notification notification) throws DatabaseException;

    /**
     * 알림 ID로 조회
     */
    Notification selectNotificationById(int notificationId) throws DatabaseException;

    /**
     * 사용자별 알림 목록 조회
     */
    List<Notification> selectNotificationsByUserId(String userId) throws DatabaseException;

    /**
     * 사용자별 안 읽은 알림 목록 조회
     */
    List<Notification> selectUnreadNotificationsByUserId(String userId) throws DatabaseException;

    /**
     * 알림 읽음 처리
     */
    int markAsRead(int notificationId) throws DatabaseException;

    /**
     * 알림 수정
     */
    int updateNotification(Notification notification) throws DatabaseException;

    /**
     * 알림 삭제
     */
    int deleteNotification(int notificationId) throws DatabaseException;

    /**
     * 사용자의 모든 알림 삭제
     */
    int deleteNotificationsByUserId(String userId) throws DatabaseException;
}
