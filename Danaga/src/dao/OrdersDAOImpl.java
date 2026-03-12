package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import dto.Orders;
import util.DBUtil;

public class OrdersDAOImpl implements OrdersDAO {

    @Override
    public int ordersInsert(Orders orders) throws SQLException {
        Connection con = null;
        int result = 0;

        try {
            // 1. Connection 생성 및 트랜잭션 시작
            con = DBUtil.getConnection();
            con.setAutoCommit(false); 

            // 2. 내부 private 메소드들을 호출하여 비즈니스 로직 수행 (동일한 con 공유)
            int price = selectProductPrice(con, orders.getProductId());
            
            int balance = selectUserBalance(con, orders.getBuyerId());
            if (balance < price) {
                throw new SQLException("잔액이 부족합니다. (현재 잔액: " + balance + "원)");
            }

            updateUserBalance(con, orders.getBuyerId(), -price);
            
            result = insertordersTable(con, orders); // 생성된 orders_id를 반환받을 수도 있음
            
            updateProductStatus(con, orders.getProductId(), 11);
            
            //insertNotification(con, orders.getBuyerId(), "상품 구매 신청이 완료되었습니다.");

            // 3. 모든 작업 성공 시 커밋
            con.commit();

        } catch (SQLException e) {
            // 실패 시 롤백
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
            DBUtil.close(con, null, null);
        }

        return result;
    }

   

	@Override
    public List<Orders> selectordersByUserId(String userId) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Orders> list = new ArrayList<>();

        try {
            con = DBUtil.getConnection();
            String sql = "SELECT * FROM orders WHERE buyer_id = ? " + "ORDER BY orders_id DESC";
                         
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Orders orders = new Orders();
                orders.setOrdersId(rs.getInt("orders_id"));
                orders.setProductId(rs.getInt("product_id"));
                orders.setBuyerId(rs.getString("buyer_id"));
                orders.setStatusId(rs.getInt("status_id"));
                orders.setCreatedAt(rs.getString("created_at"));
                
                list.add(orders);
            }
        } finally {
            DBUtil.close(con, ps, rs);
        }
        return list;
    }
////////////////////////////////////////////////////////////////////////////////////
	private void updateUserBalance(Connection con, String userId, int amount) throws SQLException {
	    // amount가 음수면 차감, 양수면 증액이 되므로 쿼리는 하나로 충분합니다.
	    String sql = "UPDATE user SET balance = balance + ? WHERE user_id = ?";
	    
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, amount);
	        ps.setString(2, userId);
	        
	        int result = ps.executeUpdate();
	        
	        // 중요: UserDAOImpl처럼 업데이트된 행이 없는 경우 예외 처리
	        if (result == 0) {
	            throw new SQLException("잔액 수정 실패: 사용자를 찾을 수 없습니다.");
	        }
	    }
	}
	
    public int selectProductPrice(Connection con, int productId) throws SQLException {
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

    public int selectUserBalance(Connection con, String userId) throws SQLException {
        String sql = "SELECT balance FROM user WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("balance");
                throw new SQLException("구매자 정보를 찾을 수 없습니다.");
            }
        }
    }

    

    public int insertordersTable(Connection con, Orders orders) throws SQLException {
        String sql = "INSERT INTO orders (product_id, buyer_id, status_id) VALUES (?, ?, 4)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, orders.getProductId());
            ps.setString(2, orders.getBuyerId());
            
            if (ps.executeUpdate() == 0) throw new SQLException("주문 등록에 실패했습니다.");
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        }
    }

    public void updateProductStatus(Connection con, int productId, int status_id) throws SQLException {
        String sql = "UPDATE product SET status_id = ? WHERE product_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, status_id);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    public void insertNotification(Connection con, String userId, String message) throws SQLException {
        String sql = "INSERT INTO notification (user_id, message) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();
        }
    }
}