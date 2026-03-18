package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import dto.User;
import exception.DatabaseException;
import exception.DuplicateUserException;
import exception.UserNotFoundException;
import util.SessionManager;
import java.util.List;

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
     * @param user User 객체 (userId, password 필요)
     * @throws DuplicateUserException 중복된 아이디인 경우
     * @throws DatabaseException DB 오류 발생 시
     */
    @Override
    public void signup(User user)
            throws DuplicateUserException, DatabaseException {

        // 1. 입력 값 검증
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        // 2. 아이디 중복 확인
        if (userDAO.existsById(user)) {
            throw new DuplicateUserException("이미 존재하는 아이디입니다: " + user.getUserId());
        }

        // 3. 회원 정보 저장
        userDAO.insert(user);

        System.out.println("[UserService] 회원가입 성공: " + user.getUserId());
    }

    /**
     * 로그인 처리
     * @param user User 객체 (userId, password 필요)
     * @return 로그인한 사용자 정보
     * @throws UserNotFoundException 사용자를 찾을 수 없거나 비밀번호가 일치하지 않는 경우
     * @throws DatabaseException DB 오류 발생 시
     */
    @Override
    public User login(User user)
            throws UserNotFoundException, DatabaseException {

        // 1. 입력 값 검증
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        // 2. 회원 조회
        User foundUser = userDAO.selectByUser(user);

        if (foundUser == null) {
            throw new UserNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. 계정 상태 확인
        if ("BANNED".equals(foundUser.getStatus())) {
            throw new UserNotFoundException("차단된 계정입니다.");
        }
        if ("INACTIVE".equals(foundUser.getStatus())) {
            throw new UserNotFoundException("비활성화된 계정입니다.");
        }

        // 4. 세션에 로그인 정보 저장
        SessionManager.login(foundUser);

        System.out.println("[UserService] 로그인 성공: " + foundUser.getUserId());
        return foundUser;
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
    @Override
    public void chargeBalance(User user, int amount) throws DatabaseException {
        // 1. 유효성 검사 (필요 시)
        if (amount <= 0) {
            throw new DatabaseException("충전 금액은 0원보다 커야 합니다.");
        }

        // 2. DAO 호출 (실제 DB 업데이트 수행)
        userDAO.chargeBalance(user, amount);

        // 3. 세션 정보 동기화
        user.setBalance(user.getBalance() + amount);
    }

    /**
     * 전체 회원 목록 조회 (관리자용, 관리자 계정 제외)
     */
    @Override
    public List<User> getAllUsers() throws DatabaseException {
        return userDAO.selectAllUsers();
    }

    /**
     * 아이디로 회원 단건 조회 (관리자용)
     */
    @Override
    public User getUserById(String userId) throws DatabaseException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }
        User user = new User(userId, null);
        return userDAO.selectById(user);
    }

    /**
     * 회원 차단 (관리자용)
     */
    @Override
    public void blockUser(String userId) throws DatabaseException {
        User user = new User(userId, null);
        userDAO.blockUser(user);
    }

    /**
     * 회원 차단 해제 (관리자용)
     */
    @Override
    public void unblockUser(String userId) throws DatabaseException {
        User user = new User(userId, null);
        userDAO.unblockUser(user);
    }
}
