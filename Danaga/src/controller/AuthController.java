package controller;

import dto.User;
import exception.DatabaseException;
import exception.DuplicateUserException;
import exception.UserNotFoundException;
import service.UserService;
import service.UserServiceImpl;
import view.FailView;
import view.SuccessView;

/**
 * 인증(로그인/회원가입) 관련 Controller
 */
public class AuthController {
    private UserService userService;

    public AuthController() {
        this.userService = UserServiceImpl.getInstance();
    }

    /**
     * 회원가입 처리
     * @param userId 아이디
     * @param password 비밀번호
     */
    public void signup(String userId, String password) {
        try {
            User user = new User(userId, password);
            userService.signup(user);
            SuccessView.printMessage("회원가입이 완료되었습니다.");
        } catch (DuplicateUserException e) {
            FailView.printMessage(e.getMessage());
        } catch (IllegalArgumentException e) {
            FailView.printMessage(e.getMessage());
        } catch (DatabaseException e) {
            FailView.printMessage("회원가입 실패: " + e.getMessage());
        }
    }

    /**
     * 로그인 처리
     * @param userId 아이디
     * @param password 비밀번호
     * @return 로그인 성공 여부
     */
    public boolean login(String userId, String password) {
        try {
            User loginUser = new User(userId, password);
            User user = userService.login(loginUser);
            SuccessView.printLoginSuccess(user);
            return true;
        } catch (UserNotFoundException e) {
            FailView.printMessage(e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            FailView.printMessage(e.getMessage());
            return false;
        } catch (DatabaseException e) {
            FailView.printMessage("로그인 실패: " + e.getMessage());
            return false;
        }
    }

    /**
     * 로그아웃 처리
     */
    public void logout() {
        userService.logout();
        SuccessView.printMessage("로그아웃되었습니다.");
    }
}
