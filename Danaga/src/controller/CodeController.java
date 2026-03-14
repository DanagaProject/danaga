package controller;

import dto.Code;
import exception.DatabaseException;
import service.CodeService;
import service.CodeServiceImpl;
import view.FailView;
import java.util.Collections;
import java.util.List;

/**
 * 공통 코드 Controller
 * - 코드 조회 기능 제공
 * - 다른 Controller에서 상태값(코드명) 조회 시 사용
 *
 * [사용 예시]
 *   CodeController codeController = new CodeController();
 *
 *   // 상품 상태 목록 조회
 *   List<Code> itemConditions = codeController.getCodesByGroup(CodeService.GROUP_ITEM_CONDITION);
 *
 *   // 코드 ID로 코드명 조회 (상태값 -> 한글명 변환)
 *   String statusName = codeController.getCodeNameById(10); // "ON_SALE"
 */
public class CodeController {

    private final CodeService codeService;

    public CodeController() {
        this.codeService = CodeServiceImpl.getInstance();
    }

    /**
     * 전체 코드 목록 조회
     *
     * @return 전체 Code 목록 (오류 시 빈 리스트)
     */
    public List<Code> getAllCodes() {
        try {
            return codeService.getAllCodes();
        } catch (DatabaseException e) {
            FailView.printMessage("코드 조회 실패: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 그룹명으로 코드 목록 조회
     * - CodeService 상수 사용 권장
     *   ex) CodeService.GROUP_ITEM_CONDITION
     *       CodeService.GROUP_ORDER_STATUS
     *       CodeService.GROUP_PRODUCT_STATUS
     *       CodeService.GROUP_USER_STATUS
     *
     * @param groupName 조회할 그룹명
     * @return 해당 그룹의 Code 목록 (오류 시 빈 리스트)
     */
    public List<Code> getCodesByGroup(String groupName) {
        try {
            return codeService.getCodesByGroup(groupName);
        } catch (IllegalArgumentException e) {
            FailView.printMessage(e.getMessage());
            return Collections.emptyList();
        } catch (DatabaseException e) {
            FailView.printMessage("코드 조회 실패: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 코드 ID로 코드명 조회
     * - 상태값(int)을 화면 표시용 문자열로 변환할 때 사용
     *   ex) 10 -> "ON_SALE", 12 -> "ACTIVE"
     *
     * @param codeId 조회할 코드 ID
     * @return 코드명 (조회 실패 시 null)
     */
    public String getCodeNameById(int codeId) {
        try {
            return codeService.getCodeNameById(codeId);
        } catch (DatabaseException e) {
            FailView.printMessage("코드 조회 실패: " + e.getMessage());
            return null;
        }
    }
}
