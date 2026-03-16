package service;

import java.util.List;

import dto.Category;
import dto.FavoriteCategory;
import dto.Product;
import exception.CategoryNotFoundException;
import exception.DatabaseException;
import exception.ProductNotFoundException;

public interface ProductService {
	/**
	 * 전체 상품 조회(판매 중인 상품만, 최신순)
	 * 유효성 검사: 리스트가 비어 있다면 예외처리
	 * @return 판매 중인 전체 상품 목록
	 * */
	List<Product> productSelectAll() throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 카테고리별 상품 조회(판매 중인 상품만, 최신순)
	 * 유효성 검사: 카테고리에 해당하는 상품이 없다면 예외처리
	 * 			 가져온 리스트가 비어 있다면 예외처리
	 * @param categoryId 조회할 카테고리 번호
	 * @return 해당 카테고리의 판매 중인 상품 목록
	 * */
	List<Product> productSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException, CategoryNotFoundException;
	
	/**
	 * 상품명으로 상품 조회(판매중인 상품만, 최신순)
	 * 유효성 검사: keyword가 null 또는 공백이라면 예외처리
	 * 			 keyword가 2글자 미만이면 예외처리
	 * 			 조회된 상품이 없으면 예외처리
	 * @param keyword 상품 제목에서 검색할 키워드 (2글자 이상)
	 * @return 키워드가 포함된 판매 중인 상품 목록
	 * */
	List<Product> productSelectByName(String keyword) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 로그인한 유저의 상품 조회(수정/삭제용, 판매중인 상품만, 최신순)
	 * 유효성 검사: 조회된 상품이 없으면 예외처리
	 * @return 현재 로그인한 사용자의 판매 중인 상품 목록
	 * */
	List<Product> productSelectBySellerOnSale() throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 로그인한 유저의 판매 상품 조회(마이페이지용, 삭제된 상품 제외, 최신순)
	 * 유효성 검사: 조회된 상품이 없으면 예외처리
	 * @return 현재 로그인한 사용자의 전체 판매 상품 목록 (판매중/거래중/판매완료 모두 포함)
	 * */
	List<Product> productSelectBySellerAll() throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 상품 등록
	 * 회원이면 상품 판매 등록 가능
	 * 유효성 검사: 동일한 상품이 이미 존재하면 등록 불가(중복기준: 판매자 아이디, 카테고리 아이디, 상품명, 가격, 상품 상태) 
	 * 			 가격이 0원 이하라면 예외처리
	 * 			 DB 등록 실패 시 DatabaseException
	 * @param product 등록할 상품 정보(판매자아이디, 카테고리아이디, 상품명, 가격, 상품상태, 설명)
	 * @return 등록 성공 시 1
	 * */
	int productInsert(Product product) throws DatabaseException, ProductNotFoundException;
	
	/**
	 * 상품 수정(본인 상품만)
	 * 수정 가능 항목: 카테고리아이디, 상품명, 가격, 상품상태, 설명
	 * 유효성 검사: 수정한 가격이 0원 이하면 예외처리
	 * 	         수정할 내용이 비어 있으면 예외처리
	 * 			 이미 삭제된 상품이면 예외처리
	 * 			 상품이 on_sale 상태일 때만 수정
	 * @param product 수정할 상품 정보(productId 필수, 나머지는 수정할 항목만 입력)
	 * @return 수정 성공 시 1			
	 * */
	int productUpdate(Product product) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 상품 삭제(본인 상품만) Soft Delete(is_deleted = 'y')
	 * 유효성 검사: 이미 삭제된 상품이면 예외처리
	 * 	         상품이 on_sale일 때만 삭제 가능
	 * @param productId 삭제할 상품 번호
	 * @return 삭제 성공 시 1
	 * */
	int productDelete(int productId) throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 카테고리 조회(관리자 전용)
	 * 유효성 검사: 카테고리 목록이 비어 있으면 예외처리
	 * @return 전체 카테고리 목록
	 * */
	List<Category> categorySelectAll() throws CategoryNotFoundException;
	
	/**
	 * 카테고리 등록(관리자 전용)
	 * 유효성 검사: 카테고리 이름이 null 또는 공백이면 예외처리
	 * 			 같은 이름의 카테고리가 있으면 등록 불가
	 * 			 DB 등록 실패 시 DatabaseException
	 * @param name 등록할 카테고리 이름
	 * @return 등록 성공 시 1
	 * */
	int categoryInsert(String name) throws DatabaseException, CategoryNotFoundException;
	
	/**
	 * 카테고리 수정(관리자 전용)
	 * 유효성 검사: 수정할 카테고리 이름이 null 또는 공백이면 예외처리
	 * 			 존재하지 않는 카테고리 번호라면 예외처리
	 * @param category 수정할 카테고리 정보 (categoryId, name)
	 * @return 수정 성공 시 1
	 * */
	int categoryUpdate(Category category) throws CategoryNotFoundException;
	
	/**
	 * 카테고리 삭제(관리자 전용) Hard Delete
	 * 유효성 검사: 존재하지 않는 카테고리 번호이면 예외처리
	 * 			 해당 카테고리에 속한 상품이 하나라도 있으면 삭제 불가(삭제된 상품 포함)
	 * 			 해당 카테고리에 속한 선호 카테고리가 하나라도 있으면 삭제 불가
	 * @param categoryId 삭제할 카테고리 번호
	 * @return 삭제 성공 시 1
	 * */
	int categoryDelete(int categoryId) throws CategoryNotFoundException, DatabaseException;
	
	/**
	 * 선호 카테고리 조회(로그인한 유저의 것만)
	 * 유효성 검사: 선호 카테고리가 없으면 예외처리
	 * @return 현재 로그인한 사용자의 선호 카테고리 목록
	 * */
	List<FavoriteCategory> favCategorySeletAll() throws CategoryNotFoundException, DatabaseException;
	
	/**
	 * 선호 카테고리 추가(로그인한 유저의 것만)
	 * 유효성 검사: 존재하지 않는 카테고리 번호이면 예외처리
	 * 			 이미 추가된 카테고리이면 예외처리
	 * 			 DB 등록 실패 시 DatabaseException
	 * @param categoryId 추가할 카테고리 번호
	 * @return 추가 성공 시 1
	 * */
	int favCategoryInsert(int categoryId) throws DatabaseException, CategoryNotFoundException;
	
	/**
	 * 선호 카테고리 삭제(로그인한 유저의 것만)
	 * 유효성 검사: 현재 유저의 선호 카테고리 목록에 존재하지 않는 번호면 예외처리
	 * @param categoryId 삭제할 카테고리 번호
	 * @return 삭제 성공 시 1
	 * */
	int favCategoryDelete(int categoryId) throws CategoryNotFoundException, DatabaseException;
	
	/**
	 * 관리자용 전체 상품 조회(최신순)
	 * 유효성 검사: 조회된 상품이 없으면 예외처리
	 * @return 전체 상품 목록 (삭제된 상품, 모든 상태 포함)
	 * */
	List<Product> adminProductSelectAll() throws ProductNotFoundException, DatabaseException;
	
	/**
	 * 관리자용 카테고리별 상품 조회
	 * 유효성 검사: 존재하지 않는 카테고리 번호이면 예외처리
	 * 			 조회된 상품이 없으면 예외처리
	 * @param categoryId 조회할 카테고리 번호
	 * @return 해당 카테고리의 전체 상품 목록 (삭제된 상품, 모든 상태 포함)
	 * */
	List<Product> adminProductSelectByCategory(int categoryId) throws ProductNotFoundException, DatabaseException, CategoryNotFoundException;
	
	/**
	 * 관리자용 상품명으로 상품 가져오기(최신순)
	 * 유효성 검사: keyword가 null 또는 공백이라면 예외처리
	 * 			 keyword가 2글자 미만이면 예외처리
	 * 			 조회된 상품이 없으면 예외처리
	 * @param keyword 상품 제목에서 검색할 키워드 (2글자 이상)
	 * @return 키워드가 포함된 전체 상품 목록 (삭제된 상품, 모든 상태 포함)
	 * */
	List<Product> adminProductSelectByName(String keyword) throws ProductNotFoundException, DatabaseException;
	
}
