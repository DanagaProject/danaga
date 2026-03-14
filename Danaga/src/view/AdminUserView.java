package view;

import dto.User;
import java.util.List;

/**
 * 관리자 회원 관리 View (SCR-021)
 * - 회원 목록 및 상세/차단 화면 출력
 * - 아이디(userId)를 선택자로 사용
 */
public class AdminUserView {

    /**
     * 회원 목록 출력 (SCR-021)
     * - userId를 선택자로 사용 (아이디 직접 입력)
     * - 정상 회원과 차단 회원을 시각적으로 구분
     *
     * @param users 회원 목록
     */
    public static void printUserList(List<User> users) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  👥  회원 목록 / 차단 관리");
        System.out.println("════════════════════════════════════════");

        if (users == null || users.isEmpty()) {
            System.out.println("  조회된 회원이 없습니다.");
        } else {
            for (User user : users) {
                boolean isBanned = "BANNED".equals(user.getStatus());
                String statusMark = isBanned ? "  차단됨 🚫" : "  정상";
                System.out.println("  [" + user.getUserId() + "]" + statusMark);
            }
        }

        System.out.println("════════════════════════════════════════");
        System.out.println("  0. 돌아가기");
        System.out.println("════════════════════════════════════════");
        System.out.print("  아이디 입력 > ");
    }

    /**
     * 회원 상세 화면 출력 (SCR-021)
     * - 정상 회원: "1. 차단" 옵션 표시
     * - 차단 회원: "1. 차단해제" 옵션 표시
     *
     * @param user 조회한 회원
     */
    public static void printUserDetail(User user) {
        boolean isBanned = "BANNED".equals(user.getStatus());

        System.out.println("\n════════════════════════════════════════");
        System.out.println("  👤  회원 상세");
        System.out.println("════════════════════════════════════════");
        System.out.println("  아이디  :  " + user.getUserId());
        System.out.println("  상태    :  " + (isBanned ? "차단됨 🚫" : "정상"));
        System.out.println("════════════════════════════════════════");

        if (isBanned) {
            System.out.println("  1. 차단해제    0. 돌아가기");
        } else {
            System.out.println("  1. 차단        0. 돌아가기");
        }

        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }
}
