package service;

import dao.CodeDAO;
import dao.CodeDAOImpl;
import dto.Code;
import exception.DatabaseException;
import java.util.List;

/**
 * 공통 코드 Service 구현체
 */
public class CodeServiceImpl implements CodeService {

    private final CodeDAO codeDAO;

    private static CodeService instance = new CodeServiceImpl();

    private CodeServiceImpl() {
        this.codeDAO = CodeDAOImpl.getInstance();
    }

    public static CodeService getInstance() {
        return instance;
    }

    /**
     * 전체 코드 목록 조회
     */
    @Override
    public List<Code> getAllCodes() throws DatabaseException {
        return codeDAO.selectAll();
    }

    /**
     * 그룹명으로 코드 목록 조회
     *
     * @param groupName 조회할 그룹명
     */
    @Override
    public List<Code> getCodesByGroup(String groupName) throws DatabaseException {
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("그룹명을 입력해주세요.");
        }
        return codeDAO.selectByGroupName(groupName);
    }

    /**
     * 코드 ID로 코드명 조회
     *
     * @param codeId 조회할 코드 ID
     * @return 코드명 (없으면 null)
     */
    @Override
    public String getCodeNameById(int codeId) throws DatabaseException {
        Code code = codeDAO.selectByCodeId(codeId);
        return code != null ? code.getName() : null;
    }
}
