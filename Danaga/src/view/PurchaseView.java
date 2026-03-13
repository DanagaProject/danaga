package view;

import dto.Orders;
import java.util.List;

/**
 * 구매/주문 관련 View
 * - 주문 목록, 상세 정보 출력 기능
 * - ProductView와 유사한 구조로 공통 출력 담당
 */
public class PurchaseView {

    /**
     * 구매 목록 출력 (표 형식)
     * 내 구매 현황 (SCR-011)
     */
    public static void printPurchaseList(List<Orders> orders) {
        if (orders == null || orders.isEmpty()) {
            System.out.println("\n구매 내역이 없습니다.");
            return;
        }

        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                  내 구매 현황                                   ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.printf("%-6s %-20s %-10s %-10s %-12s %-12s%n",
                "주문번호", "상품명", "가격", "판매자", "주문상태", "주문일");
        System.out.println("────────────────────────────────────────────────────────────────────────────────");

        for (Orders order : orders) {
            System.out.printf("%-6d %-20s %,10d원 %-10s %-12s %-12s%n",
                    order.getOrdersId(),
                    CommonView.truncateSimple(order.getProductTitle(), 15),
                    order.getProductPrice(),
                    order.getSellerId() != null ? order.getSellerId() : "-",
                    CommonView.getOrderStatusText(order.getStatus()),
                    order.getCreatedAt() != null ? order.getCreatedAt().substring(0, 10) : "-");
        }

        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("총 " + orders.size() + "건의 구매");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
    }

    /**
     * 주문 상세 정보 출력
     */
    public static void printPurchaseDetail(Orders order) {
        if (order == null) {
            System.out.println("\n주문을 찾을 수 없습니다.");
            return;
        }

        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                주문 상세 정보                                   ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("  주문번호    : " + order.getOrdersId());
        System.out.println("  상품번호    : " + order.getProductId());
        System.out.println("  상품명      : " + (order.getProductTitle() != null ? order.getProductTitle() : "-"));
        System.out.println("  가격        : " + String.format("%,d원", order.getProductPrice()));
        System.out.println("  판매자      : " + (order.getSellerId() != null ? order.getSellerId() : "-"));
        System.out.println("  주문상태    : " + CommonView.getOrderStatusText(order.getStatus()));
        System.out.println("  주문일      : " + (order.getCreatedAt() != null ? order.getCreatedAt() : "-"));
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
    }

    /**
     * 거래 확정 헤더 출력
     */
    public static void printConfirmPurchaseHeader() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           거래 확정");
        System.out.println("════════════════════════════════════════");
        System.out.println("확정할 상품 번호 입력");
        System.out.println();
    }

    /**
     * 취소 요청 헤더 출력
     */
    public static void printCancelRequestHeader() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           취소 요청");
        System.out.println("════════════════════════════════════════");
        System.out.println("취소 요청할 상품 번호 입력");
        System.out.println();
    }

    /**
     * 간단한 주문 목록 표시
     * - 주문번호와 상품명만 간단하게 표시
     */
    public static void printSimpleOrderList(List<Orders> orders) {
        for (Orders order : orders) {
            System.out.println("[" + order.getOrdersId() + "]  " +
                order.getProductTitle() + "  거래 진행중");
        }
    }

    /**
     * 거래 확정 확인 화면 출력 (SCR-012)
     */
    public static void printConfirmPurchaseScreen(Orders order) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ⚠  거래 확정 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  상품명    :  " + order.getProductTitle());
        System.out.println("  가격      :  " + String.format("%,d원", order.getProductPrice()));
        System.out.println("  판매자    :  " + order.getSellerId());
        System.out.println("════════════════════════════════════════");
        System.out.println();
        System.out.println("확정 후 취소 불가합니다");
        System.out.println();
        System.out.println("  1. 확정확인    0. 취소");
        System.out.println("════════════════════════════════════════");
    }

    /**
     * 취소 요청 확인 화면 출력 (SCR-013)
     */
    public static void printCancelRequestScreen(Orders order) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ⚠  취소 요청 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  상품명    :  " + order.getProductTitle());
        System.out.println("  가격      :  " + String.format("%,d원", order.getProductPrice()));
        System.out.println("════════════════════════════════════════");
        System.out.println();
        System.out.println("관리자 검토 후 확불됩니다");
        System.out.println();
        System.out.println("  1. 요청확인    0. 돌아가기");
        System.out.println("════════════════════════════════════════");
    }

    /**
     * 주문번호로 주문 찾기
     *
     * @param orders 주문 목록
     * @param ordersId 찾을 주문 번호
     * @return 찾은 주문 객체, 없으면 null
     */
    public static Orders findOrderById(List<Orders> orders, int ordersId) {
        if (orders == null) {
            return null;
        }
        for (Orders order : orders) {
            if (order.getOrdersId() == ordersId) {
                return order;
            }
        }
        return null;
    }
}
