package exception;

/**
 * 회원을 찾을 수 없음 예외
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
