package service;

import dto.User;
import exception.DatabaseException;
import exception.DuplicateUserException;
import exception.UserNotFoundException;
import java.util.List;

/**
 * 회원 Service 인터페이스
 */
public interface UserService {
	
    /**
     * 회원가입
     */
    void signup(User user) throws DuplicateUserException, DatabaseException;

    /**
     * 로그인
     */
    User login(User user) throws UserNotFoundException, DatabaseException;

    /**
     * 로그아웃
     */
    void logout();

    /**
     * 잔액 충전 로직
     */
    void chargeBalance(User user, int amount) throws DatabaseException;

    
    /**
     * 회원 정보 단건 조회 (세션 동기화용)
     */
    User selectByUser(User user) throws DatabaseException;


    /**
     * 전체 회원 목록 조회 (관리자용)
     */
    List<User> getAllUsers() throws DatabaseException;

    /**
     * 아이디로 회원 단건 조회 (관리자용)
     */
    User getUserById(String userId) throws DatabaseException;

    /**
     * 회원 차단 (관리자용)
     */
    void blockUser(String userId) throws DatabaseException;

    /**
     * 회원 차단 해제 (관리자용)
     */
    void unblockUser(String userId) throws DatabaseException;

}
