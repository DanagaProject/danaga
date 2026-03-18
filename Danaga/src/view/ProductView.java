package view;

import dto.Category;
import dto.Code;
import dto.Comment;
import dto.Product;
import java.util.List;
import java.util.Scanner;

import controller.OrdersController;
import util.SessionManager;

/**
 * 상품 관련 View
 * - 상품 출력 기능 (목록, 상세, 카테고리)
 * - 상품 조회 공통 기능 (비회원/회원/관리자 모두 사용)
 */
public class ProductView {

    /**
     * 상품 목록 출력 (표 형식)
     */
    public static void printProductList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            System.out.println("\n조회된 상품이 없습니다.");
            return;
        }

        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                    상품 목록                                    ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.printf("%-6s %-20s %-10s %-8s %-10s %-10s%n",
                "번호", "제목", "가격", "상태", "카테고리", "판매자");
        System.out.println("────────────────────────────────────────────────────────────────────────────────");

        for (Product product : products) {
            System.out.printf("%-6d %-20s %,10d원 %-8s %-10s %-10s%n",
                    product.getProductId(),
                    CommonView.truncate(product.getTitle(), 20),
                    product.getPrice(),
                    product.getItemCondition() != null ? product.getItemCondition() : "-",
                    product.getCategoryName() != null ? product.getCategoryName() : "-",
                    product.getSellerId() != null ? product.getSellerId() : "-");
        }

        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("총 " + products.size() + "개의 상품");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
    }
    
    /**
     * 상품 목록 출력 (표 형식)
     */
    public static void printProductListForAdmin(List<Product> products) {
        if (products == null || products.isEmpty()) {
            System.out.println("\n조회된 상품이 없습니다.");
            return;
        }

        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                    상품 목록                                    ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.printf("%-6s %-20s %-10s %-8s %-10s %-10s %-10s%n",
                "번호", "제목", "가격", "상태", "카테고리", "판매자", "거래상태");
        System.out.println("────────────────────────────────────────────────────────────────────────────────");

        for (Product product : products) {
            System.out.printf("%-6d %-20s %,10d원 %-8s %-10s %-10s %-10s%n",
                    product.getProductId(),
                    CommonView.truncate(product.getTitle(), 20),
                    product.getPrice(),
                    product.getItemCondition() != null ? product.getItemCondition() : "-",
                    product.getCategoryName() != null ? product.getCategoryName() : "-",
                    product.getSellerId() != null ? product.getSellerId() : "-",
            		product.getStatus() != null ? product.getStatus() : "-");
        }

        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("총 " + products.size() + "개의 상품");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
    }

    /**
     * 상품 상세 정보 출력
     */
    public static void printProductDetail(Product product) {
        if (product == null) {
            System.out.println("\n상품을 찾을 수 없습니다.");
            return;
        }

        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                  상품 상세 정보                                  ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("  상품번호    : " + product.getProductId());
        System.out.println("  제목        : " + product.getTitle());
        System.out.println("  가격        : " + String.format("%,d원", product.getPrice()));
        System.out.println("  상태        : " + (product.getItemCondition() != null ? product.getItemCondition() : "-"));
        System.out.println("  판매상태    : " + (product.getStatus() != null ? product.getStatus() : "-") +
                ("Y".equals(product.getIsDeleted()) ? "  [삭제됨]" : ""));
        System.out.println("  카테고리    : " + (product.getCategoryName() != null ? product.getCategoryName() : "-"));
        System.out.println("  판매자      : " + (product.getSellerId() != null ? product.getSellerId() : "-"));
        System.out.println("  등록일      : " + (product.getCreatedAt() != null ? product.getCreatedAt() : "-"));
        System.out.println("────────────────────────────────────────────────────────────────────────────────");
        System.out.println("  설명        :");
        System.out.println("  " + (product.getDescription() != null ? product.getDescription() : "설명 없음"));
        System.out.println("════════════════════════════════════════════════════════════════════════════════");

        // 댓글 출력 기능
        System.out.println("\n[ 댓글 ]");
        controller.CommentController commentController = new controller.CommentController();
        List<Comment> comments = commentController.getCommentsByProductId(product.getProductId());
        
        if (comments.isEmpty()) {
            System.out.println("  등록된 댓글이 없습니다.");
        } else {
            for (Comment c : comments) {
                String createdAt = c.getCreatedAt();
                System.out.println("  [" +c.getCommentId() + "] " + c.getUserId() + " : " + c.getContent() + " [" + createdAt + "]");
            }
        }
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
    }

    /**
     * 카테고리 목록 출력
     *
     * @param categories 카테고리 목록
     */
    public static void printCategoryList(List<Category> categories) {
        System.out.println("\n════════════════════════════════════════════════════════════════");
        System.out.println("                        카테고리 선택                           ");
        System.out.println("════════════════════════════════════════════════════════════════");

        if (categories != null && !categories.isEmpty()) {
            for (Category category : categories) {
                System.out.println("  " + category.getCategoryId() + ". " + category.getName());
            }
        } else {
            System.out.println("  조회된 카테고리가 없습니다.");
        }

        System.out.println("  0. 뒤로가기");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.print("  카테고리 선택 > ");
    }

    /**
     * 상품 상태 목록 출력 (code 테이블에서 조회된 item_condition 그룹)
     * - 상품 등록/수정 시 상태 선택 화면
     *
     * @param conditions 상태 코드 목록 (CodeController에서 조회)
     */
    public static void printConditionList(List<Code> conditions) {
        System.out.println("\n════════════════════════════════════════════════════════════════");
        System.out.println("                        상태 선택                               ");
        System.out.println("════════════════════════════════════════════════════════════════");

        if (conditions != null && !conditions.isEmpty()) {
            for (Code code : conditions) {
                System.out.println("  " + code.getCodeId() + ". " + code.getName());
            }
        } else {
            System.out.println("  조회된 상태 정보가 없습니다.");
        }

        System.out.println("  0. 취소");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.print("  상태 선택 > ");
    }

    // ============================================================================
    // 상품 조회 공통 기능 (비회원/회원/관리자 모두 사용)
    // ============================================================================

    /**
     * 상품번호 입력 받아서 상세보기
     * - 사용자로부터 상품번호를 입력받고
     * - 해당 상품의 상세 정보를 출력
     *
     * @param sc Scanner 객체
     * @param products 상품 목록
     * @return 로그인이 필요한 경우 true, 아니면 false
     */
    public static boolean promptAndShowProductDetail(Scanner sc, List<Product> products) {
        System.out.print("\n번호입력(상세보기) (0: 뒤로가기) > ");
        String input = sc.nextLine().trim();

        if (!"0".equals(input)) {
            try {
                int productId = Integer.parseInt(input);
                return showProductDetail(sc, products, productId);
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다.");
            }
        }
        return false;
    }

    /**
     * 상품 상세 정보 출력 및 화면 대기
     * - 상품번호로 상품을 찾아 상세 정보 출력
     * - 로그인 여부에 따라 다른 안내 메시지 표시
     * - 출력 후 사용자가 Enter를 누를 때까지 대기
     *
     * @param sc Scanner 객체
     * @param products 상품 목록
     * @param productId 조회할 상품 번호
     * @return 로그인이 필요한 경우 true, 아니면 false
     */
    public static boolean showProductDetail(Scanner sc, List<Product> products, int productId) {
        Product product = findProductById(products, productId);
        if (product != null) {
            printProductDetail(product);

            // 비회원이면 로그인 유도 메뉴 표시
            if (!SessionManager.isLoggedIn()) {
                while (true) {
                    System.out.println("────────────────────────────────────────────────────────────────────────────────");
                    System.out.println("  ※ 이 상품을 구매하려면 로그인이 필요합니다.");
                    System.out.println("────────────────────────────────────────────────────────────────────────────────");
                    System.out.println("  1. 로그인");
                    System.out.println("  0. 돌아가기");
                    System.out.println("────────────────────────────────────────────────────────────────────────────────");
                    System.out.print("  선택 > ");
                    String choice = sc.nextLine().trim();

                    if ("1".equals(choice)) {
                        return true; // 로그인 필요
                    } else if ("0".equals(choice)) {
                        return false; // 돌아가기
                    } else {
                        System.out.println("잘못된 입력입니다. 1 또는 0을 입력해주세요.");
                    }
                }
            } else if (SessionManager.isAdmin()) {
                // 관리자는 안내 메시지만 표시
                printProductActionMenu();
                CommonView.pauseScreen(sc);
                return false;
            } else {
                // 일반 회원: 댓글 + 구매신청 메뉴 표시
                while (true) {
                    System.out.println("────────────────────────────────────────────────────────────────────────────────");
                    System.out.println("  1. 댓글 등록");
                    System.out.println("  2. 댓글 수정");
                    System.out.println("  3. 댓글 삭제");
                    System.out.println("  4. 구매신청");
                    System.out.println("  0. 돌아가기");
                    System.out.println("────────────────────────────────────────────────────────────────────────────────");
                    System.out.print("  선택 > ");
                    String choice = sc.nextLine().trim();

                    if ("1".equals(choice)) {
                        handleAddComment(product.getProductId(), sc);
                        // 댓글 목록 새로고침을 위해 상품 상세를 다시 출력
                        printProductDetail(product);
                    } else if ("2".equals(choice)) {
                        handleUpdateComment(product.getProductId(), sc);
                        // 댓글 목록 새로고침을 위해 상품 상세를 다시 출력
                        printProductDetail(product);
                    } else if ("3".equals(choice)) {
                        handleDeleteComment(product.getProductId(), sc);
                        // 댓글 목록 새로고침을 위해 상품 상세를 다시 출력
                        printProductDetail(product);
                    } else if ("4".equals(choice)) {
                        // 1. 컨트롤러 생성 (메서드 상단에 이미 선언되어 있다면 생략 가능)
                        OrdersController ordersController = new OrdersController();
                        
                        // 2. 컨트롤러에게 구매 프로세스 전권을 위임
                        // 성공 시 true(목록으로 이동), 취소/실패 시 false(상세화면 유지)
                        boolean isPurchased = ordersController.processPurchaseRequest(product);
                        
                        if (isPurchased) {
                            return false; // 구매 완료 후 상품 목록 화면으로 돌아감
                        } else {
                            printProductDetail(product); // 구매 취소 시 다시 상세 정보를 보여줌
                        }
                    } else if ("0".equals(choice)) {
                        return false;
                    } else {
                        System.out.println("잘못된 입력입니다.");
                    }
                }
            }
        } else {
            System.out.println("해당 상품을 찾을 수 없습니다.");
            return false;
        }
    }

    /**
     * 상품 상세보기 후 액션 메뉴 표시 (회원/관리자용)
     * - 회원: 구매 관련 안내
     * - 관리자: 관리 기능 안내
     */
    private static void printProductActionMenu() {
        System.out.println("────────────────────────────────────────────────────────────────────────────────");

        if (SessionManager.isAdmin()) {
            // 관리자
            System.out.println("  ※ 관리자 권한: 상품 수정/삭제 기능은 관리자 메뉴에서 이용 가능합니다.");
        } else {
            // 일반 회원
            System.out.println("  ※ 구매하기, 찜하기 등의 기능은 추후 구현 예정입니다.");
        }

        System.out.println("────────────────────────────────────────────────────────────────────────────────");
    }

    /**
     * 댓글 등록 처리
     *
     * @param productId 상품 ID
     * @param sc Scanner 객체
     */
    private static void handleAddComment(int productId, Scanner sc) {
        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                 댓글 등록                                       ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.print("  댓글 내용 (취소: 0) > ");

        String content = sc.nextLine().trim();

        if ("0".equals(content)) {
            System.out.println("댓글 등록이 취소되었습니다.");
            return;
        }

        if (content.isEmpty()) {
            System.out.println("댓글 내용을 입력해주세요.");
            return;
        }

        String userId = SessionManager.getCurrentUserId();
        controller.CommentController commentController = new controller.CommentController();
        commentController.addComment(productId, userId, content);
    }

    /**
     * 댓글 수정 처리
     *
     * @param productId 상품 ID
     * @param sc Scanner 객체
     */
    private static void handleUpdateComment(int productId, Scanner sc) {
        controller.CommentController commentController = new controller.CommentController();
        List<Comment> comments = commentController.getCommentsByProductId(productId);
        String currentUserId = SessionManager.getCurrentUserId();

        // 본인이 작성한 댓글 필터링
        List<Comment> myComments = new java.util.ArrayList<>();
        for (Comment c : comments) {
            if (currentUserId.equals(c.getUserId())) {
                myComments.add(c);
            }
        }

        if (myComments.isEmpty()) {
            System.out.println("\n수정할 수 있는 댓글이 없습니다.");
            return;
        }

        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                            내가 작성한 댓글                                     ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");

        for (int i = 0; i < myComments.size(); i++) {
            Comment c = myComments.get(i);
            String createdAt = c.getCreatedAt();
            if (createdAt != null && createdAt.length() > 19) {
                createdAt = createdAt.substring(0, 19);
            }
            System.out.println("  [" + (i + 1) + "] " + c.getContent() + " [" + createdAt + "]");
        }

        System.out.println("────────────────────────────────────────────────────────────────────────────────");
        System.out.print("  수정할 댓글 번호 (취소: 0) > ");

        String input = sc.nextLine().trim();

        if ("0".equals(input)) {
            return;
        }

        try {
            int index = Integer.parseInt(input) - 1;

            if (index < 0 || index >= myComments.size()) {
                System.out.println("잘못된 댓글 번호입니다.");
                return;
            }

            Comment selectedComment = myComments.get(index);

            System.out.println("\n현재 내용: " + selectedComment.getContent());
            System.out.print("새로운 내용 (취소: 0) > ");

            String newContent = sc.nextLine().trim();

            if ("0".equals(newContent)) {
                System.out.println("댓글 수정이 취소되었습니다.");
                return;
            }

            if (newContent.isEmpty()) {
                System.out.println("댓글 내용을 입력해주세요.");
                return;
            }

            System.out.print("수정하시겠습니까? (Y/N) > ");
            String confirm = sc.nextLine().trim().toUpperCase();

            if ("Y".equals(confirm)) {
                commentController.updateComment(selectedComment.getCommentId(), newContent);
            } else {
                System.out.println("댓글 수정이 취소되었습니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다.");
        }
    }

    /**
     * 댓글 삭제 처리
     *
     * @param productId 상품 ID
     * @param sc Scanner 객체
     */
    private static void handleDeleteComment(int productId, Scanner sc) {
        controller.CommentController commentController = new controller.CommentController();
        List<Comment> comments = commentController.getCommentsByProductId(productId);
        String currentUserId = SessionManager.getCurrentUserId();

        // 본인이 작성한 댓글 필터링
        List<Comment> myComments = new java.util.ArrayList<>();
        for (Comment c : comments) {
            if (currentUserId.equals(c.getUserId())) {
                myComments.add(c);
            }
        }

        if (myComments.isEmpty()) {
            System.out.println("\n삭제할 수 있는 댓글이 없습니다.");
            return;
        }

        System.out.println("\n════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                            내가 작성한 댓글                                     ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════");

        for (int i = 0; i < myComments.size(); i++) {
            Comment c = myComments.get(i);
            String createdAt = c.getCreatedAt();
            if (createdAt != null && createdAt.length() > 19) {
                createdAt = createdAt.substring(0, 19);
            }
            System.out.println("  [" + (i + 1) + "] " + c.getContent() + " [" + createdAt + "]");
        }

        System.out.println("────────────────────────────────────────────────────────────────────────────────");
        System.out.print("  삭제할 댓글 번호 (취소: 0) > ");

        String input = sc.nextLine().trim();

        if ("0".equals(input)) {
            return;
        }

        try {
            int index = Integer.parseInt(input) - 1;

            if (index < 0 || index >= myComments.size()) {
                System.out.println("잘못된 댓글 번호입니다.");
                return;
            }

            Comment selectedComment = myComments.get(index);

            System.out.println("\n삭제할 댓글: " + selectedComment.getContent());
            System.out.print("정말 삭제하시겠습니까? (Y/N) > ");

            String confirm = sc.nextLine().trim().toUpperCase();

            if ("Y".equals(confirm)) {
                commentController.deleteComment(selectedComment.getCommentId());
            } else {
                System.out.println("댓글 삭제가 취소되었습니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다.");
        }
    }

    /**
     * 상품번호로 상품 찾기
     *
     * @param products 상품 목록
     * @param productId 찾을 상품 번호
     * @return 찾은 상품 객체, 없으면 null
     */
    public static Product findProductById(List<Product> products, int productId) {
        for (Product product : products) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }
}
