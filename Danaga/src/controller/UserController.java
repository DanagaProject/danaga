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
    
    /**
     * 현재 로그인된 세션의 유저 정보를 DB 최신 상태로 동기화합니다.
     */
    public void refreshCurrentSession() {
        // 1. SessionManager에서 현재 세션의 유저 객체를 통째로 가져옵니다.
        dto.User currentUser = util.SessionManager.getCurrentUser();
        
        if (currentUser != null) {
            try {
                // 2. 💡 가지고 계신 메서드 활용! 
                // 파라미터로 User 객체를 받으므로, currentUser를 그대로 넘겨줍니다.
                dto.User freshUser = userService.selectByUser(currentUser);
                
                if (freshUser != null) {
                    // 3. DB에서 꺼내온 따끈따끈한 객체로 세션을 덮어씌웁니다.
                    util.SessionManager.updateSession(freshUser);
                }
            } catch (exception.DatabaseException e) {
                // 갱신 실패 시 기존 세션 유지 (조용히 넘어감)
            }
        }
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
