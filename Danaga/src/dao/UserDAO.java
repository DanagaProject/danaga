package dao;

import dto.User;
import exception.DatabaseException;

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
}
