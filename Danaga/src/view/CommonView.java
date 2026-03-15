package view;

import java.util.Scanner;

/**
 * 공통 View 유틸리티
 * - 여러 View에서 공통으로 사용하는 메서드 모음
 */
public class CommonView {

    // ============================================================================
    // 화면 제어 관련
    // ============================================================================

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
     * 돌아가기 대기
     * - "0. 돌아가기" 메뉴 출력
     * - 0을 입력할 때까지 대기 (다른 입력 시 오류 메시지)
     *
     * @param sc Scanner 객체
     */
    public static void waitForBack(Scanner sc) {
        while (true) {
            System.out.println("\n  0. 돌아가기");
            System.out.println("────────────────────────────────────────────────────────────────────────────────");
            System.out.print("  선택 > ");
            String choice = sc.nextLine().trim();

            if ("0".equals(choice)) {
                return;
            } else {
                System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // ============================================================================
    // 문자열 처리 관련
    // ============================================================================

    /**
     * 문자열 자르기 (한글 2칸, 영문 1칸 계산)
     * - 한글은 2칸, 영문/숫자는 1칸으로 계산하여 화면 폭 기준으로 자름
     * - maxWidth를 초과하면 "..."으로 나머지 문자열 표현
     *
     * @param str 자를 문자열
     * @param maxWidth 최대 화면 폭 (칸 수)
     * @return 잘린 문자열
     */
    public static String truncate(String str, int maxWidth) {
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
    public static int getCharWidth(char c) {
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

    /**
     * 문자열 자르기 (간단 버전)
     * - 문자 수 기준으로 자르기 (한글/영문 구분 없음)
     *
     * @param str 자를 문자열
     * @param maxLength 최대 문자 수
     * @return 잘린 문자열
     */
    public static String truncateSimple(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    // ============================================================================
    // 상태 텍스트 변환 관련
    // ============================================================================

    /**
     * 주문 상태 텍스트 변환
     * - DB의 code 테이블 값을 한글로 변환
     *
     * @param status 주문 상태 (PENDING, SHIPPING, COMPLETED 등)
     * @return 한글 상태 텍스트
     */
    public static String getOrderStatusText(String status) {
        if (status == null) {
            return "-";
        }
        switch (status) {
            case "PENDING":
                return "대기중";
            case "SHIPPING":
                return "배송중";
            case "CANCEL_REQUEST":
                return "취소요청";
            case "CANCEL_COMPLETED":
                return "취소완료";
            case "CANCEL_REJECTED":
                return "취소거부";
            case "COMPLETED":
                return "완료";
            default:
                return status;
        }
    }

    /**
     * 상품 상태 텍스트 변환
     * - DB의 code 테이블 값을 한글로 변환
     *
     * @param status 상품 상태 (ON_SALE, RESERVED, COMPLETED)
     * @return 한글 상태 텍스트
     */
    public static String getProductStatusText(String status) {
        if (status == null) {
            return "-";
        }
        switch (status) {
            case "ON_SALE":
                return "판매중";
            case "RESERVED":
                return "예약됨";
            case "COMPLETED":
                return "완료";
            default:
                return status;
        }
    }

    // ============================================================================
    // 입력 검증 관련
    // ============================================================================

    /**
     * 숫자 입력 검증
     *
     * @param input 입력 문자열
     * @return 숫자면 true, 아니면 false
     */
    public static boolean isValidNumber(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 잘못된 입력 에러 메시지 출력
     */
    public static void printInvalidInputMessage() {
        System.out.println("잘못된 입력입니다.");
    }

    /**
     * 잘못된 숫자 입력 에러 메시지 출력
     */
    public static void printInvalidNumberMessage() {
        System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
    }

    // ============================================================================
    // 범용 메시지 출력 관련
    // ============================================================================

    /**
     * 성공 메시지 출력 (단일 메시지)
     *
     * @param message 출력할 메시지
     */
    public static void printSuccessMessage(String message) {
        System.out.println("\n✅ " + message);
    }

    /**
     * 성공 메시지 출력 (여러 줄)
     * 첫 줄은 ✅와 함께 출력, 나머지는 들여쓰기
     *
     * @param messages 출력할 메시지들 (첫 번째는 제목, 나머지는 상세)
     */
    public static void printSuccessMessage(String... messages) {
        if (messages == null || messages.length == 0) {
            return;
        }
        System.out.println("\n✅ " + messages[0]);
        for (int i = 1; i < messages.length; i++) {
            System.out.println("   " + messages[i]);
        }
    }

    /**
     * 취소 메시지 출력
     *
     * @param message 출력할 메시지
     */
    public static void printCancelMessage(String message) {
        System.out.println("\n" + message);
    }

    /**
     * 에러 메시지 출력
     *
     * @param message 출력할 메시지
     */
    public static void printErrorMessage(String message) {
        System.out.println("\n" + message);
    }

    /**
     * 일반 메시지 출력
     *
     * @param message 출력할 메시지
     */
    public static void printMessage(String message) {
        System.out.println(message);
    }
}
