package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dto.Orders;
import dto.Product;
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
    public boolean processPurchaseRequest(Product product) {
        // 본인 상품 구매 불가 검증
        if (product.getSellerId().equals(SessionManager.getCurrentUserId())) {
            OrdersView.printSelfPurchaseError();
            CommonView.pauseScreen(sc);
            return false;
        }

        // 판매 중 상태 검증 (10번 기준)
        if (product.getStatusId() != 10) { 
            OrdersView.printNotForSaleError();
            CommonView.pauseScreen(sc);
            return false;
        }

        // 구매 확인창 출력 (뷰 호출)
        OrdersView.printOrderConfirm(product);
        String choice = sc.nextLine().trim();

        if ("1".equals(choice)) {
            try {
                Orders order = new Orders();
                order.setProductId(product.getProductId());
                order.setBuyerId(SessionManager.getCurrentUserId());

                // 서비스 호출 (실제 DB 처리)
                ordersService.ordersInsert(order);
                
                OrdersView.printPurchaseSuccess(); // 뷰 호출로 교체
                CommonView.pauseScreen(sc);
                return true; 
            } catch (SQLException e) {
                OrdersView.printPurchaseFail(e.getMessage()); // 뷰 호출로 교체
                CommonView.pauseScreen(sc);
                return false;
            }
        }

        OrdersView.printPurchaseCanceled(); // 뷰 호출로 교체
        return false;
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
            // 1. 서비스 계층 호출 (실제 환불 및 상태 변경 로직 실행)
            ordersService.cancelComplete(orderId);
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