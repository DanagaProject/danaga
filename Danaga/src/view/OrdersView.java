package view;

import dto.Product;

public class OrdersView {
	// 구매 성공 안내
    public static void printPurchaseSuccess() {
        System.out.println("\n✅ 구매 신청이 완료되었습니다! 마이페이지를 확인하세요.");
    }

    // 구매 실패 (잔액 부족 등) 에러 메시지
    public static void printPurchaseFail(String errorMessage) {
        System.out.println("\n❌ 구매 실패: " + errorMessage);
    }

    // 구매 취소 안내
    public static void printPurchaseCanceled() {
        System.out.println("\n구매가 취소되었습니다.");
    }
    // 구매 신청 전 최종 확인 화면
    public static void printOrderConfirm(Product product) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           🛒 구매 신청 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  상품명 : " + product.getTitle());
        System.out.println("  가  격 : " + String.format("%,d원", product.getPrice()));
        System.out.println("  판매자 : " + product.getSellerId());
        System.out.println("════════════════════════════════════════");
        System.out.print("\n정말 구매하시겠습니까? (1.확인  0.취소) > ");
    }

    public static void printSelfPurchaseError() {
        System.out.println("\n❌ 본인이 등록한 상품은 구매할 수 없습니다.");
    }

    public static void printNotForSaleError() {
        System.out.println("\n❌ 현재 구매 가능한 상품이 아닙니다.");
    }
}