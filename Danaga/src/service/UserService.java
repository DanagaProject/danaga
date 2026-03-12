package service;

import dto.User;
import exception.DatabaseException;
import exception.DuplicateUserException;
import exception.UserNotFoundException;

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
}
