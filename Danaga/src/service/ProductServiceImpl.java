package service;

import java.util.List;

import dto.Category;
import dto.Product;
import dto.User;
import exception.CategoryNotFoundException;
import exception.DatabaseException;
import exception.ProductNotFoundException;

public class ProductServiceImpl implements ProductService {

	@Override
	public List<Product> productSelectAll() throws ProductNotFoundException, DatabaseException {
		// TODO Auto-generated method stub
		return null;
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
	public int productInsert(Product product) throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int productUpdate(Product product) throws ProductNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int productDelete(int productId) throws ProductNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Category> categorySelectAll() throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int categoryInsert(Category category) throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int categoryUpdate(Category category) throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int categoryDelete(int categoryId) throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Category> favCategorySeletAll() throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int favCategoryUpdate(User currentUser) throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int favCategoryDelete(User currentUser) throws CategoryNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

}
