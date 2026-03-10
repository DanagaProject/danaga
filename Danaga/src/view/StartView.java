package view;

/**
 * 프로그램 시작점
 */
public class StartView {

    public static void main(String[] args) {
        System.out.println("\n========================================");
        System.out.println("   다나가 (Danaga) 중고 거래 플랫폼");
        System.out.println("========================================\n");

        MenuView menuView = new MenuView();

        try {
            menuView.showMenu();
        } finally {
            menuView.close();
        }
    }
}
