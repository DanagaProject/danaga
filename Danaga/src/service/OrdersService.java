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
    
    // 6. 취소 완료 및 환불
    void cancelComplete(int orderId) throws SQLException;
    
    // 7. 관리자 직권 강제 취소 및 환불 집행
    void adminForceCancel(int orderId, String userRole) throws SQLException;
    
    // 8. 취소 거절 및 강제 구매 확정 (먹튀 분쟁 조정 - 관리자용)
    void adminForceTrade(int orderId, String userRole) throws SQLException;
    
    List<Orders> getSalesBySellerId(String sellerId) throws java.sql.SQLException;
}
