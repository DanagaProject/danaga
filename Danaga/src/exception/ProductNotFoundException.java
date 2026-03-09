package exception;

/**
 * 상품을 찾을 수 없음 예외
 */
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
