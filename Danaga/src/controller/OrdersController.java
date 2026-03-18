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
    public void cancelComplete(int orderId) throws SQLException {
        ordersService.cancelComplete(orderId);
    }
}