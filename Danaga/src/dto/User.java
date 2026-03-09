package dto;

import java.io.Serializable;

/**
 * 회원 DTO
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String password;
    private int balance;
    private UserStatus status;
    private UserRole role;

    // 기본 생성자
    public User() {
    }

    // 회원가입용 생성자
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.balance = 0;
        this.status = UserStatus.ACTIVE;
        this.role = UserRole.USER;
    }

    // 전체 필드 생성자
    public User(String userId, String password, int balance, UserStatus status, UserRole role) {
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
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
