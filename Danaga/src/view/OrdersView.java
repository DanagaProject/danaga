package view;

import java.util.Scanner;

import controller.OrdersController;
import dto.Product;
import util.SessionManager;

public class OrdersView {
	
	// 💡 ProductView에서 이사 온 구매 UI 전체 흐름 제어 로직
    public static boolean handlePurchaseRequest(Product product, Scanner sc) {
        // 본인 상품 구매 불가 검증
        if (product.getSellerId().equals(SessionManager.getCurrentUserId())) {
            printSelfPurchaseError();
            CommonView.pauseScreen(sc);
            return false;
        }

        // 판매 중 상태 검증 (10번 기준)
        if (product.getStatusId() != 10) { 
            printNotForSaleError();
            CommonView.pauseScreen(sc);
            return false;
        }

        // 구매 확인창 출력
        printOrderConfirm(product);
        String choice = sc.nextLine().trim();

        if ("1".equals(choice)) {
            // 💡 컨트롤러를 여기서 직접 호출하여 순수 비즈니스 로직(DB/세션)을 실행
            OrdersController ordersController = new OrdersController();
            boolean isSuccess = ordersController.requestPurchase(product.getProductId());
            
            if (isSuccess) {
                printPurchaseSuccess(); 
                CommonView.pauseScreen(sc);
                return true; 
            } else {
                printPurchaseFail("잔액이 부족하거나 시스템 오류가 발생했습니다."); 
                CommonView.pauseScreen(sc);
                return false;
            }
        }

        printPurchaseCanceled(); 
        return false;
    }
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