package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dto.Orders;
import dto.Product;
import dto.User;
import service.OrdersService;
import service.OrdersServiceImpl;
import util.SessionManager;
import view.CommonView;
import view.OrdersView;

public class OrdersController {
    private final OrdersService ordersService = new OrdersServiceImpl();
    private Scanner sc = new Scanner(System.in);

    // ==========================================
    // 1. 상품 상세페이지 -> 구매 신청 프로세스
    // ==========================================
    public boolean requestPurchase(int productId) {
        try {
            // 1. 세션에서 현재 로그인한 유저 객체 가져오기
            User loginUser = SessionManager.getCurrentUser();
            
            if (loginUser == null) {
                return false; // 로그인 정보가 없으면 실패 처리
            }

            // 2. 서비스로 넘겨줄 주문(Orders) DTO 생성 및 세팅
            Orders order = new Orders();
            order.setProductId(productId);
            order.setBuyerId(loginUser.getUserId());

            // 3. 서비스 호출
            // 유저 객체(loginUser)를 함께 넘겨서, 서비스단에서 DB 차감 후 세션 잔액까지 동기화하게 합니다.
            ordersService.ordersInsert(order, loginUser);
            
            // 4. 에러 없이 끝났다면 뷰에게 성공(true)을 알림
            return true; 

        } catch (SQLException e) {
            // 잔액 부족이나 DB 에러가 발생하여 서비스에서 예외를 던졌을 때
            // 뷰에게 실패(false)를 알려서 실패 화면을 띄우게 합니다.
            // 개발자 확인용 로그만 가볍게 남깁니다.
            // System.out.println("\n[System Log] 구매 처리 실패: " + e.getMessage());
            return false; 
        }
    }

    // ==========================================
    // 2. 마이페이지 -> 주문 내역, 확정, 취소 로직
    // ==========================================
    
    // 내 주문 내역 가져오기
    public List<Orders> getOrdersByUserId(String userId) throws SQLException {
        return ordersService.selectOrdersByUserId(userId);
    }

    // 거래 확정
    public void confirmTrade(int orderId) throws SQLException {
        ordersService.confirmTrade(orderId);
    }

    // 취소 요청
    public void cancleRequest(int orderId) throws SQLException {
        ordersService.cancleRequest(orderId);
    }
 // 1. 내 판매 내역 조회
    public List<Orders> getSalesBySellerId(String sellerId) throws SQLException {
        return ordersService.getSalesBySellerId(sellerId);
    }

    // 2. 배송 시작 처리 (상태 4 -> 5)
    public void startDelivery(int orderId) throws SQLException {
        ordersService.startDelivery(orderId);
    }

    // 3. 취소 요청 승인 및 환불 (상태 6 -> 7)
    public void cancelComplete(int orderId) {
        try {
        	User loginUser = SessionManager.getCurrentUser();
            // 1. 서비스 계층 호출 (실제 환불 및 상태 변경 로직 실행)
        	ordersService.cancelComplete(orderId, loginUser);
        } catch (SQLException e) {
            // 3. DB 에러 발생 시 로그를 남기거나 사용자에게 알림
            System.out.println("\n❌ [DB 오류] 취소 처리 중 문제가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            // 4. 기타 예상치 못한 에러 처리
            System.out.println("\n❌ 오류 발생: " + e.getMessage());
        }
    }
    public void rejectCancel(int orderId) {
        try {
            // 서비스 호출 (DAO의 rejectCancel까지 이어짐)
            int result = ordersService.rejectCancel(orderId);
            
            if (result > 0) {
                // 성공 시 별도 메시지는 View에서 출력하므로 여기서는 로직만 수행
            } else {
                System.out.println("\n❌ 거절 처리 실패: 해당 주문을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("\n❌ [DB 에러] 거절 처리 중 오류: " + e.getMessage());
        }
    }
 // 1. 관리자 강제 환불 (취소 승인)
    public void adminForceCancel(int orderId) {
        try {
            // 세션이나 로그인 정보에서 관리자 권한을 가져와 전달 (예: "ADMIN")
            ordersService.adminForceCancel(orderId, "ADMIN");
        } catch (SQLException e) {
            System.out.println("\n❌ 처리 실패: " + e.getMessage());
        }
    }

    // 2. 관리자 강제 정산 (취소 거절/거래 확정)
    public void adminForceTrade(int orderId) {
        try {
            ordersService.adminForceTrade(orderId, "ADMIN");
        } catch (SQLException e) {
            System.out.println("\n❌ 처리 실패: " + e.getMessage());
        }
    }
    
    public List<Orders> getOrdersForAdmin() {
        try {
            return ordersService.getOrdersForAdmin();
        } catch (SQLException e) {
            // SQL 관련 에러만 따로 로깅하거나 처리
            System.out.println("❌ [DB 에러] 관리자 목록 로딩 실패: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}