package exception;

/**
 * 중복된 회원 ID 예외
 */
public class DuplicateUserException extends Exception {
    public DuplicateUserException(String message) {
        super(message);
    }
}
