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
	    System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
	    System.out.println("  🛒  내 구매 현황");
	    System.out.println("════════════════════════════════════════════════════════════════════════════════");

	    if (orders == null || orders.isEmpty()) {
	        System.out.println("\n  구매 내역이 없습니다.\n");
	        System.out.println("════════════════════════════════════════════════════════════════════════════════");
	        return;
	    }

	    System.out.printf("  %-8s %-22s %-12s %-12s %-12s %-12s%n",
	            "주문번호", "상품명", "결제금액", "판매자", "주문상태", "주문일");
	    System.out.println("────────────────────────────────────────────────────────────────────────────────");

	    for (Orders order : orders) {
	        String statusText = "";
	        // 주문 상태 번호(status_id)에 따른 한글 매핑
	        switch (order.getStatusId()) {
	            case 4: statusText = "결제완료"; break; // PENDING (판매자 확인 전)
	            case 5: statusText = "배송중"; break;   // SHIPPING
	            case 6: statusText = "취소요청중"; break; // CANCEL_REQUEST
	            case 7: statusText = "취소완료"; break; // CANCEL_COMPLETED
	            case 8: statusText = "취소거절"; break; // CANCEL_REJECTED
	            case 9: statusText = "구매확정"; break; // COMPLETED (최종 완료)
	            default: statusText = "상태확인"; break;
	        }

	        System.out.printf("  %-9d %-22s %,10d원   %-13s %-13s %-12s%n",
	                order.getOrdersId(),
	                CommonView.truncateSimple(order.getProductTitle(), 18),
	                order.getProductPrice(),
	                order.getSellerId() != null ? order.getSellerId() : "-",
	                statusText, // 변환된 한글 상태 출력
	                order.getCreatedAt() != null ? order.getCreatedAt().substring(0, 10) : "-");
	    }

	    System.out.println("════════════════════════════════════════════════════════════════════════════════");
	    System.out.println("  총 " + orders.size() + "건의 구매 내역이 있습니다.");
	    System.out.println("════════════════════════════════════════════════════════════════════════════════");
	}
    /**
     * 주문 상세 정보 출력
     */
	public static void printPurchaseDetail(Orders order) {
	    if (order == null) {
	        System.out.println("\n  ❌ 주문 정보를 불러올 수 없습니다.");
	        return;
	    }

	    System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
	    System.out.println("  📄  주문 상세 정보");
	    System.out.println("════════════════════════════════════════════════════════════════════════════════");

	    // 상태 코드 번호에 따른 한글 명칭 결정
	    String statusText = "";
	    switch (order.getStatusId()) {
	        case 4: statusText = "결제완료 (배송 대기 중)"; break;
	        case 5: statusText = "배송 중 (상품이 이동 중입니다)"; break;
	        case 6: statusText = "취소 요청 중"; break;
	        case 7: statusText = "취소 완료 (환불됨)"; break;
	        case 8: statusText = "취소 거절 (관리자 문의 필요)"; break;
	        case 9: statusText = "구매 확정 (거래 종료)"; break;
	        default: statusText = "상태 확인 불가 (" + order.getStatusId() + ")"; break;
	    }

	    System.out.println("  주문 번호    : " + order.getOrdersId());
	    System.out.println("  상품 번호    : " + order.getProductId());
	    System.out.println("  상품명       : " + (order.getProductTitle() != null ? order.getProductTitle() : "-"));
	    System.out.println("  결제 금액    : " + String.format("%,d원", order.getProductPrice()));
	    System.out.println("  판매자 ID    : " + (order.getSellerId() != null ? order.getSellerId() : "-"));
	    System.out.println("  현재 상태    : " + statusText); // 변환된 한글 상태 출력
	    System.out.println("  주문 일시    : " + (order.getCreatedAt() != null ? order.getCreatedAt() : "-"));
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
