package view;

import controller.AuthController;
import java.util.Scanner;

/**
 * 비로그인 사용자 메뉴 View
 */
public class GuestMenuView {
    private Scanner sc;
    private AuthController authController;

    public GuestMenuView(Scanner sc) {
        this.sc = sc;
        this.authController = new AuthController();
    }

    /**
     * 게스트 메뉴 출력 및 처리
     */
    public void printGuestMenu() {
        while (true) {
            System.out.println("\n════════════════════════════════════════");
            System.out.println("  💻  중고 컴퓨터 거래 사이트");
            System.out.println("════════════════════════════════════════");
            System.out.println("  1.  상품 전체 목록 보기");
            System.out.println("  2.  카테고리별 검색");
            System.out.println("  3.  상품 검색");
            System.out.println("  4.  로그인");
            System.out.println("  5.  회원가입");
            System.out.println("  6.  종료");
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
                    login();
                    return; // 로그인 성공 시 메인 메뉴로 돌아감
                case "5":
                    signup();
                    break;
                case "6":
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    /**
     * 로그인
     */
    private void login() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           로그인");
        System.out.println("════════════════════════════════════════");
        System.out.print("  아이디: ");
        String userId = sc.nextLine().trim();
        System.out.print("  비밀번호: ");
        String password = sc.nextLine().trim();

        boolean success = authController.login(userId, password);
        if (!success) {
            // 로그인 실패 시 재시도 메시지
            System.out.println("다시 시도하려면 메뉴에서 로그인을 선택해주세요.");
        }
    }

    /**
     * 회원가입
     */
    private void signup() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           회원가입");
        System.out.println("════════════════════════════════════════");
        System.out.print("  아이디: ");
        String userId = sc.nextLine().trim();
        System.out.print("  비밀번호: ");
        String password = sc.nextLine().trim();
        System.out.print("  비밀번호 확인: ");
        String passwordConfirm = sc.nextLine().trim();

        // 비밀번호 확인
        if (!password.equals(passwordConfirm)) {
            FailView.printMessage("비밀번호가 일치하지 않습니다.");
            return;
        }

        authController.signup(userId, password);
    }

    /**
     * 상품 전체 목록 보기
     */
    private void viewAllProducts() {
        System.out.println("[TODO] 상품 전체 목록 기능 구현 예정");
    }

    /**
     * 카테고리별 검색
     */
    private void viewProductsByCategory() {
        System.out.println("[TODO] 카테고리별 검색 기능 구현 예정");
    }

    /**
     * 상품번호로 검색
     */
    private void viewProductById() {
        System.out.println("[TODO] 상품번호 검색 기능 구현 예정");
    }
}
