package service;

import java.sql.SQLException;
import java.util.List;

import dao.OrdersDAO;
import dao.OrdersDAOImpl;
import dao.ProductDAO;
import dao.ProductDAOImpl;
import dto.Orders;
import dto.User;
import exception.DatabaseException;

public class OrdersServiceImpl implements OrdersService {
	private OrdersDAO ordersDAO = new OrdersDAOImpl();
	private ProductDAO productDAO = ProductDAOImpl.getInstance();
	
	// OrdersServiceImpl.java 내부에 구현

	@Override
	public int ordersInsert(Orders order, User loginUser) throws SQLException {
	    
	    // 1. 방금 보여주신 완벽한 DAO 로직을 실행하여 DB 업데이트!
	    int result = ordersDAO.ordersInsert(order);
	    
	    // ⭐ 2. DB 처리가 성공했다면 세션 잔액 동기화 ⭐
	    if (result > 0) {
	    	try {
                // 💡 두 가지 예외를 던지는 메서드이므로 try 안에 넣습니다.
                dto.Product product = productDAO.productSelectById(order.getProductId());
                
                int price = product.getPrice();
                loginUser.setBalance(loginUser.getBalance() - price);
	    	} catch (exception.ProductNotFoundException e) {
                System.out.println("\n[시스템 오류] 상품 정보를 찾을 수 없어 세션 잔액을 갱신하지 못했습니다.");
            } catch (exception.DatabaseException e) {
                System.out.println("\n[시스템 오류] DB 통신 중 오류가 발생하여 세션 잔액을 갱신하지 못했습니다.");
            }
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
    
    @Override
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
    public void cancelComplete(int orderId, User loginUser) throws SQLException {
        
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
        loginUser.setBalance(loginUser.getBalance() + order.getProductPrice());
    }
    @Override
    public int rejectCancel(int orderId) throws SQLException {
        // DAO를 통해 주문 상태를 8(판매자 거절)로 변경
        return ordersDAO.rejectCancel(orderId);
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
    
    @Override
    public List<Orders> getOrdersForAdmin() throws SQLException {
        // DAO에서 발생한 SQLException이 그대로 컨트롤러로 전달됩니다.
        return ordersDAO.getOrdersForAdmin();
    }
    
    
}
