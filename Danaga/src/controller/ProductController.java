package controller;

import java.util.List;

import dto.Category;
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
		}catch(ProductNotFoundException e1) {
			FailView.printMessage(e1.getMessage());
		}catch(DatabaseException e2) {
			FailView.printError(e2.getMessage());
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
        } catch (DatabaseException e) {
            FailView.printError(e.getMessage());
        } catch (ProductNotFoundException e) {
        	FailView.printMessage(e.getMessage());
        } catch (CategoryNotFoundException e) {
        	FailView.printMessage(e.getMessage());
        }
		return list;
	}
	
	/**
	 * 상품명으로 상품 조회(상세페이지)
	 * */
	public static List<Product> productSelectByName(String keyword) {
		List<Product> list = null;
		try {
			list = productService.productSelectByName(keyword);   
        } catch (DatabaseException e) {
            FailView.printError(e.getMessage());
        } catch (ProductNotFoundException e) {
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
		}
		return result;
	}
	
	/**
	 * 카테고리 조회
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
		}
		return result;
	}
	
	
	/**
	 * 선호 카테고리 조회
	 * */
	
	
	/**
	 * 선호 카테고리 추가
	 * */

	
	
	/**
	 * 선호 카테고리 삭제(delete)
	 * */
	
	
}
