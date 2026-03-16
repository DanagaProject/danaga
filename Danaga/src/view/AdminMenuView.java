package view;

import dto.Category;
import dto.Notification;
import dto.Orders;
import dto.Product;
import dto.User;
import util.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.ProductController;

/**
 * 관리자 메뉴 View (SCR-019)
 * - 관리자 메인 메뉴 흐름 제어
 * - 상품 조회 / 거래 관리 / 회원 관리 / 카테고리 관리 / 알림
 */
public class AdminMenuView {
    private Scanner sc;

    public AdminMenuView(Scanner sc) {
        this.sc = sc;
    }

    // ============================================================================
    // 관리자 메인 메뉴 (SCR-019)
    // ============================================================================

    /**
     * 관리자 메인 메뉴 출력 및 처리 (SCR-019)
     */
    public void printAdminMenu() {
        while (true) {
            // TODO: Controller 연동 - 안읽은 알림 개수 조회
            // int unreadCount = notificationController.getUnreadCount(SessionManager.getCurrentUserId());
            int unreadCount = getSampleUnreadNotifications().size();

            System.out.println("\n════════════════════════════════════════");
            System.out.println("  💻  중고 컴퓨터 거래");
            System.out.println("  🔧  관리자       🔔  새 알림 " + unreadCount + "건");
            System.out.println("════════════════════════════════════════");
            System.out.println("  [상품 조회]");
            System.out.println("  1. 상품목록  2. 카테고리 검색  3. 상품명 검색");
            System.out.println("════════════════════════════════════════");
            System.out.println("  [거래 관리]");
            System.out.println("  4.  취소요청 목록");
            System.out.println("  5.  취소 승인");
            System.out.println("════════════════════════════════════════");
            System.out.println("  [회원 관리]");
            System.out.println("  6.  회원 목록 / 차단 관리");
            System.out.println("════════════════════════════════════════");
            System.out.println("  [카테고리 관리]");
            System.out.println("  7. 목록  8. 추가  9. 수정  10. 삭제");
            System.out.println("════════════════════════════════════════");
            System.out.println("  11. 알림 확인    12. 로그아웃");
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
                    updateCategory();
                    break;
                case "10":
                    deleteCategory();
                    break;
                case "11":
                    viewNotifications();
                    break;
                case "12":
                    System.out.println("\n로그아웃 되었습니다.");
                    SessionManager.logout();
                    return; // 메인 메뉴로 돌아감
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // ============================================================================
    // 상품 조회 (관리자 - 모든 상태 조회 가능)
    // ============================================================================

    /**
     * 상품 전체 목록 보기
     * - 관리자는 ON_SALE / RESERVED / COMPLETED 모든 상태 조회 가능
     */
    private void viewAllProducts() {
        // TODO: Controller 연동 - 전체 상품 조회 (모든 상태 포함)
        // List<Product> products = productController.getAllProductsForAdmin();
        List<Product> products = ProductController.adminProductSelectAll();

        ProductView.printProductList(products);

        System.out.print("\n번호입력(상세보기) (0: 뒤로가기) > ");
        String input = sc.nextLine().trim();

        if (!"0".equals(input)) {
            try {
                int productId = Integer.parseInt(input);
                ProductView.showProductDetail(sc, products, productId);
            } catch (NumberFormatException e) {
                CommonView.printInvalidNumberMessage();
            }
        }
    }

    /**
     * 카테고리별 상품 검색
     */
    private void viewProductsByCategory() {
        // TODO: Controller 연동 - 카테고리 목록 조회
        // List<Category> categories = categoryController.getAllCategories();
        List<Category> categories = ProductController.categorySelectAll();

        ProductView.printCategoryList(categories);
        String categoryInput = sc.nextLine().trim();

        if ("0".equals(categoryInput)) {
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryInput);
            // TODO: Controller 연동 - 카테고리별 상품 조회 (모든 상태 포함, 유효하지 않은 ID면 빈 목록 반환)
            // List<Product> filteredProducts = productController.getProductsByCategoryForAdmin(categoryId);
            List<Product> filteredProducts = ProductController.adminProductSelectByCategory(categoryId);

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
            CommonView.printInvalidNumberMessage();
        }
    }

    /**
     * 상품명으로 검색
     */
    private void viewProductByName() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           상품명 검색");
        System.out.println("════════════════════════════════════════");
        System.out.print("  상품명 입력 (0: 취소) > ");
        String keyword = sc.nextLine().trim();

        if ("0".equals(keyword)) {
            return;
        }

        if (keyword.isEmpty()) {
            System.out.println("상품명을 입력해주세요.");
            return;
        }

        // TODO: Controller 연동 - 상품명으로 조회 (모든 상태 포함)
        // List<Product> searchResults = productController.searchProductsByNameForAdmin(keyword);
        List<Product> searchResults = ProductController.adminProductSelectByName(keyword);

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
                CommonView.printInvalidNumberMessage();
            }
        }
    }

    // ============================================================================
    // 거래 관리 (취소요청 처리)
    // ============================================================================

    /**
     * 취소요청 목록 보기 및 처리 (SCR-020)
     * - CANCEL_REQUEST(구매자 요청) / CANCEL_REJECTED(판매자 거절) 목록 표시
     * - 주문번호 선택 → 상세 → 취소 승인(환불)
     */
    private void viewCancelRequests() {
        while (true) {
            // TODO: Controller 연동 - 취소요청 목록 조회 (CANCEL_REQUEST + CANCEL_REJECTED)
            // List<Orders> cancelOrders = adminController.getCancelRequestOrders();
            List<Orders> cancelOrders = getSampleCancelOrders();

            AdminCancelView.printCancelRequestList(cancelOrders);
            String input = sc.nextLine().trim();

            if ("0".equals(input)) {
                return;
            }

            if (cancelOrders == null || cancelOrders.isEmpty()) {
                return;
            }

            try {
                int ordersId = Integer.parseInt(input);
                // TODO: Controller 연동 - 주문번호로 취소요청 상세 조회
                // Orders selectedOrder = adminController.getCancelOrderById(ordersId);
                Orders selectedOrder = getSampleCancelOrderById(ordersId);

                if (selectedOrder == null) {
                    System.out.println("해당 주문을 찾을 수 없습니다.");
                    continue;
                }

                AdminCancelView.printCancelRequestDetail(selectedOrder);
                String confirm = sc.nextLine().trim();

                if ("1".equals(confirm)) {
                    // TODO: Controller 연동 - 취소 승인 처리 (환불)
                    // adminController.approveCancelRequest(ordersId);
                    CommonView.printSuccessMessage("취소 승인 완료", "구매자에게 자동 환불 처리되었습니다.");
                    return;
                } else if ("0".equals(confirm)) {
                    // 상세에서 목록으로 돌아가기
                } else {
                    CommonView.printInvalidInputMessage();
                }

            } catch (NumberFormatException e) {
                CommonView.printInvalidNumberMessage();
            }
        }
    }

    /**
     * 취소 승인 단축 처리 (메뉴 5번)
     * - 주문번호 직접 입력하여 취소 승인
     */
    private void approveCancelRequest() {
        AdminCancelView.printDirectApproveHeader();
        System.out.print("  주문번호 입력 (0: 취소) > ");
        String input = sc.nextLine().trim();

        if ("0".equals(input)) {
            return;
        }

        try {
            int ordersId = Integer.parseInt(input);
            // TODO: Controller 연동 - 주문번호로 취소요청 상세 조회
            // Orders selectedOrder = adminController.getCancelOrderById(ordersId);
            Orders selectedOrder = getSampleCancelOrderById(ordersId);

            if (selectedOrder == null) {
                System.out.println("취소요청 상태의 주문을 찾을 수 없습니다.");
                return;
            }

            AdminCancelView.printCancelRequestDetail(selectedOrder);
            String confirm = sc.nextLine().trim();

            if ("1".equals(confirm)) {
                // TODO: Controller 연동 - 취소 승인 처리 (환불)
                // adminController.approveCancelRequest(ordersId);
                CommonView.printSuccessMessage("취소 승인 완료", "구매자에게 자동 환불 처리되었습니다.");
            } else if ("0".equals(confirm)) {
                System.out.println("\n취소 승인을 취소했습니다.");
            } else {
                CommonView.printInvalidInputMessage();
            }

        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        }
    }

    // ============================================================================
    // 회원 관리
    // ============================================================================

    /**
     * 회원 목록 조회 및 차단/해제 처리 (SCR-021)
     * - 아이디(userId) 직접 입력으로 선택
     * - 정상 회원 → 차단 / 차단 회원 → 차단해제
     */
    private void manageUsers() {
        while (true) {
            // TODO: Controller 연동 - 전체 회원 목록 조회
            // List<User> users = adminController.getAllUsers();
            List<User> users = getSampleUsers();

            AdminUserView.printUserList(users);
            String input = sc.nextLine().trim();

            if ("0".equals(input)) {
                return;
            }

            if (users == null || users.isEmpty()) {
                return;
            }

            // TODO: Controller 연동 - userId로 회원 상세 조회
            // User selectedUser = adminController.getUserById(input);
            User selectedUser = getSampleUserById(input);

            if (selectedUser == null) {
                System.out.println("해당 아이디를 찾을 수 없습니다.");
                continue;
            }

            AdminUserView.printUserDetail(selectedUser);
            String confirm = sc.nextLine().trim();

            if ("1".equals(confirm)) {
                boolean isBanned = "BANNED".equals(selectedUser.getStatus());
                if (isBanned) {
                    // TODO: Controller 연동 - 차단 해제
                    // adminController.unbanUser(selectedUser.getUserId());
                    CommonView.printSuccessMessage("차단 해제 완료",
                            selectedUser.getUserId() + " 계정이 정상으로 변경되었습니다.");
                } else {
                    // TODO: Controller 연동 - 차단
                    // adminController.banUser(selectedUser.getUserId());
                    CommonView.printSuccessMessage("차단 완료",
                            selectedUser.getUserId() + " 계정이 차단되었습니다.");
                }
                return;
            } else if ("0".equals(confirm)) {
                // 상세에서 목록으로 돌아가기
            } else {
                CommonView.printInvalidInputMessage();
            }
        }
    }

    // ============================================================================
    // 카테고리 관리
    // ============================================================================

    /**
     * 카테고리 목록 보기 (메뉴 7번)
     */
    private void listCategories() {
        // TODO: Controller 연동 - 카테고리 목록 조회
        // List<Category> categories = categoryController.getAllCategories();
        List<Category> categories = ProductController.categorySelectAll();

        System.out.println("\n════════════════════════════════════════");
        System.out.println("  📁  카테고리 목록");
        System.out.println("════════════════════════════════════════");
        for (Category c : categories) {
            System.out.println("  [" + c.getCategoryId() + "]  " + c.getName());
        }
        System.out.println("════════════════════════════════════════");
        CommonView.waitForBack(sc);
    }

    /**
     * 카테고리 추가 (메뉴 8번)
     */
    private void addCategory() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           카테고리 추가");
        System.out.println("════════════════════════════════════════");
        System.out.print("  추가할 카테고리 이름 입력 (0: 취소) > ");
        String name = sc.nextLine().trim();

        if ("0".equals(name)) {
            return;
        }

        if (name.isEmpty()) {
            System.out.println("카테고리 이름을 입력해주세요.");
            return;
        }

        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ⚠  카테고리 추가 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  추가할 카테고리  :  " + name);
        System.out.println("════════════════════════════════════════");
        System.out.println("  1. 확인    0. 취소");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
        String confirm = sc.nextLine().trim();

        if ("1".equals(confirm)) {
            // TODO: Controller 연동 - 카테고리 추가
            // adminController.addCategory(name);
        	ProductController.categoryInsert(name);
            CommonView.printSuccessMessage("카테고리 등록 완료", "'" + name + "' 카테고리가 추가되었습니다.");
        } else if ("0".equals(confirm)) {
            System.out.println("\n카테고리 추가를 취소했습니다.");
        } else {
            CommonView.printInvalidInputMessage();
        }
    }

    /**
     * 카테고리 수정 (메뉴 9번)
     * - categoryId를 직접 입력하여 선택 후 이름 수정
     */
    private void updateCategory() {
        // TODO: Controller 연동 - 카테고리 목록 조회
        // List<Category> categories = categoryController.getAllCategories();
        List<Category> categories = ProductController.categorySelectAll();

        System.out.println("\n════════════════════════════════════════");
        System.out.println("           카테고리 수정");
        System.out.println("════════════════════════════════════════");
        System.out.println("  수정할 카테고리 번호를 입력하세요.");
        System.out.println();
        for (Category c : categories) {
            System.out.println("  [" + c.getCategoryId() + "]  " + c.getName());
        }
        System.out.println("════════════════════════════════════════");
        System.out.println("  0. 취소");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
        String input = sc.nextLine().trim();

        if ("0".equals(input)) {
            return;
        }

        try {
            int categoryId = Integer.parseInt(input);
            Category selectedCategory = findCategoryById(categories, categoryId);

            if (selectedCategory == null) {
                System.out.println("잘못된 카테고리 번호입니다.");
                return;
            }

            System.out.println("\n════════════════════════════════════════");
            System.out.println("  현재 카테고리 이름  :  " + selectedCategory.getName());
            System.out.println("════════════════════════════════════════");
            System.out.print("  새로운 카테고리 이름 입력 (0: 취소) > ");
            String newName = sc.nextLine().trim();

            if ("0".equals(newName)) {
                return;
            }

            if (newName.isEmpty()) {
                System.out.println("카테고리 이름을 입력해주세요.");
                return;
            }

            System.out.println("\n════════════════════════════════════════");
            System.out.println("  ⚠  카테고리 수정 확인");
            System.out.println("════════════════════════════════════════");
            System.out.println("  기존 이름  :  " + selectedCategory.getName());
            System.out.println("  새 이름    :  " + newName);
            System.out.println("════════════════════════════════════════");
            System.out.println("  1. 확인    0. 취소");
            System.out.println("════════════════════════════════════════");
            System.out.print("  선택 > ");
            String confirm = sc.nextLine().trim();

            if ("1".equals(confirm)) {
                // TODO: Controller 연동 - 카테고리 수정
                // adminController.updateCategory(categoryId, newName);
            	ProductController.categoryUpdate(selectedCategory);
                CommonView.printSuccessMessage("카테고리 수정 완료", "'" + selectedCategory.getName() + "' → '" + newName + "'로 변경되었습니다.");
            } else if ("0".equals(confirm)) {
                System.out.println("\n카테고리 수정을 취소했습니다.");
            } else {
                CommonView.printInvalidInputMessage();
            }

        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        }
    }
    
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

    /**
     * 카테고리 삭제 (메뉴 10번)
     * - categoryId를 직접 입력하여 선택
     */
    private void deleteCategory() {
        // TODO: Controller 연동 - 카테고리 목록 조회
        // List<Category> categories = categoryController.getAllCategories();
        List<Category> categories = ProductController.categorySelectAll();

        System.out.println("\n════════════════════════════════════════");
        System.out.println("           카테고리 삭제");
        System.out.println("════════════════════════════════════════");
        System.out.println("  삭제할 카테고리 번호를 입력하세요.");
        System.out.println();
        for (Category c : categories) {
            System.out.println("  [" + c.getCategoryId() + "]  " + c.getName());
        }
        System.out.println("════════════════════════════════════════");
        System.out.println("  0. 취소");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
        String input = sc.nextLine().trim();

        if ("0".equals(input)) {
            return;
        }

        try {
            int categoryId = Integer.parseInt(input);           
            Category selectedCategory = findCategoryById(categories,categoryId);

            /*//서비스에서 객체가 존재여부에 따라 예외처리 중
            if (selectedCategory == null) {
                System.out.println("잘못된 카테고리 번호입니다.");
                return;
            }*/

            System.out.println("\n════════════════════════════════════════");
            System.out.println("  ⚠  카테고리 삭제 확인");
            System.out.println("════════════════════════════════════════");
            System.out.println("  삭제할 카테고리  :  [" + selectedCategory.getCategoryId() + "]  " + selectedCategory.getName());
            System.out.println("════════════════════════════════════════");
            System.out.println("  1. 확인    0. 취소");
            System.out.println("════════════════════════════════════════");
            System.out.print("  선택 > ");
            String confirm = sc.nextLine().trim();

            if ("1".equals(confirm)) {
                // TODO: Controller 연동 - 카테고리 삭제
                // adminController.deleteCategory(categoryId);
            	ProductController.categoryDelete(categoryId);
                CommonView.printSuccessMessage("카테고리 삭제 완료", "'" + selectedCategory.getName() + "' 카테고리가 삭제되었습니다.");
            } else if ("0".equals(confirm)) {
                System.out.println("\n카테고리 삭제를 취소했습니다.");
            } else {
                CommonView.printInvalidInputMessage();
            }

        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        }
    }

    // ============================================================================
    // 알림 확인 (SCR-018 재사용 - MyPageView와 동일한 패턴)
    // ============================================================================

    /**
     * 알림 확인 (전체 목록)
     * - NotificationView 재사용
     */
    private void viewNotifications() {
        while (true) {
            // TODO: Controller 연동 - 전체 알림 조회
            // List<Notification> list = notificationController.getNotifications(SessionManager.getCurrentUserId());
            List<Notification> list = getSampleNotifications();

            NotificationView.printNotificationList(list);
            String input = sc.nextLine().trim();

            switch (input) {
                case "1":
                    viewUnreadNotifications();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    /**
     * 안읽은 알림 보기 및 읽음 처리
     * - NotificationView 재사용
     */
    private void viewUnreadNotifications() {
        while (true) {
            // TODO: Controller 연동 - 안읽은 알림 조회
            // List<Notification> unreadList = notificationController.getUnreadNotifications(SessionManager.getCurrentUserId());
            List<Notification> unreadList = getSampleUnreadNotifications();

            NotificationView.printUnreadNotificationList(unreadList);
            String input = sc.nextLine().trim();

            if ("0".equals(input)) {
                return;
            }

            try {
                int notificationId = Integer.parseInt(input);
                // TODO: Controller 연동 - 읽음 처리
                // notificationController.markAsRead(notificationId);
                CommonView.printSuccessMessage("알림을 읽음 처리했습니다.");
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }

    // ============================================================================
    // 샘플 데이터 (View 테스트용 - 추후 Controller로 대체)
    // ============================================================================

    /**
     * 전체 상품 샘플 데이터 (관리자용 - 모든 상태 포함)
     * - 일반 사용자는 ON_SALE만 조회, 관리자는 모든 상태 조회 가능
     */
    private List<Product> getSampleAllStatusProducts() {
        List<Product> products = new ArrayList<>();

        Product p1 = new Product(1, "user01", 1, "LG 그램 17인치 노트북", 1200000,
                "2023년 구입. 상태 매우 좋습니다.", 1, 10, "2024-03-01", "N");
        p1.setCategoryName("노트북");
        p1.setItemCondition("상");
        p1.setStatus("ON_SALE");
        products.add(p1);

        Product p2 = new Product(2, "user02", 7, "RTX 4070 Ti SUPER", 1100000,
                "ASUS ROG Strix RTX 4070 Ti SUPER.", 1, 11, "2024-03-02", "N");
        p2.setCategoryName("GPU");
        p2.setItemCondition("상");
        p2.setStatus("RESERVED");
        products.add(p2);

        Product p3 = new Product(3, "user03", 5, "로지텍 MX Master 3", 80000,
                "로지텍 MX Master 3 무선 마우스.", 2, 9, "2024-03-03", "N");
        p3.setCategoryName("마우스");
        p3.setItemCondition("중");
        p3.setStatus("COMPLETED");
        products.add(p3);

        Product p4 = new Product(4, "user04", 6, "인텔 i9-13900K", 650000,
                "미개봉 신품 인텔 i9-13900K.", 1, 10, "2024-03-04", "N");
        p4.setCategoryName("CPU");
        p4.setItemCondition("상");
        p4.setStatus("ON_SALE");
        products.add(p4);

        return products;
    }

    /**
     * 취소요청 샘플 데이터
     * - CANCEL_REQUEST (구매자 요청) / CANCEL_REJECTED (판매자 거절) 포함
     */
    private List<Orders> getSampleCancelOrders() {
        List<Orders> orders = new ArrayList<>();

        Orders o1 = new Orders(1, 1, "hong123", 6, "2024-03-15 10:30:00");
        o1.setProductTitle("RTX 4080");
        o1.setProductPrice(500000);
        o1.setSellerId("user02");
        o1.setStatus("CANCEL_REQUEST");
        orders.add(o1);

        Orders o2 = new Orders(2, 4, "kim4567", 8, "2024-03-14 14:20:00");
        o2.setProductTitle("32GB DDR5");
        o2.setProductPrice(80000);
        o2.setSellerId("user03");
        o2.setStatus("CANCEL_REJECTED");
        orders.add(o2);

        return orders;
    }

    /**
     * 회원 샘플 데이터
     * - ACTIVE (정상) / BANNED (차단) 회원 포함
     */
    private List<User> getSampleUsers() {
        List<User> users = new ArrayList<>();

        User u1 = new User("hong123", "pass", 1000000, 12, "USER");
        u1.setStatus("ACTIVE");
        users.add(u1);

        User u2 = new User("kim456", "pass", 500000, 12, "USER");
        u2.setStatus("ACTIVE");
        users.add(u2);

        User u3 = new User("lee789", "pass", 0, 13, "USER");
        u3.setStatus("BANNED");
        users.add(u3);

        return users;
    }

    /**
     * 카테고리 샘플 데이터
     */
    private List<Category> getSampleCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "노트북"));
        categories.add(new Category(2, "데스크탑"));
        categories.add(new Category(3, "모니터"));
        categories.add(new Category(4, "키보드"));
        categories.add(new Category(5, "마우스"));
        categories.add(new Category(6, "기타"));
        return categories;
    }

    /**
     * 전체 알림 샘플 데이터 (View 테스트용)
     */
    private List<Notification> getSampleNotifications() {
        List<Notification> list = new ArrayList<>();
        list.add(new Notification(1, SessionManager.getCurrentUserId(),
                "[관리자] 취소요청 1건이 접수되었습니다.", "0", "2024-03-15"));
        list.add(new Notification(2, SessionManager.getCurrentUserId(),
                "[관리자] 신규 회원가입 2건이 확인되었습니다.", "0", "2024-03-14"));
        list.add(new Notification(3, SessionManager.getCurrentUserId(),
                "[시스템] 관리자 로그인 알림.", "1", "2024-03-01"));
        return list;
    }

    /**
     * 안읽은 알림 샘플 데이터 (View 테스트용)
     */
    private List<Notification> getSampleUnreadNotifications() {
        List<Notification> list = new ArrayList<>();
        list.add(new Notification(1, SessionManager.getCurrentUserId(),
                "[관리자] 취소요청 1건이 접수되었습니다.", "0", "2024-03-15"));
        list.add(new Notification(2, SessionManager.getCurrentUserId(),
                "[관리자] 신규 회원가입 2건이 확인되었습니다.", "0", "2024-03-14"));
        return list;
    }

    // ============================================================================
    // 샘플 단건 조회 (Controller.getXxxById() 동작을 시뮬레이션 - 추후 Controller로 대체)
    // ============================================================================

    /**
     * 주문번호로 취소요청 단건 조회 샘플
     * TODO: adminController.getCancelOrderById(ordersId) 로 교체
     */
    private Orders getSampleCancelOrderById(int ordersId) {
        for (Orders order : getSampleCancelOrders()) {
            if (order.getOrdersId() == ordersId) {
                return order;
            }
        }
        return null;
    }

    /**
     * userId로 회원 단건 조회 샘플
     * TODO: adminController.getUserById(userId) 로 교체
     */
    private User getSampleUserById(String userId) {
        if (userId == null) return null;
        for (User user : getSampleUsers()) {
            if (userId.equals(user.getUserId())) {
                return user;
            }
        }
        return null;
    }

    /**
     * categoryId로 카테고리 단건 조회 샘플
     * TODO: adminController.getCategoryById(categoryId) 로 교체
     */
    private Category getSampleCategoryById(int categoryId) {
        for (Category category : getSampleCategories()) {
            if (category.getCategoryId() == categoryId) {
                return category;
            }
        }
        return null;
    }
}
