package view;

import dto.Category;
import dto.Product;
import util.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.ProductController;
import controller.UserController;

/**
 * 일반 사용자 메뉴 View
 * - 로그인한 회원이 사용하는 메뉴
 */
public class UserMenuView {
    private Scanner sc;
    private MyPageView myPageView;
    private UserController userController = new controller.UserController();
    public UserMenuView(Scanner sc) {
        this.sc = sc;
        this.myPageView = new MyPageView(sc);
    }

    /**
     * 사용자 메뉴 출력 및 처리 (SCR-009)
     */
    public void printUserMenu() {
        while (true) {
        	userController.refreshCurrentSession();
            System.out.println("\n════════════════════════════════════════");
            System.out.println("  💻  중고 컴퓨터 거래 사이트");
            System.out.println("════════════════════════════════════════");
            System.out.println("  👤  " + SessionManager.getCurrentUserId() + " 님 환영합니다!");
            System.out.println("  💰  잔액:  " + BalanceView.formatBalance(SessionManager.getCurrentUser().getBalance())
                    + "  |  🔔  알림");
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
                    viewProductByName();
                    break;
                case "4":
                    myPageView.printMyPage();
                    break;
                case "5":
                    logout();
                    return; // 메인 메뉴로 돌아감
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    /**
     * 로그아웃
     */
    private void logout() {
        System.out.println("\n로그아웃 되었습니다.");
        SessionManager.logout();
    }

    /**
     * 상품 전체 목록 보기
     */
    private void viewAllProducts() {
        List<Product> products = ProductController.productSelectAll();
        ProductView.printProductList(products);

        if (products.isEmpty()) {
            System.out.println("\n해당 카테고리에 상품이 없습니다.");
            return;
        }
        
        System.out.print("\n번호입력(상세보기) (0: 뒤로가기) > ");
        String input = sc.nextLine().trim();
        
        if (!"0".equals(input)) {
            try {
                int productId = Integer.parseInt(input);
                ProductView.showProductDetail(sc, products, productId);
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다.");
            }
        }
    }

    /**
     * 카테고리별 검색
     */
    private void viewProductsByCategory() {
        // 카테고리 목록 조회 (추후 DAO에서 조회)
        List<Category> categories = ProductController.categorySelectAll();

        ProductView.printCategoryList(categories);
        String categoryInput = sc.nextLine().trim();

        if ("0".equals(categoryInput)) {
            return; // 뒤로가기
        }

        try {
            int categoryId = Integer.parseInt(categoryInput);

            // 입력한 ID가 유효한지 확인
            Category selectedCategory = findCategoryById(categories, categoryId);
            if (selectedCategory == null) {
                System.out.println("잘못된 카테고리 번호입니다.");
                return;
            }

            List<Product> filteredProducts = ProductController.productSelectByCategory(categoryId);
            if (filteredProducts.isEmpty()) {
                System.out.println("\n해당 카테고리에 상품이 없습니다.");
                return;
            }

            ProductView.printProductList(filteredProducts);

            System.out.print("\n번호입력(상세보기) (0: 뒤로가기) > ");
            String input = sc.nextLine().trim();

            if (!"0".equals(input)) {
                int productId = Integer.parseInt(input);
                ProductView.showProductDetail(sc, filteredProducts, productId);
            }
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다.");
        }
    }

    /**
     * 상품명으로 검색
     */
    private void viewProductByName() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           상품명 검색");
        System.out.println("════════════════════════════════════════");
        System.out.print("  상품명 입력 > ");
        String productName = sc.nextLine().trim();

        if (productName.isEmpty()) {
            System.out.println("상품명을 입력해주세요.");
            return;
        }

        List<Product> searchResults = ProductController.productSelectByName(productName);
        if (searchResults.isEmpty()) {
            System.out.println("\n검색된 상품이 없습니다.");
            return;
        }

        ProductView.printProductList(searchResults);

        System.out.print("\n번호입력(상세보기) (0: 뒤로가기) > ");
        String input = sc.nextLine().trim();

        if (!"0".equals(input)) {
            try {
                int productId = Integer.parseInt(input);
                ProductView.showProductDetail(sc, searchResults, productId);
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
            }
        }
    }

    /**
     * 샘플 상품 데이터 생성 (View 테스트용)
     */
    /*private List<Product> getSampleProducts() {
        List<Product> products = new ArrayList<>();

        // 샘플 상품 1 - 노트북
        Product p1 = new Product(1, "user01", 1, "LG 그램 17인치 노트북", 1200000,
                "2023년 구입한 LG 그램 노트북입니다. 거의 사용하지 않아 상태 좋습니다.",
                1, 10, "2024-03-01", "N");
        p1.setCategoryName("노트북");
        p1.setItemCondition("상");
        p1.setStatus("ON_SALE");
        products.add(p1);

        // 샘플 상품 2 - 데스크탑
        Product p2 = new Product(2, "user02", 2, "게이밍 데스크탑 RTX3080", 2500000,
                "RTX 3080 탑재 게이밍 데스크탑입니다. CPU: i7-12700K, RAM: 32GB",
                1, 10, "2024-03-02", "N");
        p2.setCategoryName("데스크탑");
        p2.setItemCondition("상");
        p2.setStatus("ON_SALE");
        products.add(p2);

        // 샘플 상품 3 - 모니터
        Product p3 = new Product(3, "user03", 3, "삼성 27인치 커브드 모니터", 350000,
                "삼성 27인치 커브드 모니터. 작은 스크래치 있으나 작동 양호합니다.",
                2, 11, "2024-03-03", "N");
        p3.setCategoryName("모니터");
        p3.setItemCondition("중");
        p3.setStatus("RESERVED");
        products.add(p3);

        // 샘플 상품 4 - 키보드
        Product p4 = new Product(4, "user01", 4, "레오폴드 FC660C 무접점", 250000,
                "레오폴드 무접점 키보드. 깔끔하게 사용했습니다.",
                1, 10, "2024-03-04", "N");
        p4.setCategoryName("키보드");
        p4.setItemCondition("상");
        p4.setStatus("ON_SALE");
        products.add(p4);

        // 샘플 상품 5 - 마우스
        Product p5 = new Product(5, "user04", 5, "로지텍 MX Master 3", 80000,
                "로지텍 MX Master 3 무선 마우스. 사용감 있습니다.",
                2, 9, "2024-03-05", "N");
        p5.setCategoryName("마우스");
        p5.setItemCondition("중");
        p5.setStatus("COMPLETED");
        products.add(p5);

        // 샘플 상품 6 - CPU
        Product p6 = new Product(6, "user05", 6, "인텔 i9-13900K", 650000,
                "미개봉 신품 인텔 i9-13900K CPU입니다.",
                1, 10, "2024-03-06", "N");
        p6.setCategoryName("CPU");
        p6.setItemCondition("상");
        p6.setStatus("ON_SALE");
        products.add(p6);

        // 샘플 상품 7 - GPU
        Product p7 = new Product(7, "user02", 7, "RTX 4070 Ti SUPER", 1100000,
                "ASUS ROG Strix RTX 4070 Ti SUPER. 보증기간 2년 남음.",
                1, 10, "2024-03-07", "N");
        p7.setCategoryName("GPU");
        p7.setItemCondition("상");
        p7.setStatus("ON_SALE");
        products.add(p7);

        // 샘플 상품 8 - RAM
        Product p8 = new Product(8, "user03", 8, "삼성 DDR5 32GB 램", 180000,
                "삼성 DDR5 32GB (16GBx2) 메모리. 정상작동 확인.",
                2, 10, "2024-03-08", "N");
        p8.setCategoryName("RAM");
        p8.setItemCondition("중");
        p8.setStatus("ON_SALE");
        products.add(p8);

        // 샘플 상품 9 - 저장장치
        Product p9 = new Product(9, "user06", 9, "삼성 990 PRO 2TB SSD", 280000,
                "삼성 990 PRO 2TB NVMe SSD. 사용량 10% 미만.",
                1, 11, "2024-03-09", "N");
        p9.setCategoryName("저장장치");
        p9.setItemCondition("상");
        p9.setStatus("RESERVED");
        products.add(p9);

        // 샘플 상품 10 - 기타
        Product p10 = new Product(10, "user07", 10, "USB 허브 7포트", 25000,
                "7포트 USB 3.0 허브. 포장 상태 양호.",
                2, 10, "2024-03-10", "N");
        p10.setCategoryName("기타");
        p10.setItemCondition("중");
        p10.setStatus("ON_SALE");
        products.add(p10);

        return products;
    }*/

    /**
     * 샘플 필터링된 상품 데이터 생성 (View 테스트용)
     */
    /*private List<Product> getSampleFilteredProducts() {
        List<Product> products = new ArrayList<>();

        // 샘플 상품 1 - 노트북
        Product p1 = new Product(1, "user01", 1, "LG 그램 17인치 노트북", 1200000,
                "2023년 구입한 LG 그램 노트북입니다. 거의 사용하지 않아 상태 좋습니다.",
                1, 10, "2024-03-01", "N");
        p1.setCategoryName("노트북");
        p1.setItemCondition("상");
        p1.setStatus("ON_SALE");
        products.add(p1);

        // 샘플 상품 2 - 노트북
        Product p2 = new Product(2, "user02", 1, "LG 그램 18인치 노트북", 1500000,
                "2024년 구입한 LG 그램 노트북입니다. 거의 사용하지 않아 상태 좋습니다.",
                1, 10, "2024-03-01", "N");
        p2.setCategoryName("노트북");
        p2.setItemCondition("중");
        p2.setStatus("ON_SALE");
        products.add(p2);

        // 샘플 상품 3 - 노트북
        Product p3 = new Product(3, "user03", 1, "LG 그램 19인치 노트북", 1800000,
                "2025년 구입한 LG 그램 노트북입니다. 거의 사용하지 않아 상태 좋습니다.",
                1, 10, "2024-03-01", "N");
        p3.setCategoryName("노트북");
        p3.setItemCondition("상");
        p3.setStatus("ON_SALE");
        products.add(p3);

        return products;
    }*/

    /**
     * 카테고리 목록 샘플 데이터 (View 테스트용)
     * 추후 Controller/Service를 통해 실제 데이터로 대체
     */
    /*private List<Category> getSampleCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "노트북"));
        categories.add(new Category(2, "데스크탑"));
        categories.add(new Category(3, "모니터"));
        categories.add(new Category(4, "키보드"));
        categories.add(new Category(5, "마우스"));
        categories.add(new Category(6, "CPU"));
        categories.add(new Category(7, "GPU"));
        categories.add(new Category(8, "RAM"));
        categories.add(new Category(9, "저장장치"));
        categories.add(new Category(10, "기타"));
        return categories;
    }*/

    /**
     * ID로 카테고리 찾기
     */
    private Category findCategoryById(List<Category> categories, int categoryId) {
        if (categories == null) {
            return null;
        }
        for (Category category : categories) {
            if (category.getCategoryId() == categoryId) {
                return category;
            }
        }
        return null;
    }

}
