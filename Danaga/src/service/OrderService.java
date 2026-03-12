package service;

import exception.DatabaseException;

public interface OrderService {
	/**
	 * 구매자 잔액 차감, 상품 상태 변경, 주문 생성, 알림 발송을 트랜잭션으로 처리합니다.
	 */
	void requestPurchase(String buyerId, int productId) throws DatabaseException;
	
	/**
     * 상품 배송 완료 - 판매자
     * 거래 상태를 PENDING에서 SHIPPING으로 변경합니다.
     */
    void completeShipping(int orderId) throws DatabaseException;
    
    /**
     * 거래 확정 - 구매자
     * 거래/상품 상태를 COMPLETED로 변경하고 판매자에게 대금을 입금합니다.
     */
    void confirmTrade(int orderId, String sellerId, int productId, int price) throws DatabaseException;
    
    /**
     * 거래 취소 요청(환불) - 구매자
     * 거래 상태를 CANCEL_REQUEST로 변경합니다.
     */
    void requestCancel(int orderId) throws DatabaseException;

    /**
     * 거래 취소 동의 및 거절 - 판매자
     * 동의(isAgree=true) 시 환불 처리 및 상품 상태 롤백, 거절 시 CANCEL_REJECTED 상태로 변경합니다.
     */
    void replyCancelRequest(int orderId, boolean isAgree, String buyerId, int productId, int price) throws DatabaseException;

    /**
     * 거래 취소 동의(강제 집행) - 관리자
     * CANCEL_REJECTED 상태의 거래를 관리자 직권으로 환불 처리합니다.
     */
    void adminForceCancel(int orderId, String buyerId, int productId, int price) throws DatabaseException;
}
