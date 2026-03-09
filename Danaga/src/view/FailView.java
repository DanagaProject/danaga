package view;

/**
 * 실패 메시지 출력 View
 */
public class FailView {

    public static void errorMessageDisplay(String message) {
        System.err.println("\n[오류] " + message);
    }

    public static void printError(String message) {
        System.err.println("✗ " + message);
    }

    public static void exceptionDisplay(Exception e) {
        System.err.println("\n[예외 발생] " + e.getMessage());
    }
}
