package service;

import java.sql.SQLException;
import java.util.List;

import dao.OrdersDAO;
import dao.OrdersDAOImpl;
import dto.Orders;
import exception.DatabaseException;

public class OrdersServiceImpl implements OrdersService {
	private OrdersDAO ordersDAO = new OrdersDAOImpl();
	
	@Override
    public int ordersInsert(Orders orders) throws SQLException {
        // 비즈니스 로직 추가 가능 (예: 본인 상품 구매 불가 체크 등)
        if (orders.getBuyerId() == null || orders.getProductId() <= 0) {
            throw new SQLException("주문 정보가 올바르지 않습니다.");
        }

        int result = ordersDAO.ordersInsert(orders);
        
        if (result == -1) {
            throw new SQLException("주문 처리 중 오류가 발생했습니다.");
        }
        
        return result;
    }

    @Override
    public List<Orders> selectOrdersByUserId(String userId) throws SQLException {
        if (userId == null || userId.isEmpty()) {
            throw new SQLException("사용자 아이디가 유효하지 않습니다.");
        }
        return ordersDAO.selectordersByUserId(userId);
    }
    
    @Override
    public void confirmTrade(int orderId) throws SQLException {
        Orders order = ordersDAO.selectOrderById(orderId); 
        
        if (order == null) {
            throw new SQLException("존재하지 않는 주문입니다.");
        }
        
        int result = ordersDAO.confirmTrade(order);
        
        if (result == 0) {
            throw new SQLException("구매 확정 처리 중 오류가 발생했습니다.");
        }
    }
    
    public void startDelivery(int orderId) throws SQLException {
    	int result = ordersDAO.startDelivery(orderId);
    	if (result==0) {
            throw new SQLException("배송 처리 실패");
        } 
    }
    
    @Override
    public void cancleRequest(int orderId) throws SQLException {
        int result = ordersDAO.cancleRequest(orderId);
        if (result == 0) {
            throw new SQLException("취소 요청 처리 실패");
        }
    }
    
    @Override
    public void cancelComplete(int orderId) throws SQLException {
        
        Orders order = ordersDAO.selectOrderById(orderId); 
        
        if (order == null) {
            throw new SQLException("존재하지 않는 주문입니다.");
        }


        if (order.getStatusId() == 9) { 
            throw new SQLException("이미 구매 확정된 주문은 취소할 수 없습니다.");
        }


        int result = ordersDAO.cancelComplete(order);
        
        if (result == 0) {
            throw new SQLException("취소 완료 처리 중 오류가 발생했습니다.");
        }
    }
    
    @Override
    public void adminForceCancel(int orderId, String userRole) throws SQLException {

        if (userRole == null || !userRole.equalsIgnoreCase("ADMIN")) {
            throw new SQLException("접근 권한이 없습니다. 관리자만 강제 취소를 집행할 수 있습니다.");
        }


        Orders order = ordersDAO.selectOrderById(orderId); 
        
        if (order == null) {
            throw new SQLException("존재하지 않는 주문입니다.");
        }

        
        if (order.getStatusId() == 9) { 
            throw new SQLException("이미 최종 구매 확정된 주문은 강제 취소할 수 없습니다.");
        }

        
        int result = ordersDAO.cancelComplete(order);
        
        if (result == 0) {
            throw new SQLException("관리자 강제 취소 처리 중 오류가 발생했습니다.");
        }
    }
    
    @Override
    public void adminForceTrade(int orderId, String userRole) throws SQLException {
        
        if (userRole == null || !userRole.equalsIgnoreCase("ADMIN")) {
            throw new SQLException("접근 권한이 없습니다. 관리자만 분쟁 조정을 집행할 수 있습니다.");
        }

        
        Orders order = ordersDAO.selectOrderById(orderId); 
        
        if (order == null) {
            throw new SQLException("존재하지 않는 주문입니다.");
        }

        
        if (order.getStatusId() != 6) { 
            throw new SQLException("취소 요청 상태인 주문만 강제 확정(분쟁 조정)을 진행할 수 있습니다.");
        }

        
        int result = ordersDAO.confirmTrade(order);
        
        if (result == 0) {
            throw new SQLException("강제 구매 확정 처리 중 오류가 발생했습니다.");
        }
    }
    
    @Override
    public List<Orders> getSalesBySellerId(String sellerId) throws SQLException {
        if (sellerId == null || sellerId.isEmpty()) {
            throw new SQLException("판매자 정보가 유효하지 않습니다.");
        }
        return ordersDAO.selectSalesBySellerId(sellerId);
    }
}
