package service;

public class OrderServiceImp implements OrderService {
	/*
	 * 구매 신청
	 * users 테이블에서 구매자의 balance>= products 테이블의 price 일때 주문성공
	 * 주문 성공시 
	 * ordertable에 추가
	 * users에서 balance를 products의 값 만큼 차감
	 * products에서 status를 PENDING으로 전환
	 * 
	 * 실패시 
	 * 판매자에게 잔액이 부족합니다 메시지 출력(예외처리?)
	 */
	
}
