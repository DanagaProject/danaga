package service;

import exception.DatabaseException;

import dto.User;
import java.sql.SQLException;
import java.util.List;

import dto.Orders;

public interface OrdersService {
    
    // 1. 주문 추가
	int ordersInsert(Orders orders, User loginUser) throws SQLException;
    
    // 2. 사용자별 주문 조회
    List<Orders> selectOrdersByUserId(String userId) throws SQLException;

    // 3. 구매 확정
    void confirmTrade(int orderId) throws SQLException;

    // 4. 배송 시작
    void startDelivery(int orderId) throws SQLException;

    // 5. 취소 요청
    void cancleRequest(int orderId) throws SQLException;
    
    // 6. 취소 완료 및 환불
    void cancelComplete(int orderId, User loginUser) throws SQLException;
    
    // 7. 취소 거절
    int rejectCancel(int orderId) throws SQLException;
    // 8. 관리자 직권 강제 취소 및 환불 집행
    void adminForceCancel(int orderId, String userRole) throws SQLException;
    
    // 9. 취소 거절 및 강제 구매 확정 (먹튀 분쟁 조정 - 관리자용)
    void adminForceTrade(int orderId, String userRole) throws SQLException;
    
    List<Orders> getSalesBySellerId(String sellerId) throws SQLException;
    
    List<Orders> getOrdersForAdmin() throws SQLException;
}
