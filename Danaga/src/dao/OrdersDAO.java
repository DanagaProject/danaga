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
}