package controller;

import dto.Notification;
import exception.DatabaseException;
import service.NotificationService;
import service.NotificationServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 알림 Controller
 * - View와 NotificationService 사이의 중간 계층
 * - 예외 처리 후 안전한 값 반환
 */
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController() {
        this.notificationService = NotificationServiceImpl.getInstance();
    }

    /**
     * 사용자의 전체 알림 목록 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 알림 목록 (오류 시 빈 리스트)
     */
    public List<Notification> getNotifications(String userId) {
        try {
            return notificationService.getNotificationsByUserId(userId);
        } catch (DatabaseException e) {
            System.out.println("[알림] 알림 목록 조회 중 오류 발생: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 사용자의 안읽은 알림 목록 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 안읽은 알림 목록 (오류 시 빈 리스트)
     */
    public List<Notification> getUnreadNotifications(String userId) {
        try {
            return notificationService.getUnreadNotificationsByUserId(userId);
        } catch (DatabaseException e) {
            System.out.println("[알림] 안읽은 알림 조회 중 오류 발생: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 사용자의 안읽은 알림 개수 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 안읽은 알림 개수 (오류 시 0)
     */
    public int getUnreadCount(String userId) {
        try {
            return notificationService.getUnreadNotificationsByUserId(userId).size();
        } catch (DatabaseException e) {
            return 0;
        }
    }

    /**
     * 알림 읽음 처리
     *
     * @param notificationId 읽음 처리할 알림 ID
     * @return 성공 여부
     */
    public boolean markAsRead(int notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return true;
        } catch (DatabaseException e) {
            System.out.println("[알림] 읽음 처리 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    /**
     * 알림 삭제
     *
     * @param notificationId 삭제할 알림 ID
     * @return 성공 여부
     */
    public boolean deleteNotification(int notificationId) {
        try {
            notificationService.deleteNotification(notificationId);
            return true;
        } catch (DatabaseException e) {
            System.out.println("[알림] 알림 삭제 중 오류 발생: " + e.getMessage());
            return false;
        }
    }
}
