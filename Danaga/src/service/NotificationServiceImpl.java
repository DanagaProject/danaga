package service;

import dao.NotificationDAO;
import dao.NotificationDAOImpl;
import dto.Notification;
import exception.DatabaseException;
import java.util.List;

/**
 * 알림 서비스 구현체
 */
public class NotificationServiceImpl implements NotificationService {
    private static final NotificationService instance = new NotificationServiceImpl();
    private final NotificationDAO notificationDAO;

    private NotificationServiceImpl() {
        this.notificationDAO = new NotificationDAOImpl();
    }

    public static NotificationService getInstance() {
        return instance;
    }

    @Override
    public void addNotification(Notification notification) throws DatabaseException {
        int result = notificationDAO.insertNotification(notification);
        if (result == 0) {
            throw new DatabaseException("알림 등록에 실패했습니다.");
        }
    }

    @Override
    public Notification getNotificationById(int notificationId) throws DatabaseException {
        return notificationDAO.selectNotificationById(notificationId);
    }

    @Override
    public List<Notification> getNotificationsByUserId(String userId) throws DatabaseException {
        return notificationDAO.selectNotificationsByUserId(userId);
    }

    @Override
    public List<Notification> getUnreadNotificationsByUserId(String userId) throws DatabaseException {
        return notificationDAO.selectUnreadNotificationsByUserId(userId);
    }

    @Override
    public void markAsRead(int notificationId) throws DatabaseException {
        int result = notificationDAO.markAsRead(notificationId);
        if (result == 0) {
            throw new DatabaseException("알림 읽음 처리에 실패했습니다.");
        }
    }

    @Override
    public void updateNotification(Notification notification) throws DatabaseException {
        int result = notificationDAO.updateNotification(notification);
        if (result == 0) {
            throw new DatabaseException("알림 수정에 실패했습니다.");
        }
    }

    @Override
    public void deleteNotification(int notificationId) throws DatabaseException {
        int result = notificationDAO.deleteNotification(notificationId);
        if (result == 0) {
            throw new DatabaseException("알림 삭제에 실패했습니다.");
        }
    }

    @Override
    public void deleteNotificationsByUserId(String userId) throws DatabaseException {
        notificationDAO.deleteNotificationsByUserId(userId);
        // 삭제 개수가 0이어도 에러로 처리하지 않음 (알림이 없을 수 있음)
    }
}
