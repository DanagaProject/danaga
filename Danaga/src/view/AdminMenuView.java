package view;

import util.SessionManager;
import java.util.Scanner;

/**
 * 관리자 메뉴 View (SCR-019)
 */
public class AdminMenuView {
    private Scanner sc;

    public AdminMenuView(Scanner sc) {
        this.sc = sc;
    }

    /**
     * 관리자 메뉴 출력 및 처리
     */
    public void printAdminMenu() {
        while (true) {
            System.out.println("\n  💻  중고 컴퓨터 거래");
            System.out.println("  🔧  관리자       🔔  새 알림 표시 예정");
            System.out.println("════════════════════════════════════════");
            System.out.println("  [상품 조회]");
            System.out.println("  1. 상품 목록  2. 카테고리 검색  3. 상품 검색");
            System.out.println("════════════════════════════════════════");
            System.out.println("  [거래 관리]");
            System.out.println("  4.  취소요청 목록");
            System.out.println("  5.  취소 승인");
            System.out.println("════════════════════════════════════════");
            System.out.println("  [회원 관리]");
            System.out.println("  6.  회원 목록 / 차단 관리");
            System.out.println("════════════════════════════════════════");
            System.out.println("  [카테고리 관리]");
            System.out.println("  7. 목록  8. 추가  9. 삭제");
            System.out.println("════════════════════════════════════════");
            System.out.println("  10. 알림 확인    11. 로그아웃");
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
                    viewCancelRequests();
                    break;
                case "5":
                    approveCancelRequest();
                    break;
                case "6":
                    manageUsers();
                    break;
                case "7":
                    listCategories();
                    break;
                case "8":
                    addCategory();
                    break;
                case "9":
                    deleteCategory();
                    break;
                case "10":
                    viewNotifications();
                    break;
                case "11":
                    SessionManager.logout();
                    return; // 메인 메뉴로 돌아감
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private void viewAllProducts() {
        System.out.println("[TODO] 상품목록 기능 구현 예정");
    }

    private void viewProductsByCategory() {
        System.out.println("[TODO] 카테고리검색 기능 구현 예정");
    }

    private void viewProductById() {
        System.out.println("[TODO] 번호검색 기능 구현 예정");
    }

    private void viewCancelRequests() {
        System.out.println("[TODO] 취소요청 목록 기능 구현 예정");
    }

    private void approveCancelRequest() {
        System.out.println("[TODO] 취소 승인 기능 구현 예정");
    }

    private void manageUsers() {
        System.out.println("[TODO] 회원 목록 / 차단 관리 기능 구현 예정");
    }

    private void listCategories() {
        System.out.println("[TODO] 카테고리 목록 기능 구현 예정");
    }

    private void addCategory() {
        System.out.println("[TODO] 카테고리 추가 기능 구현 예정");
    }

    private void deleteCategory() {
        System.out.println("[TODO] 카테고리 삭제 기능 구현 예정");
    }

    private void viewNotifications() {
        System.out.println("[TODO] 알림 확인 기능 구현 예정");
    }
}
