package controller;

import dto.Orders;
import service.OrdersService;
import service.OrdersServiceImpl;

public class OrdersController {
    private final OrdersService ordersService = new OrdersServiceImpl();

    /**
     * 주문 신청 처리
     */
    public boolean placeOrder(Orders order) {
        try {
            // 우리가 이전에 테스트했던 그 서비스 메서드입니다!
            int result = ordersService.ordersInsert(order);
            return result > 0;
        } catch (Exception e) {
            // 서비스에서 던진 "잔액 부족" 등의 에러 메시지를 여기서 출력
            System.out.println("\n[오류] " + e.getMessage());
            return false;
        }
    }
}