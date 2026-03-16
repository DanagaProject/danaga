package dao;

import java.sql.SQLException;
import java.util.List;
import dto.Orders;

public interface OrdersDAO {

    /**
     * 다나가 중고상품 구매 신청 (내부에서 트랜잭션 처리)
     */
    int ordersInsert(Orders orders) throws SQLException;

    /**
     * 주문 내역 조회
     */
	List<Orders> selectordersByUserId(String userId) throws SQLException;
	
	/**
     * 특정 주문 번호로 1건 상세 조회 (정산 정보 포함)
     */
	Orders selectOrderById(int orderId) throws SQLException;
	
	/**
	 * 배송 완료
	 */
	int startDelivery(int orderId) throws SQLException;
	
	/**
	 * 취소요청
	 */
	int cancleRequest(int orderId) throws SQLException;
	/**
	 * 거래 완료
	 */
	int confirmTrade(Orders orders) throws SQLException;
	
	/**
	 * 취소완료 및 환불
	 */
	int cancelComplete(Orders order) throws SQLException;
	
	
}