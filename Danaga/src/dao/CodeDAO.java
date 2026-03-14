package dao;

import dto.Code;
import exception.DatabaseException;
import java.util.List;

/**
 * 공통 코드 DAO 인터페이스
 * - code 테이블 조회 담당
 */
public interface CodeDAO {

    /**
     * 전체 코드 목록 조회
     */
    List<Code> selectAll() throws DatabaseException;

    /**
     * 그룹명으로 코드 목록 조회
     * - 예) "item_condition", "order_status", "product_status", "user_status"
     *
     * @param groupName 조회할 그룹명
     */
    List<Code> selectByGroupName(String groupName) throws DatabaseException;

    /**
     * 코드 ID로 단건 조회
     *
     * @param codeId 조회할 코드 ID
     */
    Code selectByCodeId(int codeId) throws DatabaseException;
}
