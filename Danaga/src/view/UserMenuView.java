package view;

import util.SessionManager;
import java.util.Scanner;

/**
 * 일반 사용자 메뉴 View
 */
public class UserMenuView {
    private Scanner sc;
    private MyPageView myPageView;

    public UserMenuView(Scanner sc) {
        this.sc = sc;
        this.myPageView = new MyPageView(sc);
    }

    /**
     * 사용자 메뉴 출력 및 처리 (SCR-009)
     */
    public void printUserMenu() {
        while (true) {
            System.out.println("\n  💻  중고 컴퓨터 거래");
            System.out.println("  👤  " + SessionManager.getCurrentUserId() + " 님  💰  잔액 표시 예정");
            System.out.println("  🔔  새 알림 표시 예정");
            System.out.println("════════════════════════════════════════");
            System.out.println("  [상품 조회]");
            System.out.println("  1.  상품 전체 목록 보기");
            System.out.println("  2.  카테고리별 검색");
            System.out.println("  3.  상품명으로 검색");
            System.out.println("  [기타]");
            System.out.println("  4.  마이페이지");
            System.out.println("  5.  로그아웃");
            System.out.println("════════════════════════════════════════");
            System.out.print("  선택 > ");

            String menu = sc.nextLine().trim();

            switch (menu) {
                case "1":
                    viewAllProducts();
                    break;
                case "2":
                    viewProductsByCategory();
                    break;
                case "3":
                    viewProductById();
                    break;
                case "4":
                    myPageView.printMyPage();
                    break;
                case "5":
                    SessionManager.logout();
                    return; // 메인 메뉴로 돌아감
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private void viewAllProducts() {
        System.out.println("[TODO] 상품 전체 목록 기능 구현 예정");
    }

    private void viewProductsByCategory() {
        System.out.println("[TODO] 카테고리별 검색 기능 구현 예정");
    }

    private void viewProductById() {
        System.out.println("[TODO] 상품번호 검색 기능 구현 예정");
    }
}
