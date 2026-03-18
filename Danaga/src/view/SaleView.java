package view;

import dto.Product;
import dto.Orders;
import java.util.List;

/**
 * 판매 관련 View
 * - 판매 상품 목록, 상세 정보 출력 기능
 * - 판매자 관련 화면 출력 담당
 */
public class SaleView {

    /**
     * 내 판매 현황 출력 (SCR-014)
     * - 거래중인 상품, 최근 완료된 상품 그룹으로 구분
     *
     * @param ongoingOrders 거래중인 주문 목록
     * @param completedOrders 최근 완료된 주문 목록
     */
    public static void printSaleHistory(List<Orders> ongoingOrders, List<Orders> completedOrders) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  💼  내 판매 현황");
        System.out.println("════════════════════════════════════════");
        System.out.println();

        // 거래중인 상품
        System.out.println("[거래중인 상품]");
        if (ongoingOrders == null || ongoingOrders.isEmpty()) {
            System.out.println("거래중인 상품이 없습니다.");
        } else {
            for (Orders order : ongoingOrders) {
                String statusText = getOrderActionText(order.getStatus());
                System.out.println("[" + order.getOrdersId() + "]  " +
                    order.getProductTitle() + "  " + statusText);
            }
        }

        System.out.println();

        // 최근 완료된 상품
        System.out.println("[최근 완료된 상품]");
        if (completedOrders == null || completedOrders.isEmpty()) {
            System.out.println("완료된 상품이 없습니다.");
        } else {
            for (Orders order : completedOrders) {
                String dateStr = order.getCreatedAt() != null ?
                    order.getCreatedAt().substring(5, 10) : "-";
                System.out.println("[" + order.getOrdersId() + "]  " +
                    order.getProductTitle() + "  거래 완료  " + dateStr);
            }
        }
    }

    /**
     * 판매 상품 관리 출력 (SCR-015)
     * - 판매중인 상품, 거래중인 상품, 최근 완료 그룹으로 구분
     *
     * @param onSaleProducts 판매중인 상품 목록
     * @param reservedProducts 거래중인 상품 목록
     * @param completedProducts 최근 완료된 상품 목록
     */
    public static void printProductManagement(List<Product> onSaleProducts,
                                               List<Product> reservedProducts,
                                               List<Product> completedProducts) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  📦  내 판매 상품 관리");
        System.out.println("════════════════════════════════════════");
        System.out.println();

        // 판매중인 상품
        System.out.println("[판매중인 상품]");
        if (onSaleProducts == null || onSaleProducts.isEmpty()) {
            System.out.println("판매중인 상품이 없습니다.");
        } else {
            for (Product product : onSaleProducts) {
                System.out.println("[" + product.getProductId() + "]  " +
                    product.getTitle() + "  " +
                    String.format("%,d", product.getPrice()) + "  판매중");
            }
        }

        System.out.println();

        // 거래중인 상품
        System.out.println("[거래중인 상품]");
        if (reservedProducts == null || reservedProducts.isEmpty()) {
            System.out.println("거래중인 상품이 없습니다.");
        } else {
            for (Product product : reservedProducts) {
                System.out.println("[" + product.getProductId() + "]  " +
                    product.getTitle() + "  거래 진행중");
            }
        }

        System.out.println();

        // 최근 완료
        System.out.println("[최근 완료]");
        if (completedProducts == null || completedProducts.isEmpty()) {
            System.out.println("완료된 상품이 없습니다.");
        } else {
            for (Product product : completedProducts) {
                System.out.println("[" + product.getProductId() + "]  " +
                    product.getTitle() + "  거래 완료");
            }
        }

        System.out.println();
        System.out.println("  1. 수정    2. 삭제    3. 전체이력    0. 돌아가기");
        System.out.println("════════════════════════════════════════");
        System.out.println("수정/삭제는 판매중 상태에서만 가능 | 거래중/완료 상품은 수정·삭제 불가");
        System.out.println();
        System.out.print("  선택 > ");
    }

    /**
     * 주문 상태에 따른 액션 텍스트 반환
     */
    private static String getOrderActionText(String status) {
        if (status == null) {
            return "거래 진행중";
        }
        switch (status) {
            case "PENDING":
                return "거래 진행중  ← 배송 처리 가능";
            case "CANCEL_REQUEST":
                return "취소 요청중  ← 취소 요청 처리 가능";
            case "SHIPPING":
                return "배송중";
            default:
                return "거래 진행중";
        }
    }

    /**
     * 배송 처리 헤더 출력
     */
    public static void printShippingProcessHeader() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           배송 처리");
        System.out.println("════════════════════════════════════════");
        System.out.println("배송 처리할 주문 번호 입력");
        System.out.println();
    }

    /**
     * 배송 처리 확인 화면
     */
    public static void printShippingProcessConfirm(Orders order) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  📦 배송 처리 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  주문번호  :  " + order.getOrdersId());
        System.out.println("  상품명    :  " + order.getProductTitle());
        System.out.println("  구매자    :  " + order.getBuyerId());
        System.out.println("════════════════════════════════════════");
        System.out.println();
        System.out.println("배송 처리하시겠습니까?");
        System.out.println();
        System.out.println("  1. 처리확인    0. 취소");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

    /**
     * 취소 요청 처리 헤더 출력
     */
    public static void printCancelRequestProcessHeader() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           취소 요청 처리");
        System.out.println("════════════════════════════════════════");
        System.out.println("처리할 취소 요청 번호 입력");
        System.out.println();
    }
    
    
    
    /**
     * 판매자 입장에서 구매 취소 동의 혹은 거절
     */
    public static void printSellerCancelDecisionScreen(Orders order) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  📩 구매자 취소 요청 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  상품명: " + order.getProductTitle());
        System.out.println("  금액: " + String.format("%,d원", order.getProductPrice()));
        System.out.println("════════════════════════════════════════");
        System.out.println("  1. 취소 동의 (즉시 환불 처리)");
        System.out.println("  2. 취소 거절 (관리자 중재 요청)");
        System.out.println("  0. 돌아가기");
        System.out.print("  선택 > ");
    }
    /**
     * 취소 요청 처리 확인 화면
     */
    public static void printCancelRequestProcessConfirm(Orders order) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ⚠  취소 요청 처리");
        System.out.println("════════════════════════════════════════");
        System.out.println("  주문번호  :  " + order.getOrdersId());
        System.out.println("  상품명    :  " + order.getProductTitle());
        System.out.println("  구매자    :  " + order.getBuyerId());
        System.out.println("════════════════════════════════════════");
        System.out.println();
        System.out.println("  1. 승인    2. 거부    0. 돌아가기");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

    /**
     * 전체 판매 이력 조회 (모든 주문 상태 포함)
     *
     * @param allOrders 모든 판매 주문 목록
     */
    public static void printAllSaleHistory(List<Orders> allOrders) {
        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                전체 판매 이력                                   ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");

        if (allOrders == null || allOrders.isEmpty()) {
            System.out.println("\n판매 이력이 없습니다.");
            System.out.println("════════════════════════════════════════════════════════════════════════════════");
            return;
        }

        System.out.printf("%-6s %-20s %-10s %-10s %-12s %-12s%n",
                "주문번호", "상품명", "가격", "구매자", "주문상태", "주문일");
        System.out.println("────────────────────────────────────────────────────────────────────────────────");

        for (Orders order : allOrders) {
            System.out.printf("%-6d %-20s %,10d원 %-10s %-12s %-12s%n",
                    order.getOrdersId(),
                    CommonView.truncateSimple(order.getProductTitle(), 15),
                    order.getProductPrice(),
                    order.getBuyerId() != null ? order.getBuyerId() : "-",
                    (order.getStatus() != null ? order.getStatus() : "-"),
                    order.getCreatedAt() != null ? order.getCreatedAt().substring(0, 10) : "-");
        }

        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("총 " + allOrders.size() + "건의 판매");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
    }

    /**
     * 전체 상품 이력 조회 (모든 상품 상태 포함)
     *
     * @param allProducts 모든 등록 상품 목록
     */
    public static void printAllProductHistory(List<Product> allProducts) {
        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                전체 상품 이력                                   ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");

        if (allProducts == null || allProducts.isEmpty()) {
            System.out.println("\n등록한 상품이 없습니다.");
            System.out.println("════════════════════════════════════════════════════════════════════════════════");
            return;
        }

        System.out.printf("%-6s %-20s %-10s %-8s %-10s %-12s%n",
                "번호", "제목", "가격", "상태", "판매상태", "등록일");
        System.out.println("────────────────────────────────────────────────────────────────────────────────");

        for (Product product : allProducts) {
            System.out.printf("%-6d %-20s %,10d원 %-8s %-10s %-12s%n",
                    product.getProductId(),
                    CommonView.truncate(product.getTitle(), 15),
                    product.getPrice(),
                    product.getItemCondition() != null ? product.getItemCondition() : "-",
                    CommonView.getProductStatusText(product.getStatus()),
                    product.getCreatedAt() != null ? product.getCreatedAt().substring(0, 10) : "-");
        }

        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("총 " + allProducts.size() + "개의 상품");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
    }

    /**
     * 상품 등록 헤더 출력
     */
    public static void printRegisterProductHeader() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           상품 등록");
        System.out.println("════════════════════════════════════════");
    }

    /**
     * 상품 등록 확인 화면
     * - 입력한 상품 정보 표시 및 등록 확인
     *
     * @param product 등록할 상품
     */
    public static void printRegisterProductConfirm(Product product) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ⚠  등록 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  제목        : " + product.getTitle());
        System.out.println("  가격        : " + String.format("%,d원", product.getPrice()));
        System.out.println("  상태        : " + (product.getItemCondition() != null ? product.getItemCondition() : "-"));
        System.out.println("  카테고리    : " + (product.getCategoryName() != null ? product.getCategoryName() : "-"));
        System.out.println("  설명        : " + (product.getDescription() != null ? product.getDescription() : "-"));
        System.out.println("════════════════════════════════════════");
        System.out.println();
        System.out.println("이대로 등록하시겠습니까?");
        System.out.println();
        System.out.println("  1. 등록확인    0. 취소");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

    /**
     * 상품 수정 헤더 출력
     */
    public static void printUpdateProductHeader() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           상품 수정");
        System.out.println("════════════════════════════════════════");
        System.out.println("수정할 상품 번호 입력");
        System.out.println();
    }

    /**
     * 상품 수정 상세 화면 (항목별 번호 포함)
     *
     * @param product 수정할 상품
     */
    public static void printUpdateProductDetail(Product product) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("          상품 수정");
        System.out.println("════════════════════════════════════════");
        System.out.println("  상품번호    : " + product.getProductId());
        System.out.println();
        System.out.println("  1. 제목        : " + product.getTitle());
        System.out.println("  2. 가격        : " + String.format("%,d원", product.getPrice()));
        System.out.println("  3. 상태        : " + (product.getItemCondition() != null ? product.getItemCondition() : "-"));
        System.out.println("  4. 카테고리    : " + (product.getCategoryName() != null ? product.getCategoryName() : "-"));
        System.out.println("  5. 설명        : " + (product.getDescription() != null ? product.getDescription() : "-"));
        System.out.println();
        System.out.println("  0. 수정 완료");
        System.out.println("════════════════════════════════════════");
        System.out.print("수정할 항목 번호 입력 (0: 완료) > ");
    }

    /**
     * 상품 수정 확인 화면
     * - 원본과 비교하여 변경된 항목 표시
     *
     * @param original 원본 상품
     * @param updated 수정된 상품
     */
    public static void printUpdateProductConfirm(Product original, Product updated) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ⚠  수정 확인");
        System.out.println("════════════════════════════════════════");

        // 제목
        if (!original.getTitle().equals(updated.getTitle())) {
            System.out.println("  제목        : " + updated.getTitle() + " (변경됨)");
        } else {
            System.out.println("  제목        : " + updated.getTitle());
        }

        // 가격
        if (original.getPrice() != updated.getPrice()) {
            System.out.println("  가격        : " + String.format("%,d원", updated.getPrice()) + " (변경됨)");
        } else {
            System.out.println("  가격        : " + String.format("%,d원", updated.getPrice()));
        }

        // 상태
        String origCondition = original.getItemCondition() != null ? original.getItemCondition() : "";
        String updCondition = updated.getItemCondition() != null ? updated.getItemCondition() : "";
        if (!origCondition.equals(updCondition)) {
            System.out.println("  상태        : " + updCondition + " (변경됨)");
        } else {
            System.out.println("  상태        : " + updCondition);
        }

        // 카테고리
        String origCategory = original.getCategoryName() != null ? original.getCategoryName() : "";
        String updCategory = updated.getCategoryName() != null ? updated.getCategoryName() : "";
        if (!origCategory.equals(updCategory)) {
            System.out.println("  카테고리    : " + updCategory + " (변경됨)");
        } else {
            System.out.println("  카테고리    : " + updCategory);
        }

        // 설명
        String origDesc = original.getDescription() != null ? original.getDescription() : "";
        String updDesc = updated.getDescription() != null ? updated.getDescription() : "";
        if (!origDesc.equals(updDesc)) {
            System.out.println("  설명        : " + updDesc + " (변경됨)");
        } else {
            System.out.println("  설명        : " + updDesc);
        }

        System.out.println("════════════════════════════════════════");
        System.out.println();
        System.out.println("이대로 수정하시겠습니까?");
        System.out.println();
        System.out.println("  1. 수정확인    0. 취소");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

    /**
     * 상품 삭제 헤더 출력
     */
    public static void printDeleteProductHeader() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           상품 삭제");
        System.out.println("════════════════════════════════════════");
        System.out.println("삭제할 상품 번호 입력");
        System.out.println();
    }

    /**
     * 상품 삭제 확인 화면
     */
    public static void printDeleteProductConfirm(Product product) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ⚠  상품 삭제 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  상품명    :  " + product.getTitle());
        System.out.println("  가격      :  " + String.format("%,d원", product.getPrice()));
        System.out.println("════════════════════════════════════════");
        System.out.println();
        System.out.println("정말 삭제하시겠습니까?");
        System.out.println();
        System.out.println("  1. 삭제확인    0. 취소");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

}
