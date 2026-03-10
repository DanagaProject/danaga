package dto;

/**
 * 알림 DTO
 */
public class Notification {
    private int notificationId;
    private String userId;
    private String message;
    private boolean isRead;
    private String createdAt;

    // 기본 생성자
    public Notification() {
    }

    // 등록용 생성자 (ID, isRead, createdAt 제외)
    public Notification(String userId, String message) {
        this.userId = userId;
        this.message = message;
        this.isRead = false;
    }

    // 전체 필드 생성자
    public Notification(int notificationId, String userId, String message,
                        boolean isRead, String createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}
