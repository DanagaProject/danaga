package controller;

import java.util.List;

import dto.Category;
import dto.FavoriteCategory;
import dto.Product;
import exception.CategoryNotFoundException;
import exception.DatabaseException;
import exception.ProductNotFoundException;

import service.ProductService;
import service.ProductServiceImpl;
import view.FailView;

public class ProductController {
	private static ProductService productService = ProductServiceImpl.getInstance();
	
	/**
	 * 상품 목록 조회
	 * */
	public static List<Product> productSelectAll() {
		List<Product> list = null;
		try {
			list = productService.productSelectAll();
		}catch(ProductNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 카테고리별 상품 조회
	 * */
	public static List<Product> productSelectByCategory(int categoryId) {
		List<Product> list = null;
		try {
			list = productService.productSelectByCategory(categoryId);   
        }catch(ProductNotFoundException e) {
        	FailView.printMessage(e.getMessage());
        }catch(CategoryNotFoundException e) {
        	FailView.printMessage(e.getMessage());
        }catch(DatabaseException e) {
            FailView.printMessage(e.getMessage());
        } 
		return list;
	}
	
	/**
	 * 상품명으로 상품 조회
	 * */
	public static List<Product> productSelectByName(String keyword) {
		List<Product> list = null;
		try {
			list = productService.productSelectByName(keyword);   
        }catch(ProductNotFoundException e) {
        	FailView.printMessage(e.getMessage());
        }catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 사용자의 판매 상품 조회(마이페이지용)
	 * */
	public static List<Product> productSelectBySellerAll() {
		List<Product> list = null;
		try {
			list = productService.productSelectBySellerAll();   
        }catch(ProductNotFoundException e) {
        	FailView.printMessage(e.getMessage());
        }catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 상품 등록
	 * */
	public static int productInsert(Product product) {
		int result = 0;
		try {
			result = productService.productInsert(product);	
		}catch(ProductNotFoundException e) {
        	FailView.printMessage(e.getMessage());
        }catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 상품 수정
	 * */
	public static int productUpdate(Product product) {
		int result = 0;
		try {
			result = productService.productUpdate(product);
		}catch(ProductNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 상품 삭제(is_deleted)
	 * */
	public static int productDelete(int productId) {
		int result = 0;
		try {
			result = productService.productDelete(productId);
		}catch(ProductNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 카테고리 목록 조회
	 * */
	public static List<Category> categorySelectAll(){
		List<Category> list = null;
		try {
			list = productService.categorySelectAll();
		}catch(CategoryNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 카테고리 추가
	 * */
	public static int categoryInsert(String name) {
		int result = 0;
		try {
			result = productService.categoryInsert(name);
		}catch(CategoryNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 카테고리 수정 
	 * */
	public static int categoryUpdate(Category category) {
		int result = 0;
		try {
			result = productService.categoryUpdate(category);
		}catch(CategoryNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 카테고리 삭제(delete)
	 * */
	public static int categoryDelete(int categoryId) {
		int result = 0;
		try {
			result = productService.categoryDelete(categoryId);
		}catch(CategoryNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 선호 카테고리 조회
	 * */
	public static List<FavoriteCategory> favCategorySelectAll(){
		List<FavoriteCategory> list = null;
		try {
			list = productService.favCategorySeletAll();
		}catch(CategoryNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 선호 카테고리 추가
	 * */
	public static int favCategoryInsert(int categoryId) {
		int result = 0;
		try {
			result = productService.favCategoryInsert(categoryId);
		}catch(CategoryNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 선호 카테고리 삭제(delete)
	 * */
	public static int favCategoryDelete(int categoryId) {
		int result = 0;
		try {
			result = productService.favCategoryDelete(categoryId);
		}catch(CategoryNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 관리자용 상품 목록 조회(모든 상태)
	 * */
	public static List<Product> adminProductSelectAll(){
		List<Product> list = null;
		try {
			list = productService.adminProductSelectAll();
		}catch(ProductNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 관리자용 카테고리별 상품 조회(모든 상태)
	 * */
	public static List<Product> adminProductSelectByCategory(int categoryId){
		List<Product> list = null;
		try {
			list = productService.adminProductSelectByCategory(categoryId);
		}catch(ProductNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(CategoryNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 관리자용 상품명으로 상품 조회(모든 상태)
	 * */
	public static List<Product> adminProductSelectByName(String name){
		List<Product> list = null;
		try {
			list = productService.adminProductSelectByName(name);
		}catch(ProductNotFoundException e) {
			FailView.printMessage(e.getMessage());
		}catch(DatabaseException e) {
			FailView.printMessage(e.getMessage());
		}
		return list;
	}
	
}
