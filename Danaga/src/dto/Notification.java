package dto;

/**
 * 알림 DTO
 */
public class Notification {
    private int notificationId;
    private String userId;
    private String message;
    private String isRead; // '0': 안읽음, '1': 읽음
    private String createdAt;

    // 기본 생성자
    public Notification() {
    }

    // 등록용 생성자 (ID, isRead, createdAt 제외)
    public Notification(String userId, String message) {
        this.userId = userId;
        this.message = message;
        this.isRead = "0"; // 기본값: 안읽음
    }

    // 전체 필드 생성자
    public Notification(int notificationId, String userId, String message,
                        String isRead, String createdAt) {
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

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
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
