package dto;

/**
 * 상품 판매 상태
 */
public enum ProductStatus {
    ON_SALE,    // 판매중
    RESERVED,   // 예약중 (구매 완료, 거래 진행중)
    COMPLETED   // 거래 완료
}
