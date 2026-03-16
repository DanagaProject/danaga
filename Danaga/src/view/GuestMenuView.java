package view;

import controller.AuthController;
import controller.ProductController;
import dto.Category;
import dto.Product;
import java.util.ArrayList;
import java.util.List;
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
            System.out.println("  3.  상품명으로 검색");
            System.out.println("  4.  로그인");
            System.out.println("  5.  회원가입");
            System.out.println("  6.  종료");
            System.out.println("════════════════════════════════════════");
            System.out.print("  선택 > ");

            String menu = sc.nextLine().trim();

            switch (menu) {
                case "1":
                    if (viewAllProducts()) {
                        return; // 로그인 성공 시 메인 메뉴로 돌아감
                    }
                    break;
                case "2":
                    if (viewProductsByCategory()) {
                        return; // 로그인 성공 시 메인 메뉴로 돌아감
                    }
                    break;
                case "3":
                    if (viewProductByName()) {
                        return; // 로그인 성공 시 메인 메뉴로 돌아감
                    }
                    break;
                case "4":
                    if (login()) {
                        return; // 로그인 성공 시 메인 메뉴로 돌아감
                    }
                    break;
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
     * @return 로그인 성공 여부
     */
    private boolean login() {
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
        return success;
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
     * @return 로그인 성공 여부
     */
    private boolean viewAllProducts() {
        List<Product> products = ProductController.productSelectAll();
        ProductView.printProductList(products); // 상품 목록 표 형식 출력

        System.out.print("\n번호입력(상세보기) (0: 뒤로가기) > ");
        String input = sc.nextLine().trim();

        if (!"0".equals(input)) {
            try {
                int productId = Integer.parseInt(input);
                boolean needLogin = ProductView.showProductDetail(sc, products, productId);

                if (needLogin) {
                    boolean loginSuccess = login(); // 로그인 페이지로 이동
                    if (loginSuccess) {
                        // 로그인 성공 시 다시 상품 상세보기 (이번에는 로그인 상태)
                        ProductView.showProductDetail(sc, products, productId);
                        // 상품 상세에서 돌아온 후, GuestMenuView를 빠져나가서 UserMenuView로 이동
                        return true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다.");
            }
        }
        return false;
    }

    /**
     * 카테고리별 검색
     * @return 로그인 성공 여부
     */
    private boolean viewProductsByCategory() {
    	//나중에 카테고리조회 구현
    	List<Category> allCategories = ProductController.categorySelectAll();
        ProductView.printCategoryList(allCategories); // 카테고리 목록 출력
        String categoryInput = sc.nextLine().trim();

        if ("0".equals(categoryInput)) {
            return false; // 뒤로가기
        }

        try {
            int categoryId = Integer.parseInt(categoryInput);
            if (categoryId < 1 || categoryId > 10) {
                System.out.println("잘못된 카테고리 번호입니다.");
                return false;
            }

            List<Product> filteredProducts = ProductController.productSelectByCategory(categoryId);
            //filteredProducts.clear(); //조회내용 없을시 테스트용
            if (filteredProducts.isEmpty()) {
                System.out.println("\n해당 카테고리에 상품이 없습니다.");
                return false;
            }

            ProductView.printProductList(filteredProducts); // 카테고리별 상품 목록 출력

            System.out.print("\n번호입력(상세보기) (0: 뒤로가기) > ");
            String input = sc.nextLine().trim();

            if (!"0".equals(input)) {
                int productId = Integer.parseInt(input);
                boolean needLogin = ProductView.showProductDetail(sc, filteredProducts, productId);

                if (needLogin) {
                    boolean loginSuccess = login(); // 로그인 페이지로 이동
                    if (loginSuccess) {
                        // 로그인 성공 시 다시 상품 상세보기 (이번에는 로그인 상태)
                        ProductView.showProductDetail(sc, filteredProducts, productId);
                        // 상품 상세에서 돌아온 후, GuestMenuView를 빠져나가서 UserMenuView로 이동
                        return true;
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다.");
        }
        return false;
    }

    /**
     * 상품명으로 검색
     * @return 로그인 성공 여부
     */
    private boolean viewProductByName() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           상품명 검색");
        System.out.println("════════════════════════════════════════");
        System.out.print("  상품명 입력 > ");
        String productName = sc.nextLine().trim();

        if (productName.isEmpty()) {
            System.out.println("상품명을 입력해주세요.");
            return false;
        }

        List<Product> searchResults = ProductController.productSelectByName(productName);
        //searchResults.clear(); //조회내용 없을시 테스트용
        if (searchResults.isEmpty()) {
            System.out.println("\n검색된 상품이 없습니다.");
            return false;
        }

        ProductView.printProductList(searchResults); // 검색된 상품 목록 출력

        System.out.print("\n번호입력(상세보기) (0: 뒤로가기) > ");
        String input = sc.nextLine().trim();

        if (!"0".equals(input)) {
            try {
                int productId = Integer.parseInt(input);
                boolean needLogin = ProductView.showProductDetail(sc, searchResults, productId);

                if (needLogin) {
                    boolean loginSuccess = login(); // 로그인 페이지로 이동
                    if (loginSuccess) {
                        // 로그인 성공 시 다시 상품 상세보기 (이번에는 로그인 상태)
                        ProductView.showProductDetail(sc, searchResults, productId);
                        // 상품 상세에서 돌아온 후, GuestMenuView를 빠져나가서 UserMenuView로 이동
                        return true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
            }
        }
        return false;
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
     * 샘플 상품 데이터 생성 (View 테스트용)
     */
    /*private List<Product> getSampleFilterdProducts() {
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
        Product p2 = new Product(1, "user02", 2, "LG 그램 18인치 노트북", 1200000,
                "2024년 구입한 LG 그램 노트북입니다. 거의 사용하지 않아 상태 좋습니다.",
                1, 10, "2024-03-01", "N");
        p2.setCategoryName("노트북");
        p2.setItemCondition("중");
        p2.setStatus("ON_SALE");
        products.add(p2);

        // 샘플 상품 3 - 노트북
        Product p3 = new Product(1, "user03", 3, "LG 그램 19인치 노트북", 1200000,
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
        categories.add(new Category(5, "기타"));
        return categories;
    }*/
}