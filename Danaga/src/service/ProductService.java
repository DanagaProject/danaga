package service;

import java.util.List;

import dto.Category;
import dto.FavoriteCategory;
import dto.Product;
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
	 * 유효성 검사: 가져온 리스트가 비어 있다면 예외처리
	 * */
	List<Product> productSelectAll() throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 카테고리별 상품 조회
	 * 상품 번호, 상품명, 상품 상태, 가격
	 * 유효성 검사: 카테고리에 해당하는 상품이 없다면 예외처리
	 * 			 가져온 리스트가 비어 있다면 예외처리
	 * @param categoryId
	 * */
	List<Product> productSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException, CategoryNotFoundException;
	
	/**
	 * 상품명으로 상품 가져오기
	 * 유효성 검사: keyword가 null 또는 공백이라면 예외처리
	 * 			 keyword의 최소길이가 2글자 이상인지 체크
	 * 			 가져온 리스트가 비어 있다면 예외처리
	 * */
	List<Product> productSelectByName(String keyword) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 사용자의 판매 상품 조회(수정/삭제용)
	 * 유효성 검사: on_sale인 경우만 가져온다.
	 * 			 가져온 리스트가 비어 있다면 예외처리
	 * */
	List<Product> productSelectBySellerOnSale() throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 사용자의 판매 상품 조회(마이페이지용)
	 * 유효성 검사: 가져온 리스트가 비어 있다면 예외처리
	 * */
	List<Product> productSelectBySellerAll() throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 상품 등록
	 * 회원이면 상품 판매 등록 가능
	 * 유효성 검사: 등록하려고 입력받은 요소가 원래 있는 상품과 같은 경우에는 등록할 수 없음(카테고리 아이디, 상품명, 가격, 상품 상태) 
	 * 			 가격이 0원 보다 큰 지 체크
	 * 			 가져온 상품이 비어 있다면 예외처리
	 * @param Product product(판매자아이디, 카테고리아이디, 상품명, 가격, 상품상태, 설명)
	 * */
	int productInsert(Product product) throws DatabaseException, ProductNotFoundException;
	
	/**
	 * 상품 수정
	 * 판매상품 관리 페이지에서 해당하는 상품 수정
	 * 카테고리아이디, 상품명, 가격, 상품상태, 설명
	 * 유효성 검사: 가격이 0원 이상인지, 판매자아이디와 현재 유저의 아이디가 일치하는 지, 상품이 on_sale 상태일 때만 수정
	 * */
	int productUpdate(Product product) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 상품 삭제
	 * 판매 상품 관리 페이지에서 상품 삭제
	 * 유효성 검사: 판매자아이디와 현재 유저의 아이디가 일치하는 지, 상품이 on_sale일 때만 삭제 가능
	 * */
	int productDelete(int productId) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 카테고리 조회
	 * 관리자가 카테고리 관리 페이지에서 카테고리 목록 조회
	 * */
	List<Category> categorySelectAll() throws CategoryNotFoundException;
	
	/**
	 * 카테고리 추가
	 * 카테고리 관리 페이지에서 카테고리 추가
	 * 유효성 검사: 같은 이름의 카테고리가 있으면 추가할 수 없다.
	 * 카테고리 이름
	 * */
	int categoryInsert(String name) throws DatabaseException, CategoryNotFoundException;
	
	/**
	 * 카테고리 수정
	 * 카테고리 관리 페이지에서 카테고리 추가
	 * 유효성 검사: 수정하려는 카테고리에 하나라도 상품이 있다면 수정할 수 없다.
	 * 관리자가 수정하고 싶은 카테고리의 번호와 수정할 카테고리 이름의 객체를 받아서 수정
	 * */
	int categoryUpdate(Category category) throws CategoryNotFoundException;
	
	/**
	 * 카테고리 삭제
	 * 카테고리 관리 페이지에서 카테고리 삭제(delete)
	 * 유효성 검사: 카테고리를 사용하고 있는 상품이 있다면 지울 수 없고,
	 *           카테고리를 선호 카테고리로 지정하고 있다면 삭제할 수 없다. 
	 * */
	int categoryDelete(int categoryId) throws CategoryNotFoundException, DatabaseException;
	
	/**
	 * 선호 카테고리 조회
	 * 회원 마이페이지에서 즐겨찾기 관리 페이지에 출력할 카테고리 리스트
	 * 카테고리 이름
	 * */
	List<FavoriteCategory> favCategorySeletAll() throws CategoryNotFoundException, DatabaseException;
	
	/**
	 * 선호 카테고리 추가
	 * 회원 마이페이지에서 즐겨찾기 관리 페이지에서 즐겨찾기 카테고리 추가
	 * 카테고리 번호
	 * */
	int favCategoryInsert(int categoryId) throws DatabaseException, CategoryNotFoundException;
	
	/**
	 * 선호 카테고리 삭제
	 * 회원 마이페이지에서 즐겨찾기 관리 페이지에서 즐겨찾기 카테고리 삭제
	 * 카테고리 번호
	 * */
	int favCategoryDelete(int categoryId) throws CategoryNotFoundException, DatabaseException;
	
	/**
	 * 관리자용 전체 상품 조회
	 * 상품 번호, 상품명, 상품 상태, 가격
	 * 유효성 검사: 가져온 리스트가 비어 있다면 예외처리
	 * */
	List<Product> adminProductSelectAll() throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 관리자용 카테고리별 상품 조회
	 * 상품 번호, 상품명, 상품 상태, 가격
	 * 유효성 검사: 카테고리에 해당하는 상품이 없다면 예외처리
	 * 			 가져온 리스트가 비어 있다면 예외처리
	 * @param categoryId
	 * */
	List<Product> adminProductSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException, CategoryNotFoundException;
	
	/**
	 * 관리자용 상품명으로 상품 가져오기
	 * 유효성 검사: keyword가 null 또는 공백이라면 예외처리
	 * 			 keyword의 최소길이가 2글자 이상인지 체크
	 * 			 가져온 리스트가 비어 있다면 예외처리
	 * 상품 번호, 카테고리이름, 상품명, 상품상태, 가격, 설명, 판매자 아이디
	 * */
	List<Product> adminProductSelectByName(String keyword) throws ProductNotFoundException, DatabaseException;
	
}
