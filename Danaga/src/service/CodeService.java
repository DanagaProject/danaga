package service;

import dto.Code;
import exception.DatabaseException;
import java.util.List;

/**
 * 공통 코드 Service 인터페이스
 * - code 테이블의 데이터를 조회하여 상태값 등에 활용
 */
public interface CodeService {

    /**
     * 전체 코드 목록 조회
     */
    List<Code> getAllCodes() throws DatabaseException;

    /**
     * 그룹명으로 코드 목록 조회
     *
     * @param groupName 조회할 그룹명 (CodeService 상수 참고)
     */
    List<Code> getCodesByGroup(String groupName) throws DatabaseException;

    /**
     * 코드 ID로 코드명 조회
     * - 상태값(int)을 화면에 표시할 문자열로 변환할 때 사용
     *
     * @param codeId 조회할 코드 ID
     * @return 코드명 (없으면 null)
     */
    String getCodeNameById(int codeId) throws DatabaseException;

}
