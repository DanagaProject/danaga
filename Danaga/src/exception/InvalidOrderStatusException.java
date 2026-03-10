package exception;

/**
 * 잘못된 주문 상태 예외
 */
public class InvalidOrderStatusException extends Exception {
    public InvalidOrderStatusException(String message) {
        super(message);
    }
}
