package view;

import dto.Orders;
import java.util.List;

/**
 * 관리자 취소요청 View (SCR-020)
 * - 취소요청 목록 및 상세 화면 출력
 * - CANCEL_REQUEST(구매자 요청) / CANCEL_REJECTED(판매자 거절) 구분 표시
 */
public class AdminCancelView {

    /**
     * 취소요청 목록 출력 (SCR-020)
     * - 주문번호를 선택자로 사용 (ordersId 직접 입력)
     *
     * @param cancelOrders 취소요청 주문 목록
     */
    public static void printCancelRequestList(List<Orders> cancelOrders) {
        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("  🔧  취소요청 목록");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.printf("  %-8s %-20s %-12s %-12s %-30s%n",
                "주문번호", "상품명", "구매자", "금액", "처리구분");
        System.out.println("  ──────────────────────────────────────────────────────────────────────────────");

        if (cancelOrders == null || cancelOrders.isEmpty()) {
            System.out.println("\n  처리할 취소요청이 없습니다.");
        } else {
            for (Orders order : cancelOrders) {
                System.out.printf("  [%-6d] %-20s %-12s %,10d원  %s%n",
                        order.getOrdersId(),
                        CommonView.truncateSimple(order.getProductTitle(), 15),
                        maskUserId(order.getBuyerId()),
                        order.getProductPrice(),
                        getCancelProcessType(order.getStatus()));
            }
        }

        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("  0. 돌아가기");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.print("  주문번호 선택 > ");
    }

    /**
     * 취소요청 상세 및 승인 화면 출력 (SCR-020)
     *
     * @param order 선택한 취소요청 주문
     */
    public static void printCancelRequestDetail(Orders order) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  🔧  취소요청 상세");
        System.out.println("════════════════════════════════════════");
        System.out.println("  상품명    :  " + order.getProductTitle());
        System.out.println("  구매자    :  " + maskUserId(order.getBuyerId()));
        System.out.println("  금액      :  " + String.format("%,d원", order.getProductPrice()));
        System.out.println("  처리구분  :  " + getCancelProcessTypeLabel(order.getStatus()));
        System.out.println("  상태      :  " + order.getStatus());
        System.out.println("════════════════════════════════════════");
        System.out.println("  1. 취소승인(환불)    0. 돌아가기");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

    /**
     * 취소 승인 직접 처리 헤더 출력
     * - 메뉴 5번 "취소 승인" 단축 경로용
     */
    public static void printDirectApproveHeader() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  🔧  취소 승인 처리");
        System.out.println("════════════════════════════════════════");
        System.out.println("  처리할 주문번호를 입력하세요.");
        System.out.println();
    }

    /**
     * 주문 상태에 따른 처리구분 텍스트 반환 (목록 표시용)
     * - CANCEL_REQUEST  → "(구매자 요청 / CANCEL_REQUEST)"
     * - CANCEL_REJECTED → "(판매자 거절 / CANCEL_REJECTED)"
     */
    public static String getCancelProcessType(String status) {
        if ("CANCEL_REQUEST".equals(status)) {
            return "(구매자 요청 / CANCEL_REQUEST)";
        } else if ("CANCEL_REJECTED".equals(status)) {
            return "(판매자 거절 / CANCEL_REJECTED)";
        }
        return "(" + status + ")";
    }

    /**
     * 주문 상태에 따른 처리구분 레이블 반환 (상세 표시용)
     * - CANCEL_REQUEST  → "구매자 요청"
     * - CANCEL_REJECTED → "판매자 거절"
     */
    public static String getCancelProcessTypeLabel(String status) {
        if ("CANCEL_REQUEST".equals(status)) {
            return "구매자 요청";
        } else if ("CANCEL_REJECTED".equals(status)) {
            return "판매자 거절";
        }
        return status;
    }

    /**
     * 아이디 마스킹 처리
     * - 앞 4자리 표시, 나머지는 * 처리
     * - 예: hong123 → hong***  |  kim456 → kim4**
     */
    public static String maskUserId(String userId) {
        if (userId == null || userId.length() <= 4) {
            return userId;
        }
        String visible = userId.substring(0, 4);
        return visible + "*".repeat(userId.length() - 4);
    }
}
