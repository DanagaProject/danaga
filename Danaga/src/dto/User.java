package dto;

/**
 * 회원 DTO
 */
public class User {
    private String userId;
    private String password;
    private int balance;
    private String status;
    private String role;

    // 기본 생성자
    public User() {
    }

    // 회원가입용 생성자
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.balance = 0;
        this.status = "ACTIVE";
        this.role = "USER";
    }

    // 전체 필드 생성자
    public User(String userId, String password, int balance, String status, String role) {
        this.userId = userId;
        this.password = password;
        this.balance = balance;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", balance=" + balance +
                ", status=" + status +
                ", role=" + role +
                '}';
    }
}
