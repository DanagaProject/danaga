package service;

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
			throw new ProductNotFoundException("검색 된 레코드가 없습니다.");
		return list;
	}
	
	@Override
	public List<Product> productSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Product productSelectByName(String keyword) throws ProductNotFoundException, DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void productInsert(Product product) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void productUpdate(Product product) throws ProductNotFoundException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void productDelete(int productId) throws ProductNotFoundException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Category> categorySelectAll() throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void categoryInsert(Category category) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void categoryUpdate(Category category) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void categoryDelete(int categoryId) throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Category> favCategorySeletAll() throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void favCategoryUpdate(int categoryId) throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void favCategoryDelete() throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		
	}
	
	
}
