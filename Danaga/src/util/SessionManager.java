package util;

import dto.User;
import dto.UserRole;

/**
 * 로그인 세션 관리
 */
public class SessionManager {
    private static User currentUser = null;

    /**
     * 로그인
     */
    public static void login(User user) {
        currentUser = user;
        System.out.println("[SessionManager] 로그인: " + user.getUserId());
    }

    /**
     * 로그아웃
     */
    public static void logout() {
        if (currentUser != null) {
            System.out.println("[SessionManager] 로그아웃: " + currentUser.getUserId());
            currentUser = null;
        }
    }

    /**
     * 로그인 여부 확인
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * 현재 로그인한 사용자 가져오기
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * 관리자 여부 확인
     */
    public static boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == UserRole.ADMIN;
    }

    /**
     * 현재 사용자 ID 가져오기
     */
    public static String getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : null;
    }
}
