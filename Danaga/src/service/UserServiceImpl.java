package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import dto.User;
import exception.DatabaseException;
import exception.DuplicateUserException;
import exception.UserNotFoundException;
import util.SessionManager;

/**
 * 회원 Service 구현체
 */
public class UserServiceImpl implements UserService {
    private UserDAO userDAO;

    private static UserService instance = new UserServiceImpl();

    private UserServiceImpl() {
        this.userDAO = UserDAOImpl.getInstance();
    }

    public static UserService getInstance() {
        return instance;
    }

    /**
     * 회원가입 처리
     * @param userId 사용자 ID
     * @param password 비밀번호
     * @throws DuplicateUserException 중복된 아이디인 경우
     * @throws DatabaseException DB 오류 발생 시
     */
    @Override
    public void signup(String userId, String password)
            throws DuplicateUserException, DatabaseException {

        // 1. 입력 값 검증
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        // 2. 아이디 중복 확인
        if (userDAO.existsById(userId)) {
            throw new DuplicateUserException("이미 존재하는 아이디입니다: " + userId);
        }

        // 3. 회원 정보 생성 및 저장
        User newUser = new User(userId, password);
        userDAO.insert(newUser);

        System.out.println("[UserService] 회원가입 성공: " + userId);
    }

    /**
     * 로그인 처리
     * @param userId 사용자 ID
     * @param password 비밀번호
     * @return 로그인한 사용자 정보
     * @throws UserNotFoundException 사용자를 찾을 수 없거나 비밀번호가 일치하지 않는 경우
     * @throws DatabaseException DB 오류 발생 시
     */
    @Override
    public User login(String userId, String password)
            throws UserNotFoundException, DatabaseException {

        // 1. 입력 값 검증
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        // 2. 회원 조회
        User user = userDAO.selectByIdAndPassword(userId, password);

        if (user == null) {
            throw new UserNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. 계정 상태 확인
        if ("BANNED".equals(user.getStatus())) {
            throw new UserNotFoundException("차단된 계정입니다.");
        }
        if ("INACTIVE".equals(user.getStatus())) {
            throw new UserNotFoundException("비활성화된 계정입니다.");
        }

        // 4. 세션에 로그인 정보 저장
        SessionManager.login(user);

        System.out.println("[UserService] 로그인 성공: " + userId);
        return user;
    }

    /**
     * 로그아웃 처리
     * 세션에서 로그인 정보 제거
     */
    @Override
    public void logout() {
        SessionManager.logout();
        System.out.println("[UserService] 로그아웃 완료");
    }
}
