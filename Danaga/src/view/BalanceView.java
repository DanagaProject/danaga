package view;

/**
 * 잔액/충전 관련 View
 * - 잔액 정보 출력 기능
 * - 충전 화면 출력 기능
 */
public class BalanceView {

    /**
     * 잔액 충전 헤더 출력
     *
     * @param currentBalance 현재 잔액
     */
    public static void printChargeBalanceHeader(int currentBalance) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("           💰 잔액 충전");
        System.out.println("════════════════════════════════════════");
        System.out.println("  현재 잔액: " + String.format("%,d원", currentBalance));
        System.out.println("════════════════════════════════════════");
        System.out.println();
        System.out.print("  충전할 금액 입력 (0: 돌아가기) > ");
    }

    /**
     * 충전 확인 화면 출력
     *
     * @param chargeAmount 충전할 금액
     * @param currentBalance 현재 잔액
     */
    public static void printChargeConfirmScreen(int chargeAmount, int currentBalance) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("  💳 충전 확인");
        System.out.println("════════════════════════════════════════");
        System.out.println("  충전 금액  : " + String.format("%,d원", chargeAmount));
        System.out.println("  현재 잔액  : " + String.format("%,d원", currentBalance));
        System.out.println("  충전 후    : " + String.format("%,d원", currentBalance + chargeAmount));
        System.out.println("════════════════════════════════════════");
        System.out.println();
        System.out.println("  1. 충전확인    0. 취소");
        System.out.println("════════════════════════════════════════");
        System.out.print("  선택 > ");
    }

    /**
     * 충전 완료 메시지 출력
     *
     * @param chargeAmount 충전한 금액
     * @param newBalance 충전 후 잔액
     */
    public static void printChargeSuccess(int chargeAmount, int newBalance) {
        System.out.println("\n✅ 충전 완료");
        System.out.println("   충전 금액: +" + String.format("%,d원", chargeAmount));
        System.out.println("   현재 잔액: " + String.format("%,d원", newBalance));
    }

    /**
     * 잔액 정보 표시 (헤더용)
     * - 마이페이지 등에서 잔액 정보를 간단히 표시할 때 사용
     *
     * @param balance 잔액
     * @return 포맷된 잔액 문자열
     */
    public static String formatBalance(int balance) {
        return String.format("%,d원", balance);
    }

}
