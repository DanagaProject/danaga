package service;

import java.util.List;

import dto.Category;
import dto.Product;
import dto.User;
import exception.CategoryNotFoundException;
import exception.DatabaseException;
import exception.ProductNotFoundException;
/**
 * 유효성 검사 추가하기!!
 * */
public interface ProductService {
	/**
	 * 전체 상품 조회
	 * 상품 번호, 상품명, 상품 상태, 가격
	 * */
	List<Product> productSelectAll() throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 카테고리별 상품 조회
	 * 상품 번호, 상품명, 상품 상태, 가격
	 * */
	List<Product> productSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 상품명으로 상품 가져오기(상품 상세보기)
	 * 비회원, 관리자: 주문하기 버튼 비활성화
	 * 회원: 주문하기 버튼 활성화
	 * 상품 번호, 카테고리이름, 상품명, 상품상태, 가격, 설명, 판매자 아이디
	 * */
	Product productSelectByName(String keyword) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 상품 등록
	 * 회원이면 상품 판매 등록 가능
	 * 판매자아이디, 카테고리아이디, 상품명, 가격, 상품상태, 설명
	 * */
	int productInsert(Product product) throws DatabaseException;
	
	/**
	 * 상품 수정
	 * 판매상품 관리 페이지에서 해당하는 상품 수정
	 * 카테고리아이디, 상품명, 가격, 상품상태, 설명
	 * 유효성 검사: 가격이 0원 이상인지, 판매자아이디와 현재 유저의 아이디가 일치하는 지, 상품이 on_sale 상태일 때만 수정
	 * */
	int productUpdate(Product product) throws ProductNotFoundException;
	
	/**
	 * 상품 삭제
	 * 판매 상품 관리 페이지에서 상품 삭제
	 * 유효성 검사: 판매자아이디와 현재 유저의 아이디가 일치하는 지, 상품이 on_sale일 때만 삭제 가능
	 * */
	int productDelete(int productId) throws ProductNotFoundException;
	
	/**
	 * 카테고리 조회
	 * 관리자가 카테고리 관리 페이지에서 카테고리 목록 조회
	 * */
	List<Category> categorySelectAll() throws CategoryNotFoundException;
	
	/**
	 * 카테고리 추가
	 * 카테고리 관리 페이지에서 카테고리 추가
	 * 카테고리 이름
	 * */
	int categoryInsert(Category category) throws DatabaseException;
	
	/**
	 * 카테고리 수정
	 * 카테고리 관리 페이지에서 카테고리 추가
	 * 카테고리 이름
	 * */
	int categoryUpdate(Category category) throws DatabaseException;
	
	/**
	 * 카테고리 삭제
	 * 카테고리 관리 페이지에서 카테고리 삭제(delete)
	 * 유효성 검사: 카테고리를 사용하고 있는 상품이 있다면 지울 수 없고,
	 *           카테고리를 선호 카테고리로 지정하고 있다면 삭제할 수 없다. 
	 * */
	int categoryDelete(int categoryId) throws CategoryNotFoundException;
	
	/**
	 * 즐겨찾기 카테고리 조회
	 * 회원 마이페이지에서 즐겨찾기 관리 페이지에 출력할 카테고리 리스트
	 * 카테고리 이름
	 * */
	List<Category> favCategorySeletAll() throws CategoryNotFoundException;
	
	/**
	 * 즐겨찾기 카테고리 추가
	 * 회원 마이페이지에서 즐겨찾기 관리 페이지에서 즐겨찾기 카테고리 추가
	 * 카테고리 번호
	 * */
	int favCategoryUpdate(User currentUser) throws CategoryNotFoundException;
	
	/**
	 * 즐겨찾기 카테고리 삭제
	 * 회원 마이페이지에서 즐겨찾기 관리 페이지에서 즐겨찾기 카테고리 삭제
	 * 카테고리 번호
	 * */
	int favCategoryDelete(User currentUser) throws CategoryNotFoundException;
	
}
