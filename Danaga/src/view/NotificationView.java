package view;

import dto.Notification;
import java.util.List;

/**
 * 알림 View
 * - 출력만 담당 (로직 없음)
 * - 흐름 제어 및 Controller 호출은 MyPageView에서 담당
 */
public class NotificationView {

    /**
     * 전체 알림 목록 화면 출력
     * - 읽음/안읽음 상태와 함께 목록 출력
     * - 하단 메뉴 출력 (1. 안읽은 알림 보기 / 0. 돌아가기)
     *
     * @param notifications Controller에서 전달받은 전체 알림 목록
     */
    public static void printNotificationList(List<Notification> notifications) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  🔔  알림 확인");
        System.out.println("════════════════════════════════════════");

        if (notifications == null || notifications.isEmpty()) {
            System.out.println("  알림이 없습니다.");
        } else {
            for (Notification n : notifications) {
                String readMark = "1".equals(n.getIsRead()) ? "읽음  " : "안읽음";
                String createdAt = n.getCreatedAt() != null ? n.getCreatedAt() : "";
                System.out.printf("  [%d] %s  %s  %s%n",
                        n.getNotificationId(), readMark, createdAt, n.getMessage());
            }
        }

        System.out.println("════════════════════════════════════════");
        System.out.println("  1. 안읽은 알림 보기");
        System.out.println("  0. 돌아가기");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

    /**
     * 안읽은 알림 목록 화면 출력
     * - 안읽은 알림 목록만 출력 (Controller에서 이미 필터링된 목록을 받음)
     * - 하단 입력 안내 출력
     *
     * @param unreadList Controller에서 전달받은 안읽은 알림 목록
     */
    public static void printUnreadNotificationList(List<Notification> unreadList) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  🔔  안읽은 알림");
        System.out.println("════════════════════════════════════════");

        if (unreadList == null || unreadList.isEmpty()) {
            System.out.println("  읽지 않은 알림이 없습니다.");
            System.out.println("════════════════════════════════════════");
            System.out.println("  0. 돌아가기");
            System.out.println("════════════════════════════════════════");
        } else {
            for (Notification n : unreadList) {
                String createdAt = n.getCreatedAt() != null ? n.getCreatedAt() : "";
                System.out.printf("  [%d]  %s  %s%n",
                        n.getNotificationId(), createdAt, n.getMessage());
            }
            System.out.println("════════════════════════════════════════");
            System.out.println("  알림 번호 선택 > 읽음 처리");
            System.out.println("  0. 돌아가기");
            System.out.println("════════════════════════════════════════");
        }

        System.out.print("  선택 > ");
    }
}
