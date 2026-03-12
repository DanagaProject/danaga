package dao;

import dto.User;
import exception.DatabaseException;
import java.util.List;

/**
 * 회원 DAO 인터페이스
 */
public interface UserDAO {

    /**
     * 회원가입 - 새로운 회원 정보 저장
     */
    void insert(User user) throws DatabaseException;

    /**
     * 로그인 - 아이디와 비밀번호로 회원 조회
     */
    User selectByIdAndPassword(String userId, String password) throws DatabaseException;

    /**
     * 아이디 중복 확인
     */
    boolean existsById(String userId) throws DatabaseException;

    /**
     * 아이디로 회원 조회
     */
    User selectById(String userId) throws DatabaseException;

    /**
     * 차단된 회원 목록 조회 (status_id = 13, code 테이블의 BANNED)
     */
    List<User> selectBlockedUsers() throws DatabaseException;

    /**
     * 회원 차단하기 (status_id = 13, code 테이블의 BANNED로 변경)
     */
    void blockUser(String userId) throws DatabaseException;

    /**
     * 회원 차단 풀기 (status_id = 12, code 테이블의 ACTIVE로 변경)
     */
    void unblockUser(String userId) throws DatabaseException;

    /**
     * 잔액 충전
     */
    void chargeBalance(String userId, int amount) throws DatabaseException;

    /**
     * 잔액 차감
     */
    void deductBalance(String userId, int amount) throws DatabaseException;
}
