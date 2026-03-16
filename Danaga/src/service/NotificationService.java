package service;

import dto.Notification;
import exception.DatabaseException;
import java.util.List;

/**
 * 알림 서비스 인터페이스
 */
public interface NotificationService {
    /**
     * 알림 등록
     */
    void addNotification(Notification notification) throws DatabaseException;

    /**
     * 알림 ID로 조회
     */
    Notification getNotificationById(int notificationId) throws DatabaseException;

    /**
     * 사용자별 알림 목록 조회
     */
    List<Notification> getNotificationsByUserId(String userId) throws DatabaseException;

    /**
     * 사용자별 안 읽은 알림 목록 조회
     */
    List<Notification> getUnreadNotificationsByUserId(String userId) throws DatabaseException;

    /**
     * 알림 읽음 처리
     */
    void markAsRead(int notificationId) throws DatabaseException;

    /**
     * 알림 수정
     */
    void updateNotification(Notification notification) throws DatabaseException;

    /**
     * 알림 삭제
     */
    void deleteNotification(int notificationId) throws DatabaseException;

    /**
     * 사용자의 모든 알림 삭제
     */
    void deleteNotificationsByUserId(String userId) throws DatabaseException;
}
