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
}
