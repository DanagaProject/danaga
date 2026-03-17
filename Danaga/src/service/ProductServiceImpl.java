package service;

import java.util.List;
import java.util.stream.Collectors;

import dao.ProductDAO;
import dao.ProductDAOImpl;
import dto.Category;
import dto.FavoriteCategory;
import dto.Product;
import exception.CategoryNotFoundException;
import exception.DatabaseException;
import exception.ProductNotFoundException;
import util.SessionManager;

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
		
		// is_deleted = 'n' AND status_id = 10 필터링 추가
	    list = list.stream()
	               .filter(p -> p.getIsDeleted().equalsIgnoreCase("n") && p.getStatusId() == 10)
	               .collect(Collectors.toList());
	    
		return list;
	}
	
	@Override
	public List<Product> productSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException, CategoryNotFoundException {
		List<Category> categoryList = productDAO.categorySelectAll();
	   
		boolean isValid = categoryList.stream()
	            .anyMatch(c -> c.getCategoryId() == categoryId);
	    if (!isValid)
	        throw new CategoryNotFoundException(categoryId + "번은 존재하지 않는 카테고리입니다.");
	    
	    List<Product> list = productDAO.productSelectByCategory(categoryId);
	    
	    list = list.stream()
	               .filter(p -> p.getIsDeleted().equalsIgnoreCase("n") && p.getStatusId() == 10)
	               .collect(Collectors.toList());
	    
	    return list;
	}
	
	@Override
	public List<Product> productSelectByName(String keyword) throws ProductNotFoundException, DatabaseException {
		if(keyword == null || keyword.trim().isEmpty())
			throw new ProductNotFoundException("검색어를 입력해주세요");
		
		if (keyword.trim().length() < 2)
	        throw new ProductNotFoundException("검색어는 2글자 이상 입력해주세요.");
		
		List<Product> list = productDAO.productSelectByName(keyword);
		
		list = list.stream()
	               .filter(p -> p.getIsDeleted().equalsIgnoreCase("n") && p.getStatusId() == 10)
	               .collect(Collectors.toList());

		return list;
	}
	
	///////////////////////////////////////////////////////////////////	
	@Override
	public List<Product> productSelectBySellerAll() throws ProductNotFoundException, DatabaseException{		
		String sellerId = SessionManager.getCurrentUserId();
		
		List<Product> list = productDAO.productSelectBySellerId(sellerId);
		
		list = list.stream()
		           .filter(p -> p.getIsDeleted().equalsIgnoreCase("n"))
		           .collect(Collectors.toList());
		
		return list;
	}
	
	////////////////////////////////////////////////////////////////////	
	@Override
	public int productInsert(Product product) throws DatabaseException, ProductNotFoundException{
		if(product.getPrice() <=0)
			throw new DatabaseException("가격이 0원보다 커야 합니다.");
		
		try {
			List<Product> myProducts = productDAO.productSelectBySellerId(product.getSellerId());
		    boolean isDuplicate = myProducts.stream()
		    	.filter(p -> p.getIsDeleted().equalsIgnoreCase("n"))
		    	.anyMatch(p ->
		            p.getCategoryId() == product.getCategoryId() &&
		            p.getTitle().equals(product.getTitle()) &&
		            p.getPrice() == product.getPrice() &&
		            p.getConditionId() == product.getConditionId()
		    );
		    if(isDuplicate)
		    	throw new DatabaseException("동일한 상품이 이미 등록되어 있습니다.");
		}catch(ProductNotFoundException e) {
			//중복이 없다는 것으로 통과
		}
		int result = productDAO.productInsert(product);
		
		if(result==0)
			throw new DatabaseException("상품이 등록되지 않았습니다.");
		return result;
	}
	
	@Override
	public int productUpdate(Product product) throws ProductNotFoundException, DatabaseException {
	    if (product.getCategoryId() == 0 &&
	        (product.getTitle() == null || product.getTitle().trim().isEmpty()) &&
	        product.getPrice() == 0 &&
	        product.getConditionId() == 0 &&
	        (product.getDescription() == null || product.getDescription().trim().isEmpty()))
	       throw new ProductNotFoundException("수정할 내용을 입력해주세요.");
	    
	    if (product.getPrice() != 0 && product.getPrice() <= 0)
	        throw new ProductNotFoundException("가격은 0원보다 커야 합니다.");
	    
	    Product existing = productDAO.productSelectById(product.getProductId());
	    
	    if (existing.getIsDeleted().equalsIgnoreCase("y"))
	        throw new ProductNotFoundException("삭제된 상품입니다.");
	    if (existing.getStatusId() != 10)
	        throw new ProductNotFoundException("판매 중인 상품만 수정할 수 있습니다.")
;	    
		int result = productDAO.productUpdate(product);
		if(result==0)
			throw new ProductNotFoundException("입력하신 상품이 없습니다.");
		return result;
	}
	
	@Override
	public int productDelete(int productId) throws ProductNotFoundException, DatabaseException {
	    Product existing = productDAO.productSelectById(productId);
	    
	    if (existing.getIsDeleted().equalsIgnoreCase("y"))
	        throw new ProductNotFoundException("이미 삭제된 상품입니다.");
	    if (existing.getStatusId() != 10)
	        throw new ProductNotFoundException("판매 중인 상품만 삭제할 수 있습니다.");
	    
		int result = productDAO.productDelete(productId);
		if(result==0)
			throw new ProductNotFoundException("삭제할 상품이 없습니다.");
		return result;
	}
	
	//////////////////////////////////////////////////////////////////////
	@Override
	public List<Category> categorySelectAll() throws CategoryNotFoundException {
		List<Category> list = productDAO.categorySelectAll();
		return list;
	}
	
	@Override
	public int categoryInsert(String name) throws DatabaseException, CategoryNotFoundException {
		if(name==null || name.trim().isEmpty())
			throw new DatabaseException("카테고리 이름을 입력해 주세요.");
		
		List<Category> list = productDAO.categorySelectAll();
	    boolean isDuplicate = list.stream()
	            .anyMatch(c -> c.getName().equalsIgnoreCase(name.trim()));
	    if (isDuplicate)
	        throw new DatabaseException("이미 존재하는 카테고리입니다.");
		
		int result = productDAO.categoryInsert(name);
		if(result==0)
			throw new DatabaseException("카테고리 추가 실패");
		return result;
	}
	
	@Override
	public int categoryUpdate(Category category) throws CategoryNotFoundException {
		if (category.getName() == null || category.getName().trim().isEmpty())
	        throw new CategoryNotFoundException("수정할 카테고리 이름을 입력해주세요.");
		
		List<Category> list = productDAO.categorySelectAll();
	    boolean isDuplicate = list.stream()
	            .anyMatch(c -> c.getName().equalsIgnoreCase(category.getName()));
	    if (isDuplicate)
	        throw new CategoryNotFoundException("이미 존재하는 카테고리 이름입니다.");
	    
		int result = productDAO.categoryUpdate(category);
		if(result==0)
			throw new CategoryNotFoundException("카테고리가 수정에 실패했습니다.");
		return result;
	}
	
	@Override
	public int categoryDelete(int categoryId) throws CategoryNotFoundException, DatabaseException {
		List<Category> list = productDAO.categorySelectAll();
	    boolean isExist = list.stream()
	            .anyMatch(c -> c.getCategoryId() == categoryId);
	    if (!isExist)
	        throw new CategoryNotFoundException(categoryId + "번 카테고리가 존재하지 않습니다.");
		
	    try {
	        List<Product> products = productDAO.productSelectByCategory(categoryId);
	        if (!products.isEmpty())
	            throw new CategoryNotFoundException("해당 카테고리에 상품이 있어 삭제할 수 없습니다.");
	    } catch (ProductNotFoundException e) {
	        // 상품이 없음 → 삭제 가능 → 통과
	    }
	    
		int result = productDAO.categoryDelete(categoryId);
		if(result==0)
			throw new CategoryNotFoundException("카테고리 삭제에 실패했습니다.");
		return result;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	@Override
	public List<FavoriteCategory> favCategorySeletAll() throws DatabaseException {
		String currentUserId =  SessionManager.getCurrentUserId();
		List<FavoriteCategory> list = productDAO.favCategorySeletAllByUser(currentUserId);
		return list;
	}
	
	@Override
	public int favCategoryInsert(int categoryId) throws DatabaseException, CategoryNotFoundException {
		String currentUserId =  SessionManager.getCurrentUserId();
		
		List<Category> categoryList = productDAO.categorySelectAll();
		boolean isValid = categoryList.stream()
		        .anyMatch(c -> c.getCategoryId() == categoryId);
		if (!isValid)
			throw new CategoryNotFoundException(categoryId + "번은 존재하지 않는 카테고리입니다.");
		 
		List<FavoriteCategory> favList = productDAO.favCategorySeletAllByUser(currentUserId);
		boolean isDuplicate = favList.stream()
				.anyMatch(f -> f.getCategoryId() == categoryId);
		if (isDuplicate)
		    throw new DatabaseException("이미 추가된 카테고리입니다.");
		
		int result = productDAO.favCategoryInsert(currentUserId, categoryId);
		if(result==0)
			throw new DatabaseException("선호 카테고리를 추가하지 못했습니다.");
		return result;
	}
	
	@Override
	public int favCategoryDelete(int categoryId) throws DatabaseException {
		String currentUserId = SessionManager.getCurrentUserId();
		
		List<FavoriteCategory> favList = productDAO.favCategorySeletAllByUser(currentUserId);
		boolean isExist = favList.stream()
            .anyMatch(f -> f.getCategoryId() == categoryId);
	    if (!isExist)
	        throw new DatabaseException("선호 카테고리에 존재하지 않는 카테고리입니다.");

		
		int result = productDAO.favCategoryDelete(currentUserId, categoryId);
		if(result==0)
			throw new DatabaseException("해당 카테고리를 삭제할 수 없습니다.");
		return result;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<Product> adminProductSelectAll() throws ProductNotFoundException, DatabaseException {
	    List<Product> list = productDAO.productSelectAll();
	    return list;
	}
	
	@Override
	public List<Product> adminProductSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException, CategoryNotFoundException {
	    List<Category> categoryList = productDAO.categorySelectAll();
	    
	    boolean isValid = categoryList.stream()
	            .anyMatch(c -> c.getCategoryId() == categoryId);
	    if (!isValid)
	        throw new CategoryNotFoundException(categoryId + "번은 존재하지 않는 카테고리입니다.");

	    List<Product> list = productDAO.productSelectByCategory(categoryId);
	    
	    return list;
	}
	@Override
	public List<Product> adminProductSelectByName(String keyword) throws ProductNotFoundException, DatabaseException {
	    if (keyword == null || keyword.trim().isEmpty())
	        throw new ProductNotFoundException("검색어를 입력해주세요.");
	    
	    if (keyword.trim().length() < 2)
	        throw new ProductNotFoundException("검색어는 2글자 이상 입력해주세요.");

	    List<Product> list = productDAO.productSelectByName(keyword);

	    return list;
	}
	
	
}
