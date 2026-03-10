package view;

import util.SessionManager;
import java.util.Scanner;

/**
 * 메인 메뉴 라우팅 View
 * 권한별로 적절한 메뉴를 보여줌
 */
public class MenuView {
    private Scanner sc;
    private GuestMenuView guestMenu;
    private UserMenuView userMenu;
    private AdminMenuView adminMenu;

    public MenuView() {
        this.sc = new Scanner(System.in);
        this.guestMenu = new GuestMenuView(sc);
        this.userMenu = new UserMenuView(sc);
        this.adminMenu = new AdminMenuView(sc);
    }

    /**
     * 메인 메뉴 시작
     * 로그인 상태에 따라 적절한 메뉴로 라우팅
     */
    public void showMenu() {
        while (true) {
            if (!SessionManager.isLoggedIn()) {
                // 비로그인 사용자
                guestMenu.printGuestMenu();
            } else if (SessionManager.isAdmin()) {
                // 관리자
                adminMenu.printAdminMenu();
            } else {
                // 일반 사용자
                userMenu.printUserMenu();
            }
        }
    }

    /**
     * Scanner 종료
     */
    public void close() {
        if (sc != null) {
            sc.close();
        }
    }
}
