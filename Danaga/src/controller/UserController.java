package controller;
import dto.User;
import exception.DatabaseException;
import service.UserService;
import service.UserServiceImpl;
import util.SessionManager;


public class UserController {
	private UserService userService;

    public UserController() {
        this.userService = UserServiceImpl.getInstance();
    }
	
	public void chargeBalance(int amount) {
	    try {
	        // 세션에서 현재 유저 가져오기
	        User currentUser = SessionManager.getCurrentUser();
	        
	        if (currentUser != null) {
	            // 서비스 호출
	            userService.chargeBalance(currentUser, amount);
	            // 성공 시 뷰 알림 등 후속 처리
	        }
	    } catch (DatabaseException e) {
	        // 뷰에 에러 메시지 전달
	        System.out.println("\n❌ 오류: " + e.getMessage());
	    }
	}
}
