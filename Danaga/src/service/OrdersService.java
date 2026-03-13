package service;

import exception.DatabaseException;

import java.sql.SQLException;
import java.util.List;

import dto.Orders;

public interface OrdersService {
    
    // 1. 주문 추가
    int ordersInsert(Orders orders) throws SQLException;
    
    // 2. 사용자별 주문 조회
    List<Orders> selectOrdersByUserId(String userId) throws SQLException;

    // 3. 구매 확정
    void confirmTrade(int orderId) throws SQLException;

    // 4. 배송 시작
    void startDelivery(int orderId) throws SQLException;

    // 5. 취소 요청
    void cancleRequest(int orderId) throws SQLException;
}
//
//    /**
//     * 거래 취소 동의 및 거절 - 판매자
//     * 동의(isAgree=true) 시 환불 처리 및 상품 상태 롤백, 거절 시 CANCEL_REJECTED 상태로 변경합니다.
//     */
//    void replyCancelRequest(int orderId, boolean isAgree, String buyerId, int productId, int price) throws DatabaseException;
//
//    /**
//     * 거래 취소 동의(강제 집행) - 관리자
//     * CANCEL_REJECTED 상태의 거래를 관리자 직권으로 환불 처리합니다.
//     */
//    void adminForceCancel(int orderId, String buyerId, int productId, int price) throws DatabaseException;
