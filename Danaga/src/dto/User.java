package dto;

/**
 * 회원 DTO
 * status 값 매핑 (code 테이블):
 * - code_id 12 → name 'ACTIVE' (정상)
 * - code_id 13 → name 'BANNED' (차단)
 */
public class User {
    private String userId;
    private String password;
    private int balance;
    private int statusId; // DB 실제 컬럼
    private String role;

    // JOIN용 추가 필드
    private String status; // code 테이블과 JOIN해서 얻은 name 값 (ACTIVE/BANNED)

    // 기본 생성자
    public User() {
    }

    // 회원가입용 생성자
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.balance = 0;
        this.statusId = 12; // ACTIVE
        this.role = "USER";
    }

    // 전체 필드 생성자
    public User(String userId, String password, int balance, int statusId, String role) {
        this.userId = userId;
        this.password = password;
        this.balance = balance;
        this.statusId = statusId;
        this.role = role;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", balance=" + balance +
                ", statusId=" + statusId +
                ", status='" + status + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
