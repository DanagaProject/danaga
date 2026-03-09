package dto;

/**
 * 주문 상태
 */
public enum OrderStatus {
    PENDING,            // 결제 완료 (배송 대기)
    SHIPPING,           // 배송중
    COMPLETED,          // 거래 완료 (구매 확정)
    CANCEL_REQUEST,     // 취소 요청
    CANCEL_COMPLETED    // 취소 완료
}
