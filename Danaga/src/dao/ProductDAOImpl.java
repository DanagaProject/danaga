package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Category;
import dto.FavoriteCategory;
import dto.Product;
import dto.User;
import exception.CategoryNotFoundException;
import exception.DatabaseException;
import exception.ProductNotFoundException;
import util.DBUtil;

public class ProductDAOImpl implements ProductDAO {
	
	ProductDAO productDAO = new ProductDAOImpl();

	@Override
	public List<Product> productSelectAll() throws ProductNotFoundException, DatabaseException {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<>();
		Product product = null;
		String sql = "select * from product where is_deleted = 'n' order by product_id";
	
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int productId=rs.getInt(1); 
			    String sellerId=rs.getString(2);
			    int categoryId=rs.getInt(3);
			    String title=rs.getString(4);
			    int price=rs.getInt(5);
			    String description=rs.getString(6);
			    int conditionId=rs.getInt(7);
			    String createdAt=rs.getString(8);
			    int statusId=rs.getInt(10);
			    
				product = new Product(productId, sellerId, categoryId, title, 
									price, description, conditionId, statusId, createdAt);
				
				//이름 가져오기
				String categoryName = this.categoryNameSelect(con, categoryId);
				product.setCategoryName(categoryName);
				String status = this.statusNameSelect(con, statusId);
				product.setStatus(status);
				String itemCondition = this.conditionNameSelect(con, conditionId);
				product.setItemCondition(itemCondition);
				
				list.add(product);
			}
		}catch(SQLException e){
			throw new ProductNotFoundException("상품 목록을 찾지 못했습니다.");
		}finally {
			DBUtil.close(con, ps, rs);
		}
		
		return list;
	}

	@Override
	public List<Product> productSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		Product product = null;
		List<Product> list = new ArrayList<>();
		String sql = "select * from product where category_id = ?, is_deleted = 'n' order by product_id";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setInt(1, categoryId);
			
			rs = ps.executeQuery();
			while(rs.next()) {
				int productId=rs.getInt(1); 
			    String sellerId=rs.getString(2);
			    String title=rs.getString(4);
			    int price=rs.getInt(5);
			    String description=rs.getString(6);
			    int conditionId=rs.getInt(7);
			    String createdAt=rs.getString(8);
			    int statusId=rs.getInt(10);
			    
				product = new Product(productId, sellerId, categoryId, title, 
									price, description, conditionId, statusId, createdAt);
				
				//이름 가져오기
				String categoryName = this.categoryNameSelect(con, categoryId);
				product.setCategoryName(categoryName);
				String status = this.statusNameSelect(con, statusId);
				product.setStatus(status);
				String itemCondition = this.conditionNameSelect(con, conditionId);
				product.setItemCondition(itemCondition);
				
				list.add(product);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			throw new ProductNotFoundException("해당하는 카테고리의 상품 목록을 찾지 못했습니다.");
		}finally {
			DBUtil.close(con,ps,rs);
		}
		
		return list;
	}

	@Override
	public Product productSelectByName(String keyword) throws ProductNotFoundException, DatabaseException {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		Product product = null;
		String sql = "select * from product where title like ?, is_deleted = 'n' order by product_id";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setString(1, "%"+keyword+"%");
			
			rs = ps.executeQuery();
			if(rs.next()) {
				int productId=rs.getInt(1); 
			    String sellerId=rs.getString(2);
			    int categoryId=rs.getInt(3);
			    String title=rs.getString(4);
			    int price=rs.getInt(5);
			    String description=rs.getString(6);
			    int conditionId=rs.getInt(7);
			    String createdAt=rs.getString(8);
			    int statusId=rs.getInt(10);
			    
				product = new Product(productId, sellerId, categoryId, title, 
									price, description, conditionId, statusId, createdAt);
				
				//이름 가져오기
				String categoryName = this.categoryNameSelect(con, categoryId);
				product.setCategoryName(categoryName);
				String status = this.statusNameSelect(con, statusId);
				product.setStatus(status);
				String itemCondition = this.conditionNameSelect(con, conditionId);
				product.setItemCondition(itemCondition);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			throw new ProductNotFoundException("해당하는 카테고리의 상품 목록을 찾지 못했습니다.");
		}finally {
			DBUtil.close(con,ps,rs);
		}
		
		return product;
	}

	@Override
	public int productInsert(Product product) throws DatabaseException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "insert into product(seller_id, category_id, title, price, condition_id, description) "
				+ "values( ?, ?, ?, ?, ?, ?)";
		int result = 0;
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setString(1, product.getSellerId());
			ps.setInt(2, product.getCategoryId());
			ps.setString(3, product.getTitle());
			ps.setInt(4, product.getPrice());
			ps.setInt(5, product.getConditionId());
			ps.setString(6, product.getDescription());
			
			result = ps.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("상품을 등록할 수 없습니다.");
		}finally {
			DBUtil.close(con, ps);
		}
		
		return result;
	}

	@Override
	public int productUpdate(Product product) throws ProductNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		StringBuilder sql = new StringBuilder("update product set ");
	    List<Object> params = new ArrayList<>();
		int result = 0;
		
		//각 필드가 null이거나 기본값이 아닐 때만 쿼리에 추가
		if(product.getCategoryId() > 0) {
	    	sql.append("category_id = ?, ");
	    	params.add(product.getCategoryId());
	    }
		if(product.getTitle() != null) {
	        sql.append("title = ?, ");
	        params.add(product.getTitle());
	    }
	    if(product.getPrice() > 0) {
	        sql.append("price = ?, ");
	        params.add(product.getPrice());
	    }
	    if(product.getConditionId() > 0) {
	    	sql.append("condition_id = ?, ");
	    	params.add(product.getConditionId());
	    }
	    if(product.getDescription() != null) {
	        sql.append("description = ?, ");
	        params.add(product.getDescription());
	    }
	    
	    //마지막 쉼표(,)/ ", " 제거
	    sql.setLength(sql.length() - 2); 
	    //WHERE 절 추가
	    sql.append(" WHERE product_id = ?");
	    params.add(product.getProductId());
	    
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql.toString());
			
			// 리스트에 담긴 파라미터를 순서대로 세팅
	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }
	        
			result = ps.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new ProductNotFoundException("해당하는 번호의 상품을 찾을 수 없습니다.");
		}finally {
			DBUtil.close(con, ps);
		}
		
		return result;
	}

	@Override
	public int productDelete(int productId) throws ProductNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "delete from product where product_id = ?";
		int result = 0;
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, productId);
			
			result = ps.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new ProductNotFoundException("해당하는 번호의 상품을 찾을 수 없습니다.");
		}finally {
			DBUtil.close(con, ps);
		}
		
		return result;
	}

	@Override
	public List<Category> categorySelectAll() throws CategoryNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		List<Category> list = new ArrayList<>();
		String sql = "select * from category";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int categoryId = rs.getInt(1);
				String name = rs.getString(2);
				
				Category category = new Category(categoryId, name);
				
				list.add(category);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new CategoryNotFoundException("카테고리를 찾을 수 없습니다.");
		}finally {
			DBUtil.close(con, ps, rs);
		}
		
		return list;
	}

	@Override
	public int categoryInsert(Category category) throws DatabaseException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "insert into category(name) values(?)";
		int result = 0;
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, category.getName());
			
			result = ps.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("카테고리를 추가할 수 없습니다.");
		}finally {
			DBUtil.close(con, ps);
		}
		
		return result;
	}
	
	@Override
	public int categoryUpdate(Category category) throws CategoryNotFoundException{
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "update category set name where category_id = ?";
		int result = 0;
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, category.getCategoryId());
			
			result = ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			throw new CategoryNotFoundException("해당하는 번호의 카테고리가 없습니다.");
		}finally {
			DBUtil.close(con, ps);
		}
		
		return result;
	}

	@Override
	public int categoryDelete(int categoryId) throws CategoryNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "delete from category where category_id = ?";
		int result = 0;
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, categoryId);
			
			result = ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			throw new CategoryNotFoundException("해당하는 번호의 카테고리가 없습니다.");
		}finally {
			DBUtil.close(con, ps);
		}
		
		return result;
	}

	@Override
	public List<FavoriteCategory> favCategorySeletAll() throws CategoryNotFoundException, DatabaseException {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		FavoriteCategory favoriteCategory = null;
		List<FavoriteCategory> list = new ArrayList<>();
		String sql = "select * from favorite_category order by category_id";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {				
				String userId = rs.getString(2);
				int categoryId = rs.getInt(3);
				
				favoriteCategory = new FavoriteCategory(userId, categoryId);
				
				//이름 가져오기
				String categoryName = this.categoryNameSelect(con, categoryId);
				favoriteCategory.setCategoryName(categoryName);				
				
				list.add(favoriteCategory);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			throw new CategoryNotFoundException("해당하는 카테고리가 없습니다.");
		}finally {
			DBUtil.close(con,ps,rs);
		}
		
		return list;
	}

	@Override
	public int favCategoryUpdate(User currentUser) throws CategoryNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "";

		return 0;
	}

	@Override
	public int favCategoryDelete(User currentUser) throws CategoryNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "";

		return 0;
	}
	
	/**
	 * 카테고리 이름 가져오기
	 * */
	public String categoryNameSelect(Connection con, int categoryId) throws DatabaseException{
		PreparedStatement ps=null;
		ResultSet rs = null;
		String sql = "select name from category where category_id = ? ";
		String categoryName = null;
		
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, categoryId);
			rs = ps.executeQuery();
			
			categoryName = rs.getString(1);
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("해당하는 카데고리가 없습니다.");
		}finally {
			DBUtil.close(null, ps, rs);
		}
		return categoryName;
	}
	
	/**
	 * 상태명 가져오기
	 * */
	public String statusNameSelect(Connection con, int statusId) throws DatabaseException {
		PreparedStatement ps=null;
		ResultSet rs = null;
		String sql = "select name from code where code_id = ? ";
		String status = null;
		
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, statusId);
			rs = ps.executeQuery();
			
			status = rs.getString(1);
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("해당하는 상태가 없습니다.");
		}finally {
			DBUtil.close(null, ps, rs);
		}
		return status;
	}
	
	/**
	 * 품질명 가져오기
	 */
	public String conditionNameSelect(Connection con, int conditionId) throws DatabaseException {
		PreparedStatement ps=null;
		ResultSet rs = null;
		String sql = "select name from code where code_id = ? ";
		String itemCondition = null;
		
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, conditionId);
			rs = ps.executeQuery();
			
			itemCondition = rs.getString(1);
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("해당하는 품질이 없습니다.");
		}finally {
			DBUtil.close(null, ps, rs);
		}
		return itemCondition;
	}
	
}
