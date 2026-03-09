# 다나가(Danaga) 중고 컴퓨터 거래 플랫폼 개발 계획

## Context (프로젝트 배경)

**왜 이 프로젝트를 개발하는가?**
- Java JDBC 기반의 콘솔 MVC 패턴 학습 및 실습
- 실제 중고 거래 플랫폼의 핵심 비즈니스 로직 구현 (P2P 거래, 잔액 관리, 상태 전이)
- 복잡한 트랜잭션 처리 및 권한 관리 시스템 구축 경험

**문제 정의:**
- 화면정의서에 정의된 23개 화면을 콘솔에서 구현
- 6개 테이블의 관계형 데이터베이스 설계를 JDBC로 연동
- Guest/USER/ADMIN 3단계 권한 체계 구현
- 상품 구매/판매/취소에 대한 안전한 트랜잭션 처리

**목표:**
단계별로 구현 가능한 명확한 로드맵을 제공하여, 이 계획을 보는 누구나(다른 개발자, AI 에이전트, 미래의 나) 프로젝트를 이어서 개발하거나 새로 시작할 수 있도록 함.

---

## 프로젝트 개요

- **프로젝트명**: Danaga (다나가)
- **타입**: Java 콘솔 애플리케이션
- **아키텍처**: MVC 패턴
- **데이터베이스**: MySQL + JDBC
- **Java 버전**: Java 21
- **참고 프로젝트**: `C:\edu\Java\work\step11_mvc_SaveElec\src\mvc`

---

## 전체 패키지 구조

```
src/
├── view/                    # 사용자 인터페이스 (콘솔 메뉴)
│   ├── StartView.java      # 프로그램 시작점 (main)
│   ├── MenuView.java       # 권한별 메뉴 라우팅
│   ├── GuestMenuView.java  # 비로그인 메뉴
│   ├── UserMenuView.java   # 일반 사용자 메뉴
│   ├── AdminMenuView.java  # 관리자 메뉴
│   ├── SuccessView.java    # 성공 메시지 출력
│   └── FailView.java       # 실패 메시지 출력
│
├── controller/              # 요청 처리 및 흐름 제어
│   ├── UserController.java
│   ├── ProductController.java
│   ├── OrderController.java
│   ├── CategoryController.java
│   ├── NotificationController.java
│   └── FavoriteController.java
│
├── service/                 # 비즈니스 로직 (Singleton)
│   ├── UserService.java + UserServiceImpl.java
│   ├── ProductService.java + ProductServiceImpl.java
│   ├── OrderService.java + OrderServiceImpl.java (핵심!)
│   ├── CategoryService.java + CategoryServiceImpl.java
│   ├── NotificationService.java + NotificationServiceImpl.java
│   └── FavoriteService.java + FavoriteServiceImpl.java
│
├── dao/                     # 데이터 접근 계층 (JDBC)
│   ├── UserDAO.java
│   ├── ProductDAO.java
│   ├── OrderDAO.java
│   ├── CategoryDAO.java
│   ├── NotificationDAO.java
│   └── FavoriteCategoryDAO.java
│
├── dto/                     # 데이터 전송 객체
│   ├── User.java
│   ├── Product.java
│   ├── Order.java
│   ├── Category.java
│   ├── Notification.java
│   └── FavoriteCategory.java
│
├── exception/               # 사용자 정의 예외
│   ├── DuplicateUserException.java
│   ├── UserNotFoundException.java
│   ├── ProductNotFoundException.java
│   ├── InsufficientBalanceException.java
│   ├── InvalidOrderStatusException.java
│   ├── UnauthorizedAccessException.java
│   └── DatabaseException.java
│
├── util/                    # 유틸리티 클래스
│   ├── DBUtil.java         # DB 연결 관리 (핵심!)
│   ├── SessionManager.java # 로그인 세션 관리
│   ├── InputValidator.java # 입력 검증
│   └── ColorUtil.java      # 콘솔 색상 출력
│
└── config/
    └── db.properties       # DB 연결 정보
```

---

## 데이터베이스 설계 (6개 테이블)

### 테이블 관계도
```
users (중심)
├─ 1:N → products (판매자)
│   └─ 1:N → orders
│       └─ N:1 ← users (구매자)
├─ 1:N → notifications
└─ M:N → categories (through favorite_categories)
```

### 주요 테이블

#### 1. users (회원)
- **PK**: user_id (VARCHAR(20))
- **컬럼**: password, balance(INT), status(ENUM: ACTIVE/BANNED), role(ENUM: USER/ADMIN)

#### 2. products (상품)
- **PK**: product_id (INT, AUTO_INCREMENT)
- **FK**: seller_id(users), category_id(categories)
- **컬럼**: title, price, description, item_condition(상/중/하), status(ON_SALE/RESERVED/COMPLETED)

#### 3. orders (거래)
- **PK**: order_id (INT, AUTO_INCREMENT)
- **FK**: product_id(products), buyer_id(users)
- **컬럼**: status(PENDING/SHIPPING/CANCEL_REQUEST/CANCEL_COMPLETED/COMPLETED)

#### 4. categories (카테고리)
- **PK**: category_id (INT, AUTO_INCREMENT)
- **컬럼**: name (UNIQUE)

#### 5. favorite_categories (즐겨찾기)
- **복합 PK**: user_id + category_id

#### 6. notifications (알림)
- **PK**: notification_id (INT, AUTO_INCREMENT)
- **FK**: user_id(users)
- **컬럼**: message, is_read(BOOLEAN), created_at

**DDL 스크립트 위치**: 프로젝트 루트에 `schema.sql` 생성

---

## 핵심 구현 전략

### 1. MVC 패턴 적용

**참고 프로젝트 패턴 재사용:**
- `C:\edu\Java\work\step11_mvc_SaveElec\src\mvc\view\MenuView.java` 참고
  - Scanner.nextLine() 기반 콘솔 입력
  - switch-case 메뉴 분기
  - while(true) 무한 루프

- `C:\edu\Java\work\step11_mvc_SaveElec\src\mvc\service\ElectronicsServiceImpl.java` 참고
  - Singleton 패턴 (getInstance())
  - 인터페이스 + 구현체 분리

- `C:\edu\Java\work\step11_mvc_SaveElec\src\mvc\dto\Electronics.java` 참고
  - Serializable 구현
  - 생성자 오버로딩 (용도별)

### 2. JDBC 트랜잭션 처리 (가장 중요!)

**핵심 파일**: `src/util/DBUtil.java`

```java
// Connection Pool 관리
public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, username, password);
}

// 리소스 정리
public static void close(Connection con, PreparedStatement pstmt, ResultSet rs)

// 트랜잭션 제어
public static void commit(Connection con)
public static void rollback(Connection con)
```

**트랜잭션 패턴** (Service Layer):
```java
Connection con = null;
try {
    con = DBUtil.getConnection();
    con.setAutoCommit(false);  // 시작

    // 여러 DAO 메서드 호출 (같은 Connection 재사용)
    userDAO.updateBalance(con, buyerId, -price);
    userDAO.updateBalance(con, sellerId, +price);
    productDAO.updateStatus(con, productId, RESERVED);
    orderDAO.insertOrder(con, order);

    DBUtil.commit(con);  // 성공
} catch (Exception e) {
    DBUtil.rollback(con);  // 실패
    throw new DatabaseException("...", e);
} finally {
    con.setAutoCommit(true);  // 복구
    DBUtil.close(con, null);
}
```

### 3. 세션 관리

**핵심 파일**: `src/util/SessionManager.java`

```java
private static User currentUser = null;

public static void login(User user)
public static void logout()
public static boolean isLoggedIn()
public static User getCurrentUser()
public static boolean isAdmin()
```

**권한 체크 패턴**:
```java
// MenuView.java에서
if (!SessionManager.isLoggedIn()) {
    guestMenu.printGuestMenu();
} else if (SessionManager.isAdmin()) {
    adminMenu.printAdminMenu();
} else {
    userMenu.printUserMenu();
}
```

---

## 단계별 구현 순서 (10단계)

### Phase 1: 기반 인프라 (1-2일) ⭐ 최우선
**목표**: DB 연결 및 기본 구조 완성

1. DB 테이블 생성 (schema.sql 실행)
2. `src/config/db.properties` 작성
   ```properties
   driver=com.mysql.cj.jdbc.Driver
   url=jdbc:mysql://localhost:3306/danaga
   username=root
   password=your_password
   ```
3. **핵심 클래스 구현**:
   - `util/DBUtil.java` (Connection 관리)
   - `util/SessionManager.java` (세션 관리)
   - Enum 5개 (UserStatus, UserRole, ItemCondition, ProductStatus, OrderStatus)
   - Exception 7개 (모든 사용자 정의 예외)
4. DBUtil.getConnection() 테스트

### Phase 2: 사용자 기능 (2-3일)
**목표**: 로그인/회원가입 완성

1. `dto/User.java` 작성
2. `dao/UserDAO.java` 구현
   - insertUser() (회원가입)
   - login() (로그인 + 차단 체크)
   - getBalance() (잔액 조회)
   - updateBalance(Connection con, ...) ⭐ 트랜잭션용
3. `service/UserService` + `UserServiceImpl` (Singleton)
4. `controller/UserController.java`
5. **View 구현**:
   - `view/StartView.java` (main 메서드)
   - `view/MenuView.java` (권한별 라우팅)
   - `view/GuestMenuView.java` (로그인/가입 입력)
   - `view/SuccessView.java`, `view/FailView.java`
6. **테스트**: 회원가입 → 로그인 → 로그아웃

### Phase 3: 카테고리 & 즐겨찾기 (1일)
**목표**: 카테고리 조회 및 즐겨찾기 기능

1. `dto/Category.java`, `dto/FavoriteCategory.java`
2. `dao/CategoryDAO.java`, `dao/FavoriteCategoryDAO.java`
3. Service, Controller 구현
4. **테스트**: 즐겨찾기 추가/삭제

### Phase 4: 상품 기능 (2-3일)
**목표**: 상품 CRUD 완성

1. `dto/Product.java` (생성자 오버로딩)
2. `dao/ProductDAO.java`
   - insertProduct() (등록)
   - selectAllOnSale() (전체 조회 + JOIN)
   - selectByCategory() (카테고리별)
   - selectByKeyword() (검색)
   - selectById() (상세)
   - updateStatus(Connection con, ...) ⭐ 트랜잭션용
   - deleteProduct() (권한 체크)
3. Service, Controller 구현
4. `view/UserMenuView.java`에 상품 등록 메뉴 추가
5. **테스트**: 등록 → 조회 → 검색 → 삭제

### Phase 5: 주문/거래 (3-4일) ⭐⭐⭐ 가장 복잡하고 중요!
**목표**: 구매/배송/확정/취소 트랜잭션

1. `dto/Order.java` (JOIN용 필드 포함)
2. `dao/OrderDAO.java`
   - insertOrder(Connection con, ...) ⭐ 트랜잭션용
   - selectByBuyer() (구매 내역 + JOIN)
   - selectBySeller() (판매 내역 + JOIN)
   - updateStatus() (상태 변경)
   - selectCancelRequests() (ADMIN용)

3. **핵심 비즈니스 로직** `service/OrderServiceImpl.java`:

   **purchaseProduct() - 구매 트랜잭션**:
   ```
   1. 상품 조회 및 검증 (판매중? 본인 상품 아님?)
   2. 구매자 잔액 체크 (부족하면 예외)
   3. [트랜잭션 시작]
      - 구매자 잔액 차감
      - 판매자 잔액 증가
      - 상품 상태 변경 (ON_SALE → RESERVED)
      - 주문 생성 (PENDING)
      - 판매자에게 알림 생성
   4. [커밋]
   ```

   **approveCancel() - 취소 승인 트랜잭션**:
   ```
   1. 주문 조회 및 검증 (CANCEL_REQUEST 상태?)
   2. [트랜잭션 시작]
      - 구매자 환불
      - 판매자 차감
      - 상품 상태 복구 (RESERVED → ON_SALE)
      - 주문 상태 변경 (CANCEL_COMPLETED)
      - 구매자에게 알림
   3. [커밋]
   ```

4. `controller/OrderController.java` (예외 처리)
5. `view/UserMenuView.java`에 거래 관리 메뉴 추가
6. **테스트** (중요!):
   - 구매 성공 케이스
   - 잔액 부족 예외
   - 트랜잭션 롤백 (DB 일관성 확인)
   - 취소 승인 전체 흐름

### Phase 6: 알림 (1일)
**목표**: 알림 생성 및 조회

1. `dto/Notification.java`
2. `dao/NotificationDAO.java`
   - insertNotification(Connection con, ...) ⭐ 트랜잭션 내 호출
   - selectByUser() (사용자별 조회)
   - updateReadStatus() (읽음 처리)
   - getUnreadCount() (미읽음 개수)
3. Service, Controller 구현
4. **테스트**: 구매 시 알림 생성 확인

### Phase 7: 관리자 기능 (1-2일)
**목표**: 취소 관리, 회원 차단

1. `view/AdminMenuView.java` 구현
2. `controller/OrderController.java`에 approveCancel() 추가
3. `controller/UserController.java`에 banUser() 추가
4. 권한 체크 로직 강화
5. **테스트**: 관리자 로그인 → 취소 승인 → 회원 차단

### Phase 8: 마이페이지 (1-2일)
**목표**: 내 정보 및 상품 관리

1. `view/UserMenuView.java`에 마이페이지 메뉴
2. 내가 등록한 상품 조회
3. 잔액 조회
4. **테스트**: 전체 시나리오

### Phase 9: UI/UX 개선 (1일)
**목표**: 사용자 편의성 향상

1. `util/ColorUtil.java` (ANSI 색상 코드)
2. `util/InputValidator.java` (입력 검증)
3. 색상 적용 (성공: 초록, 실패: 빨강)
4. 확인 메시지 추가

### Phase 10: 최종 테스트 (1일)
**목표**: 통합 테스트 및 문서화

1. **통합 시나리오 테스트**:
   - 비로그인 → 회원가입 → 로그인 → 상품 조회 → 구매 → 확정
   - 판매자: 로그인 → 상품 등록 → 구매 알림 → 배송 처리
   - 취소: 구매 → 취소 요청 → 관리자 승인 → 환불
2. 경계 조건 테스트
3. README.md 작성

**총 예상 기간**: 15-21일 (2-3명 권장)

---

## 중요 파일 경로 (우선순위 순)

### 핵심 인프라
1. **`C:\edu\project\semiProject\Danaga\src\util\DBUtil.java`**
   - 모든 DB 연결의 기반
   - Connection Pool 관리, 트랜잭션 제어
   - 가장 먼저 구현해야 함

2. **`C:\edu\project\semiProject\Danaga\src\util\SessionManager.java`**
   - 로그인 세션 및 권한 관리
   - 모든 Controller에서 참조

### 핵심 비즈니스 로직
3. **`C:\edu\project\semiProject\Danaga\src\service\OrderServiceImpl.java`**
   - 가장 복잡한 트랜잭션 로직
   - purchaseProduct(), approveCancel() 메서드가 핵심

4. **`C:\edu\project\semiProject\Danaga\src\dao\UserDAO.java`**
   - updateBalance(Connection con, ...) 메서드가 모든 거래에서 사용됨
   - 트랜잭션 참여 패턴의 모범 사례

### UI 라우팅
5. **`C:\edu\project\semiProject\Danaga\src\view\MenuView.java`**
   - 권한별 화면 분기의 중심
   - SessionManager와 연동

---

## 재사용 가능한 패턴 (참고 프로젝트에서)

### 1. Singleton 패턴
**위치**: `C:\edu\Java\work\step11_mvc_SaveElec\src\mvc\service\ElectronicsServiceImpl.java`
```java
private static XxxService instance = new XxxServiceImpl();
public static XxxService getInstance() {
    return instance;
}
```

### 2. 콘솔 메뉴 패턴
**위치**: `C:\edu\Java\work\step11_mvc_SaveElec\src\mvc\view\MenuView.java`
```java
while(true) {
    System.out.print("메뉴 선택> ");
    String menu = sc.nextLine();
    switch(menu) {
        case "1": controller.method1(); break;
        case "9": System.exit(0);
    }
}
```

### 3. DTO 생성자 오버로딩
**위치**: `C:\edu\Java\work\step11_mvc_SaveElec\src\mvc\dto\Electronics.java`
```java
public Product() {}  // 기본
public Product(int id, String detail) {}  // 수정용
public Product(String seller, int categoryId, String title, ...) {}  // 등록용
```

---

## 핵심 비즈니스 로직 상세

### 상품 구매 트랜잭션 (가장 중요!)

**메서드**: `OrderServiceImpl.purchaseProduct(int productId, String buyerId)`

**흐름**:
1. 상품 조회 (`ProductDAO.selectById()`)
2. 검증:
   - 판매자 ≠ 구매자
   - 상품 상태 = ON_SALE
3. 구매자 잔액 조회 (`UserDAO.getBalance()`)
4. 검증: 잔액 ≥ 가격
5. **[트랜잭션 시작]** `con.setAutoCommit(false)`
   - 구매자 잔액 차감: `userDAO.updateBalance(con, buyerId, -price)`
   - 판매자 잔액 증가: `userDAO.updateBalance(con, sellerId, +price)`
   - 상품 상태 변경: `productDAO.updateStatus(con, productId, RESERVED)`
   - 주문 생성: `orderDAO.insertOrder(con, order)`
   - 알림 생성: `notificationDAO.insertNotification(con, notification)`
6. **[커밋]** `DBUtil.commit(con)`
7. 실패 시 **[롤백]** `DBUtil.rollback(con)`

**예외**:
- `InsufficientBalanceException`: 잔액 부족
- `ProductNotFoundException`: 상품 없음 또는 판매 완료
- `DatabaseException`: DB 오류 (롤백 수행됨)

---

## 상태 전이 관리

### 상품 상태 (ProductStatus)
```
ON_SALE (판매중)
    ↓ [구매]
RESERVED (예약중)
    ↓ [구매 확정]
COMPLETED (거래 완료)

RESERVED → ON_SALE (취소 승인 시 복구)
```

### 주문 상태 (OrderStatus)
```
PENDING (결제 완료)
    ↓ [배송 시작]
SHIPPING (배송중)
    ↓ [구매 확정]
COMPLETED (거래 완료)

PENDING/SHIPPING → CANCEL_REQUEST (취소 요청)
CANCEL_REQUEST → CANCEL_COMPLETED (취소 승인)
```

---

## 검증 및 테스트 방법

### 단위 테스트
각 Phase 완료 시 다음을 테스트:
1. **DAO 테스트**: main 메서드에서 직접 호출
   ```java
   public static void main(String[] args) {
       UserDAO dao = new UserDAO();
       User user = new User("test", "1234", 10000, UserStatus.ACTIVE, UserRole.USER);
       dao.insertUser(user);
       System.out.println("Success!");
   }
   ```

2. **Service 테스트**: Controller 없이 Service 직접 호출

3. **트랜잭션 테스트**:
   - 일부러 예외 발생시켜 롤백 확인
   - DB에서 `SELECT * FROM users WHERE user_id = 'xxx'`로 잔액 확인

### 통합 테스트 시나리오
1. **정상 구매 흐름**:
   ```
   회원가입(buyer, 50000원) → 로그인 → 상품 조회 → 구매(30000원 상품)
   → 잔액 확인(20000원) → 판매자 로그인 → 판매 내역 확인 → 배송 시작
   → 구매자 로그인 → 구매 확정
   ```

2. **취소 흐름**:
   ```
   구매 → 취소 요청 → 관리자 로그인 → 취소 승인
   → 구매자 잔액 환불 확인 → 상품 상태 ON_SALE 확인
   ```

3. **예외 케이스**:
   - 잔액 부족으로 구매 실패
   - 차단된 회원 로그인 차단
   - 본인 상품 구매 차단

---

## 주의사항

### 트랜잭션 관리
- **반드시** try-catch-finally에서 롤백 처리
- Connection은 Service에서 생성, DAO는 재사용
- finally 블록에서 `con.setAutoCommit(true)` 복구 필수

### 리소스 정리
- 모든 JDBC 리소스는 `DBUtil.close()` 호출
- Scanner는 프로그램 종료 시 1번만 close

### 권한 체크
- Controller 메서드마다 `SessionManager.isLoggedIn()` 확인
- ADMIN 전용 기능은 `SessionManager.isAdmin()` 체크

### SQL Injection 방지
- **절대** String 연결로 SQL 작성 금지
- **반드시** PreparedStatement 사용

---

## 다음 세션에서 이어서 개발하기

### 현재 상태 확인 방법
1. Git 상태 확인: `git status`
2. 컴파일 확인: Eclipse에서 빌드 오류 체크
3. 현재 구현 Phase 확인: 이 문서의 체크리스트 참조

### 새로운 기능 추가 시
1. 해당 Phase의 순서를 따라 구현 (DTO → DAO → Service → Controller → View)
2. 트랜잭션이 필요한 경우 OrderServiceImpl 패턴 참조
3. 테스트 후 다음 Phase로 진행

### 문제 해결
- **Connection 문제**: DBUtil.java 확인, db.properties 설정 확인
- **트랜잭션 롤백 안됨**: con.setAutoCommit(false) 확인
- **NULL 예외**: SessionManager.isLoggedIn() 먼저 체크
- **중복 키 오류**: DAO에서 중복 체크 로직 추가

---

## 완료 기준

✅ **프로젝트 완료 체크리스트**
- [ ] 모든 23개 화면 구현 완료
- [ ] 3단계 권한 체계 동작 (Guest/USER/ADMIN)
- [ ] 트랜잭션 롤백 테스트 통과
- [ ] 모든 예외 케이스 처리
- [ ] DB Connection 누수 없음 (close 확인)
- [ ] README.md 작성 (실행 방법, 테스트 계정)
- [ ] schema.sql 제공 (DDL + 초기 데이터)

---

## 참고 문서

- **화면정의서**: `C:\edu\project\semiProject\화면정의서_다나가_v1_7.pptx`
- **DB 설계서**: `C:\edu\project\semiProject\다나가_Danaga_DB_상세_설계_v4.pptx`
- **참고 프로젝트**: `C:\edu\Java\work\step11_mvc_SaveElec\src\mvc`
- **현재 프로젝트**: `C:\edu\project\semiProject\Danaga`

이 계획서를 따라 단계별로 구현하면, 체계적이고 안정적인 중고 거래 플랫폼을 완성할 수 있습니다.
