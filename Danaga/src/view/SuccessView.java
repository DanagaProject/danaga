package view;

/**
 * 성공 메시지 출력 View
 */
public class SuccessView {

    public static void messageDisplay(String message) {
        System.out.println("\n[성공] " + message);
    }

    public static void printSuccess(String message) {
        System.out.println("✓ " + message);
    }

    public static void printMessage(String message) {
        System.out.println("\n[성공] " + message);
    }

    public static void printLoginSuccess(dto.User user) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  로그인 성공!");
        System.out.println("  환영합니다, " + user.getUserId() + "님!");
        System.out.println("  잔액: " + user.getBalance() + "원");
        System.out.println("  권한: " + ("ADMIN".equals(user.getRole()) ? "관리자" : "일반 회원"));
        System.out.println("════════════════════════════════════════");
    }
}
