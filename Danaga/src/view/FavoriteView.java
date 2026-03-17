package view;

import java.util.List;

import dto.Category;
import dto.FavoriteCategory;

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
    public static void printFavoriteManagement(List<FavoriteCategory> favoriteCategories) {
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
                FavoriteCategory category = favoriteCategories.get(i);
                System.out.println("[" + (i + 1) + "]  " + category.getCategoryName());
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
    public static void printAddFavoriteSuccess(FavoriteCategory category) {
        System.out.println("\n✅ 즐겨찾기 추가 완료");
        System.out.println("   카테고리: " + category.getCategoryName());
        System.out.println("   해당 카테고리 신상품 등록 시");
        System.out.println("   자동 알림 발송");
    }

    /**
     * 즐겨찾기 삭제 목록 화면 출력
     * - categoryId를 번호로 표시 (Controller 연동 시 categoryId로 직접 전달)
     *
     * @param favorites Controller에서 전달받은 즐겨찾기 목록
     */
    public static void printRemoveFavoriteList(List<FavoriteCategory> favorites) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           즐겨찾기 삭제");
        System.out.println("════════════════════════════════════════");

        if (favorites == null || favorites.isEmpty()) {
            System.out.println("  삭제할 즐겨찾기가 없습니다.");
        } else {
            System.out.println("  삭제할 카테고리 번호를 입력하세요");
            System.out.println();
            for (FavoriteCategory c : favorites) {
                System.out.println("  [" + c.getCategoryId() + "]  " + c.getCategoryName());
            }
        }

        System.out.println("════════════════════════════════════════");
        System.out.println("  0. 돌아가기");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

    /**
     * 즐겨찾기 삭제 확인 화면
     *
     * @param category 삭제할 카테고리
     */
    public static void printRemoveFavoriteConfirm(FavoriteCategory category) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  ⚠  삭제 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  카테고리  :  " + category.getCategoryName());
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
    public static void printRemoveFavoriteSuccess(FavoriteCategory category) {
        System.out.println("\n✅ 즐겨찾기 삭제 완료");
        System.out.println("   카테고리: " + category.getCategoryName());
    }

    /**
     * 즐겨찾기가 비어있을 때 삭제 시도 메시지
     */
    public static void printNoFavoritesToRemove() {
        System.out.println("\n삭제할 즐겨찾기가 없습니다.");
    }
}
