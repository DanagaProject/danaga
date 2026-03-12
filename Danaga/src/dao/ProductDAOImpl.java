package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Category;
import dto.Product;
import dto.User;
import exception.CategoryNotFoundException;
import exception.DatabaseException;
import exception.ProductNotFoundException;
import util.DBUtil;

public class ProductDAOImpl implements ProductDAO {
	
	ProductDAO productDAO = new ProductDAOImpl();

	@Override
	public List<Product> productSelectAll() throws ProductNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<>();
		String sql = "select * from product";
	
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
			    int itemCondition=rs.getInt(7);
			    String createdAt=rs.getString(8);
			    String updatedAt=rs.getString(9);
			    int status=rs.getInt(10);

				list.add(new Product(productId, sellerId, categoryId, title, price, itemCondition ));
			}
		}catch(SQLException e){
			throw new ProductNotFoundException("");
		}finally {
			DBUtil.close(con, ps, rs);
		}
		
		return list;
	}

	@Override
	public List<Product> productSelectByCategory(int categoryId) throws ProductNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<>();
		String sql = "";
	
		return list;
	}

	@Override
	public Product productSelectByName(String keyword) throws ProductNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		Product product = null;
		String sql = "";
		
//		int productId = rs.getInt(1);
//		int sellerId = rs.getInt(2);
//		int categoryId = rs.getInt(3); //카테고리 이름 필요
//		String title = rs.getString(4);
//		int price = rs.getInt(5);
//		String description = rs.getString(6);
//		String itemCondition = rs.getString(7); //상태 이름 필요
		return product;
	}

	@Override
	public int productInsert(Product product) throws DatabaseException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "";
		
		return 0;
	}

	@Override
	public int productUpdate(Product product) throws ProductNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "";

		return 0;
	}

	@Override
	public int productDelete(int productId) throws ProductNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "";

		return 0;
	}

	@Override
	public List<Category> categorySelectAll() throws CategoryNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		List<Category> list = new ArrayList<>();
		String sql = "";

		return list;
	}

	@Override
	public int categoryInsert(Product product) throws DatabaseException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "";

		return 0;
	}

	@Override
	public int categoryDelete(int categoryId) throws CategoryNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		String sql = "";

		return 0;
	}

	@Override
	public List<Category> favCategorySeletAll() throws CategoryNotFoundException {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		List<Category> list = new ArrayList<>();
		String sql = "";
		
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
	public String categoryName(int categoryId) {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		String sql = "select name from category where category_id = ? ";
		String result = null;
		
		try {
			
		}/*catch(SQLException e) {
			throw new 
		}*/finally {
			DBUtil.close(con, ps, rs);
		}
		
		return null;
	}
	
	/**
	 * 상태명 가져오기
	 * */
	public String statusName(int status) {
		return null;
	}
	
	/**
	 * 품질명가져오기
	 */
	public String conditionName(int itemCondition) {
		return null;
	}
	
}
