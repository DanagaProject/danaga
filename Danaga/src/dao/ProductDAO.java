package dao;

import java.util.List;

import dto.Category;
import dto.FavoriteCategory;
import dto.Product;
import dto.User;
import exception.CategoryNotFoundException;
import exception.DatabaseException;
import exception.ProductNotFoundException;

/**
 * 상품 관리 DAO
 * */
public interface ProductDAO {
	/**
	 * 전체 상품
	 * */
	List<Product> productSelectAll() throws ProductNotFoundException, DatabaseException;

	/**
	 * 카테고리별 상품
	 * */
	List<Product> productSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 상품명으로 상품 가져오기
	 * */
	Product productSelectByName(String keyword) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 상품 등록
	 * */
	int productInsert(Product product) throws DatabaseException;
	
	/**
	 * 상품 수정
	 * */
	int productUpdate(Product product) throws ProductNotFoundException;
	
	/**
	 * 상품 삭제(is_deleted)
	 * */
	int productDelete(int productId) throws ProductNotFoundException;
	
	/**
	 * 카테고리 조회
	 * */
	List<Category> categorySelectAll() throws CategoryNotFoundException;
	
	/**
	 * 카테고리 추가
	 * */
	int categoryInsert(Category category) throws DatabaseException;
	
	/**
	 * 카테고리 수정 
	 * */
	int categoryUpdate(Category category) throws CategoryNotFoundException;
	
	/**
	 * 카테고리 삭제(delete)
	 * */
	int categoryDelete(int categoryId) throws CategoryNotFoundException;
	
	/**
	 * 즐겨찾기 카테고리 조회
	 * */
	List<FavoriteCategory> favCategorySeletAll() throws CategoryNotFoundException, DatabaseException;
	
	/**
	 * 즐겨찾기 카테고리 추가
	 * */
	int favCategoryUpdate(User currentUser) throws CategoryNotFoundException;
	
	
	/**
	 * 즐겨찾기 카테고리 삭제(delete)
	 * */
	int favCategoryDelete(User currentUser) throws CategoryNotFoundException;
	
}
