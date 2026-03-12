package view;

import dto.Product;
import java.util.List;
import java.util.Scanner;
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
                    truncate(product.getTitle(), 20),
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
        System.out.println("  판매상태    : " + (product.getStatus() != null ? product.getStatus() : "-"));
        System.out.println("  카테고리    : " + (product.getCategoryName() != null ? product.getCategoryName() : "-"));
        System.out.println("  판매자      : " + (product.getSellerId() != null ? product.getSellerId() : "-"));
        System.out.println("  등록일      : " + (product.getCreatedAt() != null ? product.getCreatedAt() : "-"));
        System.out.println("────────────────────────────────────────────────────────────────────────────────");
        System.out.println("  설명        :");
        System.out.println("  " + (product.getDescription() != null ? product.getDescription() : "설명 없음"));
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
    }

    /**
     * 카테고리 목록 출력
     */
    public static void printCategoryList() {
        System.out.println("\n════════════════════════════════════════════════════════════════");
        System.out.println("                        카테고리 선택                           ");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("  1. 노트북");
        System.out.println("  2. 데스크탑");
        System.out.println("  3. 모니터");
        System.out.println("  4. 키보드");
        System.out.println("  5. 마우스");
        System.out.println("  0. 뒤로가기");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.print("  카테고리 선택 > ");
    }

    /**
     * 문자열 자르기 (표시용)
     * - 한글은 2칸, 영문/숫자는 1칸으로 계산하여 화면 폭 기준으로 자름
     * - maxWidth를 초과하면 "..."으로 나머지 문자열 표현
     *
     * @param str 자를 문자열
     * @param maxWidth 최대 화면 폭 (칸 수)
     * @return 잘린 문자열
     */
    private static String truncate(String str, int maxWidth) {
        if (str == null) {
            return "";
        }

        // 1단계: 전체 문자열의 화면 폭 계산
        int totalWidth = 0;
        for (int i = 0; i < str.length(); i++) {
            totalWidth += getCharWidth(str.charAt(i));
        }

        // 2단계: 전체 폭이 maxWidth 이하면 원본 그대로 반환
        if (totalWidth <= maxWidth) {
            return str;
        }

        // 3단계: 자를 필요가 있으면 maxWidth - 3(...)까지만 추가하고 "..." 붙임
        int currentWidth = 0;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int charWidth = getCharWidth(c);

            // 현재 문자를 추가하면 "..." 공간까지 고려해서 maxWidth 초과하는지 확인
            if (currentWidth + charWidth + 3 > maxWidth) {
                result.append("...");
                break;
            }

            result.append(c);
            currentWidth += charWidth;
        }

        return result.toString();
    }

    /**
     * 문자의 화면 폭 계산
     * - 한글, 한자, 일본어 등: 2칸
     * - 영문, 숫자, 기호 등: 1칸
     *
     * @param c 폭을 계산할 문자
     * @return 화면에서 차지하는 칸 수 (1 또는 2)
     */
    private static int getCharWidth(char c) {
        // 한글 음절 (가-힣): AC00-D7A3
        if (c >= 0xAC00 && c <= 0xD7A3) {
            return 2;
        }
        // 한글 자모 (ㄱ-ㅎ, ㅏ-ㅣ): 1100-11FF, 3130-318F
        if ((c >= 0x1100 && c <= 0x11FF) || (c >= 0x3130 && c <= 0x318F)) {
            return 2;
        }
        // CJK 통합 한자: 4E00-9FFF
        if (c >= 0x4E00 && c <= 0x9FFF) {
            return 2;
        }
        // 일본어 히라가나, 가타카나: 3040-30FF
        if (c >= 0x3040 && c <= 0x30FF) {
            return 2;
        }
        // 전각 기호 및 문자: FF00-FFEF
        if (c >= 0xFF00 && c <= 0xFFEF) {
            return 2;
        }
        // 그 외 ASCII 및 기본 문자는 1칸
        return 1;
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
                }
                return false;
            } else if (SessionManager.isAdmin()) {
                // 관리자는 안내 메시지만 표시
                printProductActionMenu();
                pauseScreen(sc);
                return false;
            } else {
                // 일반 회원: 구매신청 메뉴 표시
                System.out.println("────────────────────────────────────────────────────────────────────────────────");
                System.out.println("  1. 구매신청");
                System.out.println("  0. 돌아가기");
                System.out.println("────────────────────────────────────────────────────────────────────────────────");
                System.out.print("  선택 > ");
                String choice = sc.nextLine().trim();

                if ("1".equals(choice)) {
                    // 구매신청 처리 (추후 구현)
                    System.out.println("\n구매신청 기능은 추후 구현 예정입니다.");
                    pauseScreen(sc);
                }
                return false;
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
     * 화면 대기
     * - "계속하려면 Enter를 누르세요..." 메시지 출력
     * - 사용자가 Enter를 입력할 때까지 대기
     *
     * @param sc Scanner 객체
     */
    public static void pauseScreen(Scanner sc) {
        System.out.print("\n계속하려면 Enter를 누르세요...");
        sc.nextLine();
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
