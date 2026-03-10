package view;

import util.SessionManager;
import java.util.Scanner;

/**
 * 마이페이지 View (SCR-010)
 */
public class MyPageView {
    private Scanner sc;

    public MyPageView(Scanner sc) {
        this.sc = sc;
    }

    /**
     * 마이페이지 메뉴
     */
    public void printMyPage() {
        while (true) {
            System.out.println("\n  👤  마이페이지  [" + SessionManager.getCurrentUserId() + " 님]");
            System.out.println("  💰  잔액 : 표시 예정");
            System.out.println("════════════════════════════════════════");
            System.out.println("  1.  내 구매 현황");
            System.out.println("  2.  내 판매 현황");
            System.out.println("  3.  내 판매 상품 관리");
            System.out.println("  4.  상품 등록");
            System.out.println("  5.  즐겨찾기 카테고리 관리");
            System.out.println("  6.  알림 확인");
            System.out.println("  7.  잔액 충전");
            System.out.println("  0.  돌아가기");
            System.out.println("════════════════════════════════════════");
            System.out.print("  선택 > ");

            String menu = sc.nextLine().trim();

            switch (menu) {
                case "1":
                    viewPurchaseHistory();
                    break;
                case "2":
                    viewSaleHistory();
                    break;
                case "3":
                    manageMyProducts();
                    break;
                case "4":
                    registerProduct();
                    break;
                case "5":
                    manageFavorites();
                    break;
                case "6":
                    viewNotifications();
                    break;
                case "7":
                    chargeBalance();
                    break;
                case "0":
                    return; // 이전 메뉴로 돌아감
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private void viewPurchaseHistory() {
        System.out.println("[TODO] 내 구매 현황 기능 구현 예정");
    }

    private void viewSaleHistory() {
        System.out.println("[TODO] 내 판매 현황 기능 구현 예정");
    }

    private void manageMyProducts() {
        System.out.println("[TODO] 내 판매 상품 관리 기능 구현 예정");
    }

    private void registerProduct() {
        System.out.println("[TODO] 상품 등록 기능 구현 예정");
    }

    private void manageFavorites() {
        System.out.println("[TODO] 즐겨찾기 카테고리 관리 기능 구현 예정");
    }

    private void viewNotifications() {
        System.out.println("[TODO] 알림 확인 기능 구현 예정");
    }

    private void chargeBalance() {
        System.out.println("[TODO] 잔액 충전 기능 구현 예정");
    }
}
