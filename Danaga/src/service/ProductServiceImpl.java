package service;

import java.util.ArrayList;
import java.util.List;

import dao.ProductDAO;
import dao.ProductDAOImpl;
import dto.Category;
import dto.Product;
import exception.CategoryNotFoundException;
import exception.DatabaseException;
import exception.ProductNotFoundException;

public class ProductServiceImpl implements ProductService {
	private static ProductService instance = new ProductServiceImpl();
	private ProductDAO productDAO = ProductDAOImpl.getInstance();
	
	private ProductServiceImpl() {}
	public static ProductService getInstance() {
		return instance;
	}
	
	@Override
	public List<Product> productSelectAll() throws ProductNotFoundException, DatabaseException {
		List<Product> list = productDAO.productSelectAll();
		if(list.isEmpty())
			throw new ProductNotFoundException("상품 목록이 없습니다.");
		return list;
	}
	
	@Override
	public List<Product> productSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException, CategoryNotFoundException {
		List<Category> categoryList = null;
		List<Product> list = null;
		try{
			//카테고리목록 가져오기
			categoryList = productDAO.categorySelectAll();
			
			//사용자가 입력한 ID가 실제 존재하는지 동적으로 검사 (Stream API 활용)
		    boolean isValid = categoryList.stream()
		            .anyMatch(c -> c.getCategoryId() == categoryId);
		    
		    if (!isValid) {
	            // 존재하지 않는 번호라면 전용 예외를 던지거나 메시지 처리
	            throw new CategoryNotFoundException(categoryId + "번은 존재하지 않는 카테고리입니다.");
	        }
		    
		    //존재하는 번호라면 상품 목록 가져오기
		    list = productDAO.productSelectByCategory(categoryId);
		    if(list.isEmpty())
				throw new ProductNotFoundException("카테고리에 상품이 없습니다.");
		    
		}catch(DatabaseException e) {
			throw new DatabaseException(e.getMessage());
		}	   
		return list;
	}
	
	@Override
	public List<Product> productSelectByName(String keyword) throws ProductNotFoundException, DatabaseException {
		List<Product> list = productDAO.productSelectByName(keyword);
		if(list.isEmpty()) 
			throw new ProductNotFoundException("입력하신 이름의 상품이 없습니다.");
		return list;
	}
	
	@Override
	public int productInsert(Product product) throws DatabaseException {
		int result = productDAO.productInsert(product);
		if(result==0)
			throw new DatabaseException("상품이 등록되지 않았습니다.");
		return result;
	}
	
	@Override
	public int productUpdate(Product product) throws ProductNotFoundException {
		int result = productDAO.productUpdate(product);
		if(result==0)
			throw new ProductNotFoundException("입력하신 상품이 없습니다.");
		return result;
	}
	
	@Override
	public int productDelete(int productId) throws ProductNotFoundException {
		int result = productDAO.productDelete(productId);
		if(result==0)
			throw new ProductNotFoundException("수정할 상품이 없습니다.");
		return result;
	}
	
	@Override
	public List<Category> categorySelectAll() throws CategoryNotFoundException {
		List<Category> list = productDAO.categorySelectAll();
		if(list.isEmpty())
			throw new CategoryNotFoundException("카테고리 목록이 없습니다.");
		return list;
	}
	
	@Override
	public int categoryInsert(String name) throws DatabaseException {
		int result = productDAO.categoryInsert(name);
		if(result==0)
			throw new DatabaseException("카테고리 추가 실패");
		return result;
	}
	@Override
	public int categoryUpdate(Category category) throws CategoryNotFoundException {
		int result = productDAO.categoryUpdate(category);
		if(result==0)
			throw new CategoryNotFoundException("요청하신 카테고리가 수정되지 않았습니다.");
		return result;
	}
	
	@Override
	public int categoryDelete(int categoryId) throws CategoryNotFoundException {
		int result = productDAO.categoryDelete(categoryId);
		if(result==0)
			throw new CategoryNotFoundException("요청하신 카테고리가 삭제되지 않았습니다.");
		return result;
	}
	
	@Override
	public List<Category> favCategorySeletAll() throws CategoryNotFoundException {
		currentUser.getC
		Lsit<Category> list = productDAO.favCategorySeletAllByUser();
		if(list)
		return null;
	}
	
	@Override
	public int favCategoryUpdate(int categoryId) throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int favCategoryDelete() throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
