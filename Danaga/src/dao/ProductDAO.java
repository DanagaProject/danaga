package dao;

import java.util.List;

import dto.Category;
import dto.FavoriteCategory;
import dto.Product;
import exception.CategoryNotFoundException;
import exception.DatabaseException;
import exception.ProductNotFoundException;

/**
 * 상품 관리 DAO
 * */
public interface ProductDAO {
	/**
	 * 전체 상품 조회
	 * */
	List<Product> productSelectAll() throws ProductNotFoundException, DatabaseException;

	/**
	 * 카테고리별 상품 조회
	 * */
	List<Product> productSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 상품명으로 상품 조회
	 * */
	List<Product> productSelectByName(String keyword) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 로그인 중인 userId의 상품 조회
	 * */
	List<Product> productSelectBySellerId(String sellerId) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 상품 Id로 상품 조회
	 * */
	Product productSelectById(int productId) throws ProductNotFoundException, DatabaseException;	
	
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
	 * 카테고리 목록 조회
	 * */
	List<Category> categorySelectAll() throws CategoryNotFoundException;
	
	/**
	 * 카테고리 추가
	 * */
	int categoryInsert(String name) throws DatabaseException;
	
	/**
	 * 카테고리 수정 
	 * */
	int categoryUpdate(Category category) throws CategoryNotFoundException;
	
	/**
	 * 카테고리 삭제(delete)
	 * */
	int categoryDelete(int categoryId) throws CategoryNotFoundException;
	
	/**
	 * 선호 카테고리 조회
	 * */
	List<FavoriteCategory> favCategorySeletAllByUser(String currentUserId) throws CategoryNotFoundException, DatabaseException;
	
	/**
	 * 선호 카테고리 추가
	 * */
	int favCategoryInsert(String currentUserId, int categoryId) throws DatabaseException;
	
	/**
	 * 선호 카테고리 삭제(delete)
	 * */
	int favCategoryDelete(String currentUserId, int cateogryId) throws CategoryNotFoundException;
	
}
