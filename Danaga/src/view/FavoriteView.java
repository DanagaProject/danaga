package view;

import dto.Category;
import java.util.List;

/**
 * 즐겨찾기 카테고리 관련 View
 * - 즐겨찾기 카테고리 목록 출력
 * - 즐겨찾기 추가/삭제 화면 출력
 */
public class FavoriteView {

    /**
     * 즐겨찾기 카테고리 관리 메인 화면
     *
     * @param favoriteCategories 즐겨찾기 카테고리 목록
     */
    public static void printFavoriteManagement(List<Category> favoriteCategories) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ⭐ 즐겨찾기 카테고리 관리");
        System.out.println("════════════════════════════════════════");
        System.out.println();

        // 현재 즐겨찾기 카테고리 목록
        System.out.println("[현재 즐겨찾기 카테고리]");
        if (favoriteCategories == null || favoriteCategories.isEmpty()) {
            System.out.println("등록된 즐겨찾기 카테고리가 없습니다.");
        } else {
            for (int i = 0; i < favoriteCategories.size(); i++) {
                Category category = favoriteCategories.get(i);
                System.out.println("[" + (i + 1) + "]  " + category.getName());
            }
        }

        System.out.println();
//        System.out.println("⚠  즐겨찾기 카테고리에 새 상품이");
//        System.out.println("   등록되면 알림을 받습니다");
        System.out.println();
        System.out.println("  1. 즐겨찾기 추가");
        System.out.println("  2. 즐겨찾기 삭제");
        System.out.println("  0. 돌아가기");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

    /**
     * 즐겨찾기 추가 헤더
     */
    public static void printAddFavoriteHeader() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           즐겨찾기 추가");
        System.out.println("════════════════════════════════════════");
        System.out.println("추가할 카테고리를 선택하세요");
        System.out.println();
    }

    /**
     * 즐겨찾기 추가 성공 메시지
     *
     * @param category 추가된 카테고리
     */
    public static void printAddFavoriteSuccess(Category category) {
        System.out.println("\n✅ 즐겨찾기 추가 완료");
        System.out.println("   카테고리: " + category.getName());
        System.out.println("   해당 카테고리 신상품 등록 시");
        System.out.println("   자동 알림 발송");
    }

    /**
     * 즐겨찾기 삭제 헤더
     */
    public static void printRemoveFavoriteHeader() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           즐겨찾기 삭제");
        System.out.println("════════════════════════════════════════");
        System.out.println("삭제할 즐겨찾기 번호 입력");
        System.out.println();
    }

    /**
     * 즐겨찾기 삭제 확인 화면
     *
     * @param category 삭제할 카테고리
     */
    public static void printRemoveFavoriteConfirm(Category category) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ⚠  삭제 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  카테고리  :  " + category.getName());
        System.out.println("════════════════════════════════════════");
        System.out.println();
        System.out.println("즐겨찾기에서 삭제하시겠습니까?");
        System.out.println();
        System.out.println("  1. 삭제확인    0. 취소");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

    /**
     * 즐겨찾기 삭제 성공 메시지
     */
    public static void printRemoveFavoriteSuccess(Category category) {
        System.out.println("\n✅ 즐겨찾기 삭제 완료");
        System.out.println("   카테고리: " + category.getName());
    }

    /**
     * 이미 즐겨찾기에 등록된 카테고리 오류 메시지
     */
    public static void printAlreadyInFavorite() {
        System.out.println("\n이미 즐겨찾기에 등록된 카테고리입니다.");
    }

    /**
     * 즐겨찾기가 비어있을 때 삭제 시도 메시지
     */
    public static void printNoFavoritesToRemove() {
        System.out.println("\n삭제할 즐겨찾기가 없습니다.");
    }
}
