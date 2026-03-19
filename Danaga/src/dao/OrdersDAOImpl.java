package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import dto.Orders;
import dto.User;
import util.DBUtil;

public class OrdersDAOImpl implements OrdersDAO {
	
	@Override
	public int ordersInsert(Orders order) throws SQLException {
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    
	    
	   
	    String sql = "INSERT INTO orders (product_id, buyer_id, status_id) VALUES (?, ?, 4)";
	    
	    int price = 0;
	    int balance = 0;
	    
	    int result = 0;

	    try {
	        con = DBUtil.getConnection();
	        con.setAutoCommit(false);

	        
	        price = this.selectProductPrice(con, order.getProductId());
	        balance = this.selectUserBalance(con, order.getBuyerId());

	        if (balance < price) {
	            throw new SQLException("잔액이 부족합니다. (현재 잔액: " + balance + "원)");
	        }

	        
	        this.updateUserBalance(con, order.getBuyerId(), -price);
	        this.updateUserBalance(con, "admin", price);
	       
	        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        ps.setInt(1, order.getProductId());
	        ps.setString(2, order.getBuyerId());

	        result = ps.executeUpdate();

	        if (result == 0) {
	            con.rollback();
	            throw new SQLException("주문 등록에 실패했습니다.");
	        } else {
	            
	            rs = ps.getGeneratedKeys();
	            int orderId = -1;
	            if (rs.next()) {
	                orderId = rs.getInt(1);
	            }
	            System.out.println("생성된 orderId = " + orderId);

	            
	            this.updateProductStatus(con, order.getProductId(), 11);

	            
	            this.insertNotification(con, order.getBuyerId(), "상품 구매 신청이 완료되었습니다.");

	            con.commit(); 
	        }

	    } catch (SQLException e) {
	        if (con != null) {
	            try {
	                con.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        throw e;
	    } finally {
	        // 자원 해제
	        DBUtil.close(con, ps, rs);
	    }

	    return result;
	}
	

/////////////////////////////// 
///    조회
	
    private Orders mapOrders(ResultSet rs) throws SQLException {
        Orders orders = new Orders();
        orders.setOrdersId(rs.getInt("orders_id"));
        orders.setProductId(rs.getInt("product_id"));
        orders.setBuyerId(rs.getString("buyer_id"));
        orders.setStatusId(rs.getInt("status_id"));
        
        
        try { orders.setCreatedAt(rs.getString("created_at")); } catch (SQLException e) {}
        
        orders.setProductTitle(rs.getString("title")); 
        orders.setProductPrice(rs.getInt("price"));
        orders.setSellerId(rs.getString("seller_id"));
        orders.setStatus(rs.getString("status_name"));
        return orders;
    }

    // 2. 관리자 전용: 취소 요청(6) 및 거절(8) 주문 조회 (새로운 SQL 추가)
    @Override
    public List<Orders> getOrdersForAdmin() throws SQLException {
        // ID 필터링 없이 상태 코드 6, 8만 IN 연산자로 조회
        String sql = getBaseSql() + " WHERE o.status_id IN (6, 8) ORDER BY o.created_at ASC";
        return executeQuery(sql, null);
    }

    // 3. 기존 공통 메서드 (구매자/판매자 조회용)
    public List<Orders> getOrdersListByCondition(String whereColumn, String id) throws SQLException {
        String sql = getBaseSql() + " WHERE " + whereColumn + " = ? ORDER BY o.orders_id DESC";
        return executeQuery(sql, id);
    }

    // 4. [보너스] 쿼리 실행 공통 로직 (executeQuery)
    private List<Orders> executeQuery(String sql, String param) throws SQLException {
        List<Orders> list = new java.util.ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (param != null) ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapOrders(rs));
            }
        }
        return list;
    }

    // 5. 기초 SQL 문 (반복되는 JOIN 문 통합)
    private String getBaseSql() {
        return "SELECT o.*, p.title, p.price, p.seller_id, c.name as status_name " +
               "FROM orders o " +
               "JOIN product p ON o.product_id = p.product_id " +
               "LEFT JOIN code c ON o.status_id = c.code_id";
    }

    // --- 인터페이스 구현부 (한 줄로 종결) ---

    @Override
    public List<Orders> selectordersByUserId(String userId) throws SQLException {
        return getOrdersListByCondition("o.buyer_id", userId);
    }

    @Override
    public List<Orders> selectSalesBySellerId(String sellerId) throws SQLException {
        return getOrdersListByCondition("p.seller_id", sellerId);
    }

    @Override
    public Orders selectOrderById(int orderId) throws SQLException {
        String sql = getBaseSql() + " WHERE o.orders_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapOrders(rs) : null;
            }
        }
    }

	
	public int startDelivery(int orderId) throws SQLException {
	    Connection con = null;
	    int result = 0;
	    try {
	        con = DBUtil.getConnection();
	        con.setAutoCommit(false);

	        // 1. 상태 변경 (도우미 메서드 활용 - 배송중 상태코드 전달)
	        result = this.updateOrdersStatus(con, orderId, 5); // 5: 배송중 예시
	        
	        // 2. 구매자에게 알림
	        String buyerId = this.selectBuyerIdByOrderId(con, orderId);
	        String message = "주문번호 [" + orderId + "]번 배송이 시작되었습니다!";
	        this.insertNotification(con, buyerId, message);

	        con.commit();
	    } catch (SQLException e) {
	        if (con != null) con.rollback();
	        throw e;
	    } finally {
	        DBUtil.close(con, null, null);
	    }
	    return result;
	}
	
	
	public int cancleRequest(int orderId) throws SQLException {
	    Connection con = null;
	    int result = 0;
	    try {
	        con = DBUtil.getConnection();
	        con.setAutoCommit(false);
	        
	        // 1. 판매자 ID 조회
	        String sellerId = this.selectSellerIdByOrderId(con, orderId);
	        if(sellerId == null) throw new SQLException("판매자 정보를 찾을 수 없습니다");
	        
	        // 2. 상태 변경 (도우미 메서드 호출 - con 전달!)
	        result = this.updateOrdersStatus(con, orderId, 6); // 6: 취소요청
	        
	        if(result == 0) throw new SQLException("주문상태 변경 실패");
	        
	        // 3. 알림 전송
	        String message = "주문 번호 [" + orderId + "]에 대한 주문 취소 요청이 들어왔습니다.";  
	        this.insertNotification(con, sellerId, message);
	        
	        con.commit();
	    } catch (SQLException e) {
	        if (con != null) con.rollback();
	        throw e;
	    } finally {
	        DBUtil.close(con, null, null);
	    }
	    return result;
	}
	
	
	public int confirmTrade(Orders orders) throws SQLException {
		Connection con = null;
	    PreparedStatement ps = null;
	    int result = 0;

	    try {
	        con = DBUtil.getConnection();
	        con.setAutoCommit(false); // 트랜잭션 시작
	        
	        
	        
	        
	        result = this.updateOrdersStatus(con, orders.getOrdersId(), 9);
	       
	        

	        if (result == 0) {
	            throw new SQLException("주문 상태 변경 실패: 해당 주문이 존재하지 않습니다.");
	        }

	        
	       
	        this.updateUserBalance(con, orders.getSellerId(), orders.getProductPrice());
	        this.updateUserBalance(con, "admin", -orders.getProductPrice());

	        
	        
	        this.updateProductStatus(con, orders.getProductId(), 12);

	        
	        this.insertNotification(con, orders.getSellerId(), 
	            "주문번호 [" + orders.getOrdersId() + "]의 정산이 완료되었습니다.");

	        con.commit(); // 최종 성공

	    } catch (SQLException e) {
	        if (con != null) try { con.rollback(); } catch (SQLException ex) {}
	        throw e;
	    } finally {
	        DBUtil.close(con, null, null);
	    }
	    return result;
	}
	
	@Override
    public int cancelComplete(Orders order) throws SQLException {
        Connection con = null;
        int result = 0;

        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            
            result = this.updateOrdersStatus(con, order.getOrdersId(), 7);
            if (result == 0) {
                throw new SQLException("주문 상태 변경 실패");
            }

            
            this.updateUserBalance(con, "admin", -order.getProductPrice());
            this.updateUserBalance(con, order.getBuyerId(), order.getProductPrice());

            
            this.updateProductStatus(con, order.getProductId(), 10);

            
            this.insertNotification(con, order.getBuyerId(), 
                "주문번호 [" + order.getOrdersId() + "] 취소 및 환불 처리가 완료되었습니다.");
            
            if (order.getSellerId() != null) {
                this.insertNotification(con, order.getSellerId(), 
                    "주문번호 [" + order.getOrdersId() + "] 거래가 최종 취소되었습니다.");
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) {}
            throw e;
        } finally {
            DBUtil.close(con, null, null);
        }

        return result;
    }
	public int rejectCancel(int orderId) throws SQLException {
		int result = 0; // 성공 여부를 담을 변수
		
	    try (Connection con = DBUtil.getConnection()) {
	        con.setAutoCommit(false); // 트랜잭션 시작
	        
	        try {
	            // 1. 주문 상태를 8(거절)로 변경 -> 관리자가 이 상태를 보고 중재함
	        	result = updateOrdersStatus(con, orderId, 8);
	            con.commit(); // 확정
	        } catch (SQLException e) {
	            con.rollback(); // 에러 시 되돌리기
	            throw e;
	        }
	    }
	    return result;
	}
	
	
////////////////////////////////////////////////////////////////////////////////////
	public void updateUserBalance(Connection con, String userId, int amount) throws SQLException {

	    String sql = "UPDATE user SET balance = balance + ? WHERE user_id = ?";
	    
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, amount);
	        ps.setString(2, userId);
	        
	        int result = ps.executeUpdate();
	        
	        
	        if (result == 0) {
	            throw new SQLException("잔액 수정 실패: 사용자를 찾을 수 없습니다.");
	        }
	    }
	}
	
    private int selectProductPrice(Connection con, int productId) throws SQLException {
        String sql = "SELECT price, status_id FROM product WHERE product_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getInt("status_id") != 10) {
                        throw new SQLException("현재 구매할 수 없는 상품입니다.");
                    }
                    return rs.getInt("price");
                }
                throw new SQLException("상품 정보를 찾을 수 없습니다.");
            }
        }
    }

    private int selectUserBalance(Connection con, String userId) throws SQLException {
        String sql = "SELECT balance FROM user WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("balance");
                throw new SQLException("구매자 정보를 찾을 수 없습니다.");
            }
        }
    }

    

    

    private void updateProductStatus(Connection con, int productId, int statusId) throws SQLException {
        String sql = "UPDATE product SET status_id = ? WHERE product_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, statusId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    private void insertNotification(Connection con, String userId, String message) throws SQLException {
        String sql = "INSERT INTO notification (user_id, message) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();
        }
    }   
    
    private String selectSellerIdByOrderId(Connection con, int orderId) throws SQLException {
    	String sql = "SELECT p.seller_id FROM orders o JOIN product p ON o.product_id = p.product_id WHERE o.orders_id = ?";
    				 
    	try (PreparedStatement ps = con.prepareStatement(sql)) {
    		ps.setInt(1, orderId);
    		try (ResultSet rs = ps.executeQuery()) {
    	            if (rs.next()) {
    	            	return rs.getString("seller_id");   
    	            }
    		}
    		
    	}
    	return null;
    		
    }
    private int updateOrdersStatus(Connection con, int orderId, int newStatusId) throws SQLException {
        String sql = "UPDATE orders SET status_id = ? WHERE orders_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, newStatusId);
            ps.setInt(2, orderId);
            return ps.executeUpdate();
        }
    }
    private String selectBuyerIdByOrderId(Connection con, int orderId) throws SQLException {
    	String sql = "SELECT buyer_id FROM orders WHERE orders_id = ?";
    				 
    	try (PreparedStatement ps = con.prepareStatement(sql)) {
    		ps.setInt(1, orderId);
    		try (ResultSet rs = ps.executeQuery()) {
    	            if (rs.next()) {
    	            	return rs.getString("buyer_id");   
    	            }
    		}
    		
    	}
    	return null;
    		
    }
    
    
    
}