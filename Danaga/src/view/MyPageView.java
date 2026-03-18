package view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.CodeController;
import controller.OrdersController;
import controller.ProductController;
import dto.Category;
import dto.Code;
import dto.FavoriteCategory;
import dto.Notification;
import dto.Orders;
import dto.Product;
import util.SessionManager;

/**
 * 마이페이지 View (SCR-010)
 */
public class MyPageView {
    private Scanner sc;
    private final CodeController codeController;
    private final OrdersController ordersController;
    
    
    public MyPageView(Scanner sc) {
        this.sc = sc;
        this.codeController = new CodeController();
        this.ordersController = new OrdersController();
        
    }

    /**
     * 마이페이지 메뉴
     */
    public void printMyPage() {
        while (true) {
            System.out.println("\n════════════════════════════════════════");
            System.out.println("  👤  마이페이지");
            System.out.println("════════════════════════════════════════");
            System.out.println("  사용자: " + SessionManager.getCurrentUserId() + " 님");
            System.out.println("  💰  잔액:  " + BalanceView.formatBalance(SessionManager.getCurrentUser().getBalance())
                    + "  |  🔔  알림");
            System.out.println("════════════════════════════════════════");
            System.out.println("  [구매]");
            System.out.println("  1.  내 구매 현황");
            System.out.println("  2.  잔액 충전");
            System.out.println();
            System.out.println("  [판매]");
            System.out.println("  3.  내 판매 현황");
            System.out.println("  4.  내 판매 상품 관리");
            System.out.println("  5.  상품 등록");
            System.out.println();
            System.out.println("  [계정]");
            System.out.println("  6.  즐겨찾기 카테고리 관리");
            System.out.println("  7.  알림 확인");
            System.out.println();
            System.out.println("  0.  돌아가기");
            System.out.println("════════════════════════════════════════");
            System.out.print("  선택 > ");

            String menu = sc.nextLine().trim();

            switch (menu) {
                case "1":
                    viewPurchaseHistory();
                    break;
                case "2":
                    chargeBalance();
                    break;
                case "3":
                    viewSaleHistory();
                    break;
                case "4":
                    manageMyProducts();
                    break;
                case "5":
                    registerProduct();
                    break;
                case "6":
                    manageFavorites();
                    break;
                case "7":
                    viewNotifications();
                    break;
                case "0":
                    return; // 이전 메뉴로 돌아감
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    /**
     * 내 구매 현황
     */
    /**
     * 내 구매 현황
     */
    private void viewPurchaseHistory() {
        while (true) {
            try {
            	String userId = SessionManager.getCurrentUserId();
                // 컨트롤러 호출 시 예외가 발생할 수 있으므로 try 블록 안에서 실행
                List<Orders> myOrders = ordersController.getOrdersByUserId(userId);

                // PurchaseView를 사용하여 구매 목록 출력
                PurchaseView.printPurchaseList(myOrders);

                if (myOrders == null || myOrders.isEmpty()) {
                    CommonView.waitForBack(sc);
                    return;
                }

                System.out.println("\n  1. 주문 상세보기");
                System.out.println("  2. 구매확정");
                System.out.println("  3. 주문 취소 요청");
                System.out.println("  0. 돌아가기");
                System.out.println("════════════════════════════════════════════════════════════════════════════════");
                System.out.print("  선택 > ");

                String menu = sc.nextLine().trim();

                switch (menu) {
                    case "1":
                        viewOrderDetail(myOrders);
                        break;
                    case "2":
                        confirmPurchase(myOrders);
                        break;
                    case "3":
                        requestOrderCancel(myOrders);
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("잘못된 입력입니다.");
                }

            } catch (SQLException e) {
                // DB 연동 중 에러 발생 시 안내 메시지 출력 후 마이페이지 메인으로 복귀
                System.out.println("\n❌ 구매 내역을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
                CommonView.waitForBack(sc);
                return; 
            }
        }
    }


    /**
     * 주문 상세보기
     */
    private void viewOrderDetail(List<Orders> orders) {
        System.out.print("\n주문번호 입력 > ");
        String input = sc.nextLine().trim();

        try {
            int ordersId = Integer.parseInt(input);
            Orders order = PurchaseView.findOrderById(orders, ordersId);

            if (order != null) {
                // PurchaseView의 상세 정보 출력 메서드 사용
                PurchaseView.printPurchaseDetail(order);
                CommonView.waitForBack(sc);
            } else {
                System.out.println("해당 주문을 찾을 수 없습니다.");
            }
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        }
    }

    /**
     * 거래 확정 (SCR-012)
     */
    private void confirmPurchase(List<Orders> orders) {
        // DAO에서 확정 가능한 주문만 조회해올 예정
    	List<Orders> confirmableOrders = new java.util.ArrayList<>();
        for (Orders o : orders) {
            if (o.getStatusId() == 4) {
                confirmableOrders.add(o);
            }
        }

        PurchaseView.printConfirmPurchaseHeader();
        PurchaseView.printSimpleOrderList(confirmableOrders);

        if (confirmableOrders.isEmpty()) {
            System.out.println("확정 가능한 주문이 없습니다.");
            CommonView.pauseScreen(sc);
            return;
        }

        System.out.println();
        System.out.print("번호 > ");
        String input = sc.nextLine().trim();

        try {
            int ordersId = Integer.parseInt(input);
            Orders order = PurchaseView.findOrderById(confirmableOrders, ordersId);

            if (order != null) {
                PurchaseView.printConfirmPurchaseScreen(order);
                System.out.print("  선택 > ");

                String confirm = sc.nextLine().trim();
                if ("1".equals(confirm)) {
                	ordersController.confirmTrade(order.getOrdersId());
                    CommonView.printSuccessMessage("거래 확정 완료");
                    CommonView.pauseScreen(sc);
                    return;
                } else if ("0".equals(confirm)) {
                    System.out.println("\n거래 확정을 취소했습니다.");
                } else {
                    CommonView.printInvalidInputMessage();
                }
            } else {
                System.out.println("해당 주문을 찾을 수 없습니다.");
            }
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        } catch (SQLException e) {
        	System.out.println("\n❌ 거래 확정 처리 중 오류가 발생했습니다: " + e.getMessage());
            CommonView.pauseScreen(sc);
        }
    }

    /**
     * 취소 요청 (SCR-013)
     */
    private void requestOrderCancel(List<Orders> orders) {
        PurchaseView.printCancelRequestHeader();
        
        // 취소/요청이 가능한 상태는 PENDING(4) 또는 SHIPPING(5)입니다.
        List<Orders> cancelableOrders = new java.util.ArrayList<>();
        for (Orders o : orders) {
            if (o.getStatusId() == 4 || o.getStatusId() == 5) {
                cancelableOrders.add(o);
            }
        }

        if (cancelableOrders.isEmpty()) {
            System.out.println("취소 가능한 주문이 없습니다.");
            CommonView.pauseScreen(sc);
            return;
        }

        PurchaseView.printSimpleOrderList(cancelableOrders);

        System.out.print("\n번호 > ");
        String input = sc.nextLine().trim();

        try {
            int ordersId = Integer.parseInt(input);
            Orders order = PurchaseView.findOrderById(cancelableOrders, ordersId);

            if (order != null) {
                // 1. 배송 전 (PENDING : 4) -> 즉시 취소 및 환불
                if (order.getStatusId() == 4) {
                    System.out.println("\n[배송 전 취소] 즉시 취소가 가능한 주문입니다.");
                    System.out.print("정말 취소하시겠습니까? (1.확인  0.취소) > ");
                    if ("1".equals(sc.nextLine().trim())) {
                        // 관리자 개입 없이 즉시 환불 처리 (cancelComplete 재활용)
                        ordersController.cancelComplete(order.getOrdersId());
                        CommonView.printSuccessMessage("즉시 취소 완료", "결제 금액이 전액 환불되었습니다.");
                    }
                } 
                // 2. 배송 중 (SHIPPING : 5) -> 취소 요청 및 중재 대기
                else if (order.getStatusId() == 5) {
                    System.out.println("\n[배송 후 취소] 이미 상품이 발송되었습니다.");
                    System.out.println("취소 요청 시 판매자 동의가 필요합니다.");
                    System.out.print("취소를 요청하시겠습니까? (1.확인  0.취소) > ");
                    if ("1".equals(sc.nextLine().trim())) {
                        // 상태를 6(CANCEL_REQUEST)으로 변경
                        ordersController.cancleRequest(order.getOrdersId());
                        CommonView.printSuccessMessage("취소 요청 완료", "판매자 확인 후 처리가 진행됩니다.");
                    }
                }
                CommonView.pauseScreen(sc);
            } else {
                System.out.println("목록에 있는 번호를 선택해주세요.");
            }
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }

    /**
     * 주문번호로 주문 찾기
     */

    /**
     * 샘플 주문 데이터 생성 (View 테스트용)
     * 추후 Controller/Service를 통해 실제 데이터로 대체
     */
    private List<Orders> getSampleOrders() {
        List<Orders> orders = new ArrayList<>();
        String currentUserId = SessionManager.getCurrentUserId();

        // 샘플 주문 1 - 대기중
        Orders o1 = new Orders(1, 1, currentUserId, 4, "2024-03-15 10:30:00");
        o1.setProductTitle("LG 그램 17인치 노트북");
        o1.setProductPrice(1200000);
        o1.setSellerId("user01");
        o1.setStatus("PENDING");
        orders.add(o1);

        // 샘플 주문 2 - 배송중
        Orders o2 = new Orders(2, 4, currentUserId, 5, "2024-03-10 14:20:00");
        o2.setProductTitle("레오폴드 FC660C 무접점");
        o2.setProductPrice(250000);
        o2.setSellerId("user02");
        o2.setStatus("SHIPPING");
        orders.add(o2);

        // 샘플 주문 3 - 완료
        Orders o3 = new Orders(3, 5, currentUserId, 9, "2024-03-05 09:15:00");
        o3.setProductTitle("로지텍 MX Master 3");
        o3.setProductPrice(80000);
        o3.setSellerId("user03");
        o3.setStatus("COMPLETED");
        orders.add(o3);

        // 샘플 주문 4 - 취소요청
        Orders o4 = new Orders(4, 8, currentUserId, 6, "2024-03-12 16:45:00");
        o4.setProductTitle("삼성 DDR5 32GB 램");
        o4.setProductPrice(180000);
        o4.setSellerId("user04");
        o4.setStatus("CANCEL_REQUEST");
        orders.add(o4);

        return orders;
    }

    /**
     * 내 판매 현황 (SCR-014)
     */
    private void viewSaleHistory() {
        while (true) {
            try {
                String userId = SessionManager.getCurrentUserId();
                List<Orders> allSales = ordersController.getSalesBySellerId(userId);

                List<Orders> ongoingOrders = new java.util.ArrayList<>();
                List<Orders> completedOrders = new java.util.ArrayList<>();

                for (Orders order : allSales) {
                    int status = order.getStatusId();
                    // 4:대기중, 5:배송중, 6:취소요청
                    if (status == 4 || status == 5 || status == 6) {
                        ongoingOrders.add(order);
                    } else {
                        completedOrders.add(order);
                    }
                }

                // 1. 데이터를 먼저 출력합니다 (SaleView 안의 메뉴 가이드는 지우는 것을 권장)
                SaleView.printSaleHistory(ongoingOrders, completedOrders);

                // 2. MyPageView에서 관리하는 통합 메뉴
                System.out.println("\n  [ 메뉴 선택 ]");
                System.out.println("  1. 배송 시작 처리");
                System.out.println("  2. 취소 요청 처리 (동의/거절)");
                System.out.println("  3. 전체 판매 내역 보기");
                System.out.println("  0. 돌아가기");
                System.out.print("\n  선택 > ");
                
                String menu = sc.nextLine().trim();

                switch (menu) {
                    case "1":
                        processShipping(ongoingOrders);
                        break;
                    case "2":
                        processCancelRequest(ongoingOrders);
                        break;
                    case "3":
                        viewAllSaleHistory(allSales);
                        break;
                    case "0":
                        return; // 마이페이지 메인으로 돌아감
                    default:
                        System.out.println("잘못된 입력입니다.");
                }
            } catch (java.sql.SQLException e) {
                System.out.println("\n❌ 판매 내역 조회 중 오류 발생: " + e.getMessage());
                CommonView.waitForBack(sc);
                return;
            }
        }
    }

    /**
     * 배송 처리
     */
 
    private void processShipping(List<Orders> orders) {
        // 1. 상태가 4(PENDING, 대기중)인 주문만 필터링
        List<Orders> pendingOrders = new java.util.ArrayList<>();
        for (Orders o : orders) {
            if (o.getStatusId() == 4) {
                pendingOrders.add(o);
            }
        }

        SaleView.printShippingProcessHeader();

        if (pendingOrders.isEmpty()) {
            System.out.println("배송 처리 가능한 주문이 없습니다.");
            CommonView.pauseScreen(sc);
            return;
        }

        // 전체 리스트가 아닌 필터링된 리스트를 출력
        for (Orders order : pendingOrders) {
            System.out.println("[" + order.getOrdersId() + "]  " + order.getProductTitle());
        }

        System.out.println();
        System.out.print("번호 > ");
        String input = sc.nextLine().trim();

        try {
            int ordersId = Integer.parseInt(input);
            Orders order = PurchaseView.findOrderById(pendingOrders, ordersId);

            if (order != null) {
                SaleView.printShippingProcessConfirm(order);
                String confirm = sc.nextLine().trim();

                if ("1".equals(confirm)) {
                    // ⭐ 실제 컨트롤러 호출: 상태를 5(SHIPPING)로 변경
                    ordersController.startDelivery(order.getOrdersId());
                    CommonView.printSuccessMessage("배송 처리 완료");
                    CommonView.pauseScreen(sc);
                    return;
                } else if ("0".equals(confirm)) {
                    System.out.println("\n배송 처리를 취소했습니다.");
                } else {
                    CommonView.printInvalidInputMessage(); //잘못된 입력 에러 메시지 출력
                }
            } else {
                System.out.println("해당 주문을 찾을 수 없습니다.");
            }
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        } catch (java.sql.SQLException e) {
            // ⭐ DB 에러 방어 로직
            System.out.println("\n❌ 배송 처리 중 오류 발생: " + e.getMessage());
            CommonView.pauseScreen(sc);
        }
    }
    /**
     * 판매자용 취소 요청 관리
     */
    private void processCancelRequest(List<Orders> orders) {
        // 1. 상태가 6(CANCEL_REQUEST)인 주문만 필터링
        List<Orders> cancelRequested = new java.util.ArrayList<>();
        for (Orders o : orders) {
            if (o.getStatusId() == 6) {
                cancelRequested.add(o);
            }
        }

        SaleView.printCancelRequestProcessHeader();

        if (cancelRequested.isEmpty()) {
            System.out.println("처리할 취소 요청이 없습니다.");
            CommonView.pauseScreen(sc);
            return;
        }

        for (Orders order : cancelRequested) {
            System.out.println("[" + order.getOrdersId() + "]  " + order.getProductTitle());
        }

        System.out.print("\n번호 > ");
        String input = sc.nextLine().trim();

        try {
            int ordersId = Integer.parseInt(input);
            Orders order = PurchaseView.findOrderById(cancelRequested, ordersId);

            if (order != null) {
                // 판매자용 확인 화면 출력 (1. 동의-환불, 2. 거절-관리자 개입 요청)
                SaleView.printSellerCancelDecisionScreen(order); 
                String confirm = sc.nextLine().trim();

                if ("1".equals(confirm)) {
                    // 판매자가 직접 동의 -> 바로 환불 프로세스 진행
                    ordersController.cancelComplete(order.getOrdersId());
                    CommonView.printSuccessMessage("취소 승인 완료", "구매자에게 환불되었습니다.");
                } else if ("2".equals(confirm)) {
                    // 판매자가 거절 -> 상태는 6으로 유지하고 관리자에게 알림만 보냄
                    // (필요하다면 별도의 '분쟁 중' 상태를 만들 수도 있지만, 일단은 6 유지)
                	ordersController.rejectCancel(ordersId);
                    System.out.println("\n취소를 거절하셨습니다. 관리자가 해당 건을 검토할 예정입니다.");
                    // ordersController.requestAdminIntervention(order.getOrdersId()); // [선택사항] 관리자 알림 로직
                } else {
                    System.out.println("취소 요청 처리를 중단합니다.");
                }
                CommonView.pauseScreen(sc);
            }
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        }
    }
//    /**
//     * 취소 요청 처리
//     */
//    private void proceprocessCancelRequestssCancelRequest(List<Orders> orders) {
//        // 1. 상태가 6(CANCEL_REQUEST, 취소 요청)인 주문만 필터링
//        List<Orders> cancelRequested = new java.util.ArrayList<>();
//        for (Orders o : orders) {
//            if (o.getStatusId() == 6) {
//                cancelRequested.add(o);
//            }
//        }
//
//        SaleView.printCancelRequestProcessHeader();
//
//        if (cancelRequested.isEmpty()) {
//            System.out.println("취소 요청 처리 가능한 주문이 없습니다.");
//            CommonView.pauseScreen(sc);
//            return;
//        }
//
//        // 필터링된 리스트 출력
//        for (Orders order : cancelRequested) {
//            System.out.println("[" + order.getOrdersId() + "]  " + order.getProductTitle());
//        }
//
//        System.out.println();
//        System.out.print("번호 > ");
//        String input = sc.nextLine().trim();
//
//        try {
//            int ordersId = Integer.parseInt(input);
//            Orders order = PurchaseView.findOrderById(cancelRequested, ordersId);
//
//            if (order != null) {
//                SaleView.printCancelRequestProcessConfirm(order);
//                String confirm = sc.nextLine().trim();
//
//                if ("1".equals(confirm)) {
//                    // ⭐ 1. 취소 승인: 상태를 7(CANCEL_COMPLETED)로 변경 및 환불 로직
//                    ordersController.cancelComplete(order.getOrdersId());
//                    CommonView.printSuccessMessage("취소 승인 완료", "구매자에게 환불 처리됩니다.");
//                    CommonView.pauseScreen(sc);
//                } else if ("2".equals(confirm)) {
//                    // ⭐ 2. 취소 거부: 상태를 8(CANCEL_REJECTED)로 변경
//                    ordersController.rejectCancel(order.getOrdersId());
//                    CommonView.printSuccessMessage("취소 거부 완료", "거래가 계속 진행됩니다.");
//                    CommonView.pauseScreen(sc);
//                } else if ("0".equals(confirm)) {
//                    System.out.println("\n취소 요청 처리를 취소했습니다.");
//                } else {
//                    CommonView.printInvalidInputMessage();
//                }
//            } else {
//                System.out.println("해당 주문을 찾을 수 없습니다.");
//            }
//        } catch (NumberFormatException e) {
//            CommonView.printInvalidNumberMessage();
//        } catch (java.sql.SQLException e) {
//            // ⭐ DB 에러 방어 로직
//            System.out.println("\n❌ 처리 중 오류 발생: " + e.getMessage());
//            CommonView.pauseScreen(sc);
//        }
//    }
    /**
     * 전체 판매 이력 조회
     */
    private void viewAllSaleHistory(List<Orders> allSales) {
        // 더 이상 샘플 데이터를 부르지 않고, 전달받은 실제 리스트를 사용합니다.
        if (allSales == null || allSales.isEmpty()) {
            System.out.println("\n판매 내역이 존재하지 않습니다.");
        } else {
            // SaleView에 만들어둔 전체 출력 메서드 호출
            SaleView.printAllSaleHistory(allSales); 
        }

        CommonView.waitForBack(sc);
    }

    /**
     * 전체 판매 주문 샘플 데이터
     */
    private List<Orders> getSampleAllSaleOrders() {
        List<Orders> orders = new ArrayList<>();
        String currentUserId = SessionManager.getCurrentUserId();

        // 진행중인 주문들
        orders.addAll(getSampleOngoingOrders());

        // 완료된 주문들
        orders.addAll(getSampleCompletedOrders());

        // 추가 샘플 - 배송중
        Orders o6 = new Orders(106, 16, "user06", 8, "2024-03-13 13:30:00");
        o6.setProductTitle("삼성 모니터 27인치");
        o6.setProductPrice(350000);
        o6.setSellerId(currentUserId);
        o6.setStatus("SHIPPING");
        orders.add(o6);

        // 추가 샘플 - 취소완료
        Orders o7 = new Orders(107, 17, "user07", 7, "2024-02-15 10:20:00");
        o7.setProductTitle("로지텍 키보드");
        o7.setProductPrice(120000);
        o7.setSellerId(currentUserId);
        o7.setStatus("CANCEL_COMPLETED");
        orders.add(o7);

        return orders;
    }


    /**
     * 내 판매 상품 샘플 데이터 생성 (View 테스트용)
     * 추후 Controller/Service를 통해 실제 데이터로 대체
     */
    private List<Product> getSampleMyProducts() {
        List<Product> products = new ArrayList<>();
        String currentUserId = SessionManager.getCurrentUserId();

        // 샘플 상품 1 - 판매중
        Product p1 = new Product(11, currentUserId, 1, "맥북 프로 16인치 M2", 2800000,
                "2023년 구입한 맥북 프로입니다. 상태 매우 좋습니다.",
                1, 10, "2024-03-01", "N");
        p1.setCategoryName("노트북");
        p1.setItemCondition("상");
        p1.setStatus("ON_SALE");
        products.add(p1);

        // 샘플 상품 2 - 예약됨
        Product p2 = new Product(12, currentUserId, 4, "해피해킹 키보드 HHKB", 300000,
                "해피해킹 키보드 판매합니다. 깨끗하게 사용했습니다.",
                1, 11, "2024-02-28", "N");
        p2.setCategoryName("키보드");
        p2.setItemCondition("상");
        p2.setStatus("RESERVED");
        products.add(p2);

        // 샘플 상품 3 - 판매완료
        Product p3 = new Product(13, currentUserId, 5, "로지텍 G502 마우스", 60000,
                "로지텍 게이밍 마우스. 1년 사용.",
                2, 9, "2024-02-20", "N");
        p3.setCategoryName("마우스");
        p3.setItemCondition("중");
        p3.setStatus("COMPLETED");
        products.add(p3);

        return products;
    }

    /**
     * 거래중인 주문 샘플 데이터 (판매자용 - SCR-014)
     */
    private List<Orders> getSampleOngoingOrders() {
        List<Orders> orders = new ArrayList<>();
        String currentUserId = SessionManager.getCurrentUserId();

        // 주문 1 - PENDING (배송 처리 가능)
        Orders o1 = new Orders(101, 11, "user01", 4, "2024-03-15 10:30:00");
        o1.setProductTitle("RTX 4080");
        o1.setProductPrice(500000);
        o1.setSellerId(currentUserId);
        o1.setStatus("PENDING");
        orders.add(o1);

        // 주문 2 - CANCEL_REQUEST (취소 요청 처리 가능)
        Orders o2 = new Orders(102, 12, "user02", 5, "2024-03-14 14:20:00");
        o2.setProductTitle("i9-13900K");
        o2.setProductPrice(650000);
        o2.setSellerId(currentUserId);
        o2.setStatus("CANCEL_REQUEST");
        orders.add(o2);

        return orders;
    }

    /**
     * 완료된 주문 샘플 데이터 (판매자용 - SCR-014)
     */
    private List<Orders> getSampleCompletedOrders() {
        List<Orders> orders = new ArrayList<>();
        String currentUserId = SessionManager.getCurrentUserId();

        // 주문 3 - COMPLETED
        Orders o3 = new Orders(103, 13, "user03", 9, "2024-01-10 09:15:00");
        o3.setProductTitle("GTX 1080");
        o3.setProductPrice(280000);
        o3.setSellerId(currentUserId);
        o3.setStatus("COMPLETED");
        orders.add(o3);

        // 주문 4 - COMPLETED
        Orders o4 = new Orders(104, 14, "user04", 6, "2024-01-05 16:45:00");
        o4.setProductTitle("32GB DDR5");
        o4.setProductPrice(180000);
        o4.setSellerId(currentUserId);
        o4.setStatus("COMPLETED");
        orders.add(o4);

        // 주문 5 - COMPLETED
        Orders o5 = new Orders(105, 15, "user05", 7, "2024-01-01 11:20:00");
        o5.setProductTitle("HHKB");
        o5.setProductPrice(300000);
        o5.setSellerId(currentUserId);
        o5.setStatus("COMPLETED");
        orders.add(o5);

        return orders;
    }

    /**
     * 상태별 상품 샘플 데이터 (SCR-015)
     */
    private List<Product> getSampleProductsByStatus(String status) {
        List<Product> products = new ArrayList<>();
        String currentUserId = SessionManager.getCurrentUserId();

        if ("ON_SALE".equals(status)) {
            // 판매중인 상품
            Product p1 = new Product(201, currentUserId, 7, "RTX 4080", 500000,
                    "RTX 4080 GPU 판매합니다.",
                    1, 10, "2024-03-10", "N");
            p1.setCategoryName("GPU");
            p1.setItemCondition("상");
            p1.setStatus("ON_SALE");
            products.add(p1);

            Product p2 = new Product(202, currentUserId, 4, "HHKB", 200000,
                    "해피해킹 키보드입니다.",
                    1, 10, "2024-03-08", "N");
            p2.setCategoryName("키보드");
            p2.setItemCondition("상");
            p2.setStatus("ON_SALE");
            products.add(p2);

        } else if ("RESERVED".equals(status)) {
            // 거래중인 상품
            Product p3 = new Product(203, currentUserId, 6, "i9-13900K", 650000,
                    "인텔 i9-13900K CPU",
                    1, 10, "2024-03-05", "N");
            p3.setCategoryName("CPU");
            p3.setItemCondition("상");
            p3.setStatus("RESERVED");
            products.add(p3);

        } else if ("COMPLETED".equals(status)) {
            // 최근 완료
            Product p5 = new Product(205, currentUserId, 7, "GTX 1080", 280000,
                    "GTX 1080 GPU",
                    2, 9, "2024-01-10", "N");
            p5.setCategoryName("GPU");
            p5.setItemCondition("중");
            p5.setStatus("COMPLETED");
            products.add(p5);
        }

        return products;
    }

    /**
     * 내 판매 상품 관리 (SCR-015)
     */
    private void manageMyProducts() {
        while (true) {
            // 샘플 데이터 가져오기 (추후 Controller를 통해 실제 데이터 조회)       
        	List<Product> allProducts = ProductController.productSelectBySellerAll();
        	List<Product> onSaleProducts = new ArrayList<>();
        	List<Product> reservedProducts = new ArrayList<>();
        	List<Product> completedProducts = new ArrayList<>();
 
        	for (Product p : allProducts) {
        	    switch (p.getStatusId()) {
        	        case 10 -> onSaleProducts.add(p);
        	        case 11 -> reservedProducts.add(p);
        	        case 14 -> completedProducts.add(p);
        	    }
        	}
        	
            // SaleView를 사용하여 상품 관리 화면 출력
            SaleView.printProductManagement(onSaleProducts, reservedProducts, completedProducts);

            String menu = sc.nextLine().trim();

            switch (menu) {
                case "1":
                    updateProduct(onSaleProducts);
                    break;
                case "2":
                    deleteProduct(onSaleProducts);
                    break;
                case "3":
                    viewAllProductHistory();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    /**
     * 상품 수정
     */
    private void updateProduct(List<Product> onSaleProducts) {
        // 수정할 상품 선택
        Product originalProduct = selectProductToUpdate(onSaleProducts);
        if (originalProduct == null) {
            return;
        }

        // 상품 수정 처리
        processProductUpdate(originalProduct);
    }

    /**
     * 수정할 상품 선택
     */
    private Product selectProductToUpdate(List<Product> onSaleProducts) {
        SaleView.printUpdateProductHeader();

        /*if (onSaleProducts == null || onSaleProducts.isEmpty()) {
            System.out.println("수정 가능한 상품이 없습니다.");
            return null;
        }*/

        for (Product product : onSaleProducts) {
            System.out.println("[" + product.getProductId() + "]  " +
                product.getTitle() + "  " + String.format("%,d", product.getPrice()));
        }

        System.out.println();
        System.out.print("번호 (0: 취소) > ");
        String input = sc.nextLine().trim();

        if ("0".equals(input)) {
            return null;
        }

        try {
            int productId = Integer.parseInt(input);
            Product product = ProductView.findProductById(onSaleProducts, productId);

            if (product == null) {
                System.out.println("해당 상품을 찾을 수 없습니다.");
            }
            return product;
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
            return null;
        }
    }

    /**
     * 상품 수정 처리
     */
    private void processProductUpdate(Product originalProduct) {
        Product updatedProduct = copyProduct(originalProduct);

        while (true) {
            String choice = getUpdateFieldChoice(updatedProduct);

            if ("0".equals(choice)) {
                if (confirmProductUpdate(originalProduct, updatedProduct)) {
                    ProductController.productUpdate(updatedProduct);
                    CommonView.printSuccessMessage("상품 수정 완료");
                } else {
                    System.out.println("\n수정을 취소했습니다.");
                }
                return;
            }

            updateProductField(updatedProduct, choice);
        }
    }

    /**
     * 수정할 항목 선택
     */
    private String getUpdateFieldChoice(Product product) {
        SaleView.printUpdateProductDetail(product);
        String updateMenu = sc.nextLine().trim();
        return updateMenu;
    }

    /**
     * 선택한 항목 수정
     */
    private void updateProductField(Product product, String fieldChoice) {
        switch (fieldChoice) {
            case "1":
                updateProductTitle(product);
                break;
            case "2":
                updateProductPrice(product);
                break;
            case "3":
                updateProductCondition(product);
                break;
            case "4":
                updateProductCategory(product);
                break;
            case "5":
                updateProductDescription(product);
                break;
            default:
                CommonView.printInvalidInputMessage();
        }
    }

    /**
     * 제목 수정
     */
    private void updateProductTitle(Product product) {
        System.out.print("새로운 제목 입력 > ");
        String newTitle = sc.nextLine().trim();
        if (!newTitle.isEmpty()) {
            product.setTitle(newTitle);
            System.out.println("제목이 변경되었습니다.");
        }
    }

    /**
     * 가격 수정
     */
    private void updateProductPrice(Product product) {
        System.out.print("새로운 가격 입력 > ");
        String priceInput = sc.nextLine().trim();
        try {
            int newPrice = Integer.parseInt(priceInput);
            if (newPrice > 0) {
                product.setPrice(newPrice);
                System.out.println("가격이 변경되었습니다.");
            } else {
                System.out.println("가격은 0보다 커야 합니다.");
            }
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        }
    }

    /**
     * 상태 수정
     * - code 테이블에서 item_condition 그룹 조회 후 동적으로 목록 표시
     */
    private void updateProductCondition(Product product) {
        List<Code> conditions = codeController.getCodesByGroup("item_condition");
        ProductView.printConditionList(conditions);
        String condChoice = sc.nextLine().trim();
        if ("0".equals(condChoice)) {
            return;
        }
        try {
            int codeId = Integer.parseInt(condChoice);
            for (Code code : conditions) {
                if (code.getCodeId() == codeId) {
                    product.setConditionId(code.getCodeId());
                    product.setItemCondition(code.getName());
                    System.out.println("상태가 변경되었습니다.");
                    return;
                }
            }
            System.out.println("잘못된 입력입니다.");
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        }
    }

    /**
     * 카테고리 수정
     */
    private void updateProductCategory(Product product) {
        // 카테고리 목록 조회 (추후 DAO에서 조회)
        List<Category> categories = ProductController.categorySelectAll();

        ProductView.printCategoryList(categories);
        String catInput = sc.nextLine().trim();
        try {
            int catId = Integer.parseInt(catInput);
            if (catId == 0) {
                // 취소
                return;
            }

            // 입력한 ID가 유효한지 확인
            Category selectedCategory = findCategoryById(categories, catId);
            if (selectedCategory != null) {
                product.setCategoryId(catId);
                product.setCategoryName(selectedCategory.getName());
                System.out.println("카테고리가 변경되었습니다.");
            } else {
                System.out.println("잘못된 카테고리 번호입니다.");
            }
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        }
    }

    /**
     * 설명 수정
     */
    private void updateProductDescription(Product product) {
        System.out.print("새로운 설명 입력 > ");
        String newDesc = sc.nextLine().trim();
        if (!newDesc.isEmpty()) {
            product.setDescription(newDesc);
            System.out.println("설명이 변경되었습니다.");
        }
    }

    /**
     * 수정 확인
     */
    private boolean confirmProductUpdate(Product original, Product updated) {
        while (true) {
            SaleView.printUpdateProductConfirm(original, updated);
            String confirm = sc.nextLine().trim();

            if ("1".equals(confirm)) {
                return true;
            } else if ("0".equals(confirm)) {
                return false;
            } else {
                CommonView.printInvalidInputMessage();
                // 계속 반복
            }
        }
    }

    /**
     * Product 객체 복사 (수정용 임시 객체 생성)
     */
    private Product copyProduct(Product original) {
        Product copy = new Product(
            original.getProductId(),
            original.getSellerId(),
            original.getCategoryId(),
            original.getTitle(),
            original.getPrice(),
            original.getDescription(),
            original.getConditionId(),
            original.getStatusId(),
            original.getCreatedAt(),
            original.getIsDeleted()
        );
        copy.setCategoryName(original.getCategoryName());
        copy.setItemCondition(original.getItemCondition());
        copy.setStatus(original.getStatus());
        return copy;
    }

    /**
     * 카테고리 목록 샘플 데이터 (View 테스트용)
     * 추후 Controller/Service를 통해 실제 데이터로 대체
     */
    private List<Category> getSampleCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "노트북"));
        categories.add(new Category(2, "데스크탑"));
        categories.add(new Category(3, "모니터"));
        categories.add(new Category(4, "키보드"));
        categories.add(new Category(5, "기타"));
        return categories;
    }

    /**
     * ID로 카테고리 찾기
     */
    private Category findCategoryById(List<Category> categories, int categoryId) {
        if (categories == null) {
            return null;
        }
        for (Category category : categories) {
            if (category.getCategoryId() == categoryId) {
                return category;
            }
        }
        return null;
    }   
    
    /**
     * 상품 삭제
     */
    private void deleteProduct(List<Product> onSaleProducts) {
        SaleView.printDeleteProductHeader();

        // ON_SALE 상태인 상품만 표시
        /*if (onSaleProducts == null || onSaleProducts.isEmpty()) {
            System.out.println("삭제 가능한 상품이 없습니다.");
            CommonView.pauseScreen(sc);
            return;
        }*/

        for (Product product : onSaleProducts) {
            System.out.println("[" + product.getProductId() + "]  " +
                product.getTitle() + "  " + String.format("%,d", product.getPrice()));
        }

        System.out.println();
        System.out.print("번호 (0: 취소) > ");
        String input = sc.nextLine().trim();

        if ("0".equals(input)) {
            return;
        }

        try {
            int productId = Integer.parseInt(input);
            Product product = ProductView.findProductById(onSaleProducts, productId);

            if (product != null) {
                SaleView.printDeleteProductConfirm(product);
                String confirm = sc.nextLine().trim();

                if ("1".equals(confirm)) {
                    ProductController.productDelete(productId);
                    CommonView.printSuccessMessage("상품 삭제 완료");
                    CommonView.pauseScreen(sc);
                } else if ("0".equals(confirm)) {
                    CommonView.printCancelMessage("상품 삭제를 취소했습니다.");
                } else {
                    CommonView.printInvalidInputMessage();
                }
            } else {
                System.out.println("해당 상품을 찾을 수 없습니다.");
            }
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        }
    }

    /**
     * 전체 상품 이력 조회
     */
    private void viewAllProductHistory() {
        // 샘플 데이터 가져오기 (추후 Controller를 통해 실제 데이터 조회)
        List<Product> allProducts = ProductController.productSelectBySellerAll();

        // SaleView를 사용하여 전체 상품 이력 출력
        SaleView.printAllProductHistory(allProducts);

        CommonView.waitForBack(sc);
    }

    /**
     * 전체 상품 샘플 데이터 (모든 상태 포함)
     */
    private List<Product> getSampleAllProducts() {
        List<Product> products = new ArrayList<>();
        String currentUserId = SessionManager.getCurrentUserId();

        // 판매중인 상품
        products.addAll(getSampleProductsByStatus("ON_SALE"));

        // 거래중인 상품
        products.addAll(getSampleProductsByStatus("RESERVED"));

        // 완료된 상품
        products.addAll(getSampleProductsByStatus("COMPLETED"));

        // 추가 샘플 - 완료된 상품 더 추가
        Product p6 = new Product(206, currentUserId, 8, "삼성 DDR4 16GB", 90000,
                "삼성 DDR4 16GB 메모리",
                2, 9, "2024-02-15", "N");
        p6.setCategoryName("RAM");
        p6.setItemCondition("중");
        p6.setStatus("COMPLETED");
        products.add(p6);

        Product p7 = new Product(207, currentUserId, 3, "LG 24인치 모니터", 150000,
                "LG 24인치 모니터 판매",
                2, 8, "2024-01-20", "N");
        p7.setCategoryName("모니터");
        p7.setItemCondition("중");
        p7.setStatus("COMPLETED");
        products.add(p7);

        return products;
    }

    /**
     * 상품 등록
     */
    private void registerProduct() {
        SaleView.printRegisterProductHeader();

        // 상품 정보 입력
        Product newProduct = inputProductInfo();
        if (newProduct == null) {
            return; // 취소
        }
        
        // 등록 확인
        if (confirmProductRegistration(newProduct)) {
            System.out.println("\n[TODO] 상품 등록 처리 - Controller/Service 연동 예정");
            CommonView.printSuccessMessage("상품 등록 완료");
        } else {
            System.out.println("\n상품 등록을 취소했습니다.");
        }
    }

    /**
     * 상품 정보 입력
     */
    private Product inputProductInfo() {
        // 1. 제목 입력
        String title = inputProductTitle();
        if (title == null) return null;

        // 2. 가격 입력
        Integer price = inputProductPrice();
        if (price == null) return null;

        // 3. 상태 입력
        List<Code> conditions = codeController.getCodesByGroup("item_condition");
        Code selectedCondition = inputProductCondition(conditions);
        if (selectedCondition == null) return null;

        // 4. 카테고리 입력
        // 카테고리 목록 조회 (추후 DAO에서 조회)
    	List<Category> categories = ProductController.categorySelectAll();
    	
    	Category category = inputProductCategoryId(categories);
        if (category == null) return null;

        // 5. 설명 입력
        String description = inputProductDescriptionForRegister();
        if (description == null) return null;

        // Product 객체 생성
        String currentUserId = SessionManager.getCurrentUserId();
        Product product = new Product(currentUserId, category.getCategoryId(), title, price, description, selectedCondition.getCodeId());

        // 카테고리명 설정
        product.setCategoryName(category.getName());

        // 상태명 설정 (code 테이블에서 조회한 name 사용)
        product.setItemCondition(selectedCondition.getName());

        return product;
    }

    /**
     * 제목 입력
     */
    private String inputProductTitle() {
        System.out.println("\n[1/5] 제목 입력");
        System.out.print("상품 제목 입력 (0: 취소) > ");
        String title = sc.nextLine().trim();
        if ("0".equals(title)) {
            return null;
        }
        if (title.isEmpty()) {
            System.out.println("제목을 입력해주세요.");
            return inputProductTitle(); // 재귀 호출
        }
        return title;
    }

    /**
     * 가격 입력
     */
    private Integer inputProductPrice() {
        System.out.println("\n[2/5] 가격 입력");
        System.out.print("가격 입력 (0: 취소) > ");
        String priceInput = sc.nextLine().trim();
        if ("0".equals(priceInput)) {
            return null;
        }
        try {
            int price = Integer.parseInt(priceInput);
            if (price <= 0) {
                System.out.println("가격은 0보다 커야 합니다.");
                return inputProductPrice(); // 재귀 호출
            }
            return price;
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
            return inputProductPrice(); // 재귀 호출
        }
    }

    /**
     * 상태 입력
     * - code 테이블에서 조회된 item_condition 목록을 동적으로 표시
     * - 사용자가 code_id를 입력하면 해당 Code 객체 반환 (취소 시 null)
     *
     * @param conditions code 테이블에서 조회된 상태 목록
     * @return 선택된 Code 객체, 취소 시 null
     */
    private Code inputProductCondition(List<Code> conditions) {
        System.out.println("\n[3/5] 상태 선택");
        ProductView.printConditionList(conditions);
        String condInput = sc.nextLine().trim();
        if ("0".equals(condInput)) {
            return null;
        }
        try {
            int codeId = Integer.parseInt(condInput);
            for (Code code : conditions) {
                if (code.getCodeId() == codeId) {
                    return code;
                }
            }
            System.out.println("잘못된 입력입니다.");
            return inputProductCondition(conditions); // 재귀 호출
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
            return inputProductCondition(conditions); // 재귀 호출
        }
    }

    /**
     * 카테고리 입력
     */
    private Category inputProductCategoryId(List<Category> categories) {
        System.out.println("\n[4/5] 카테고리 선택");
        ProductView.printCategoryList(categories);
        String catInput = sc.nextLine().trim();
        if ("0".equals(catInput)) {
            return null;
        }
        try {
            int catId = Integer.parseInt(catInput);

            // 입력한 ID가 유효한지 확인
            Category selectedCategory = findCategoryById(categories, catId);
            if (selectedCategory != null) {
                return selectedCategory;
            } else {
                System.out.println("잘못된 카테고리 번호입니다.");
                return inputProductCategoryId(categories); // 재귀 호출
            }
        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
            return inputProductCategoryId(categories); // 재귀 호출
        }
    }

    /**
     * 설명 입력 (등록용)
     */
    private String inputProductDescriptionForRegister() {
        System.out.println("\n[5/5] 설명 입력");
        System.out.print("상품 설명 입력 (0: 취소) > ");
        String description = sc.nextLine().trim();
        if ("0".equals(description)) {
            return null;
        }
        if (description.isEmpty()) {
            System.out.println("설명을 입력해주세요.");
            return inputProductDescriptionForRegister(); // 재귀 호출
        }
        return description;
    }

    /**
     * 상품 등록 확인
     */
    private boolean confirmProductRegistration(Product product) {
        while (true) {
            SaleView.printRegisterProductConfirm(product);
            String confirm = sc.nextLine().trim();
            
            if ("1".equals(confirm)) {
            	ProductController.productInsert(product);
                return true;
            } else if ("0".equals(confirm)) {
                return false;
            } else {
                CommonView.printInvalidInputMessage();
                // 계속 반복
            }
        }
    }

    /**
     * 즐겨찾기 카테고리 관리
     */
    private void manageFavorites() {
        while (true) {
            // TODO: Controller 연동 - 즐겨찾기 목록 조회
            // List<Category> favoriteCategories = favoriteController.getFavoriteCategories(SessionManager.getCurrentUserId());
            List<FavoriteCategory> favoriteCategories = ProductController.favCategorySelectAll();

            FavoriteView.printFavoriteManagement(favoriteCategories);
            String menu = sc.nextLine().trim();

            switch (menu) {
                case "1":
                    addFavoriteCategory();
                    break;
                case "2":
                    removeFavoriteCategory();
                    break;
                case "0":
                    return;
                default:
                    CommonView.printInvalidInputMessage();
            }
        }
    }

    /**
     * 즐겨찾기 추가
     */
    private void addFavoriteCategory() {
        FavoriteView.printAddFavoriteHeader();

        // TODO: Controller 연동 - 전체 카테고리 조회
        // List<Category> allCategories = categoryController.getAllCategories();
        List<Category> allCategories = ProductController.categorySelectAll();

        ProductView.printCategoryList(allCategories);
        String input = sc.nextLine().trim();

        if ("0".equals(input)) {
            return;
        }

        try {
            int categoryId = Integer.parseInt(input);
            FavoriteCategory selectedCategory = findFavoriteCategoryFromCategories(allCategories, categoryId);

            if (selectedCategory == null) {
                System.out.println("잘못된 카테고리 번호입니다.");
                return;
            }

            ProductController.favCategoryInsert(categoryId);
            FavoriteView.printAddFavoriteSuccess(selectedCategory);

        } catch (NumberFormatException e) {
            CommonView.printInvalidNumberMessage();
        }
    }

    /**
     * 즐겨찾기 삭제
     */
    private void removeFavoriteCategory() {
        while (true) {
            // TODO: Controller 연동 - 즐겨찾기 목록 조회
            // List<Category> favorites = favoriteController.getFavoriteCategories(SessionManager.getCurrentUserId());
            List<FavoriteCategory> favorites = ProductController.favCategorySelectAll();

            FavoriteView.printRemoveFavoriteList(favorites);
            String input = sc.nextLine().trim();

            if ("0".equals(input)) {
                return;
            }

            /*if (favorites == null || favorites.isEmpty()) {
                return;
            }*/

            try {
                int categoryId = Integer.parseInt(input);
                FavoriteCategory selectedCategory = findFavoriteCategoryFromFavorites(favorites, categoryId);

                if (selectedCategory == null) {
                    System.out.println("잘못된 번호입니다.");
                    continue;
                }

                FavoriteView.printRemoveFavoriteConfirm(selectedCategory);
                String confirm = sc.nextLine().trim();

                if ("1".equals(confirm)) {
                    // TODO: Controller 연동 - 즐겨찾기 삭제
                    // favoriteController.removeFavorite(SessionManager.getCurrentUserId(), categoryId);
                	ProductController.favCategoryDelete(categoryId);
                    FavoriteView.printRemoveFavoriteSuccess(selectedCategory);
                    return;
                } else if ("0".equals(confirm)) {
                    System.out.println("\n삭제를 취소했습니다.");
                    return;
                } else {
                    CommonView.printInvalidInputMessage();
                }

            } catch (NumberFormatException e) {
                CommonView.printInvalidNumberMessage();
            }
        }
    }

    /**
     * List<FavoriteCategory>에서 탐색 (addFavoriteCategory용)
     * */
    private FavoriteCategory findFavoriteCategoryFromCategories(List<Category> categories, int categoryId) {
    	if (categories == null) {
            return null;
        }
        for (Category category : categories) {
        	if(category.getCategoryId() == categoryId) {
        		FavoriteCategory favCategory = new FavoriteCategory();
        		favCategory.setCategoryId(category.getCategoryId());
        		favCategory.setCategoryName(category.getName());
            return favCategory;
            }
        }
        return null;
    }
    
    /**
     * List<FavoriteCategory>에서 탐색 (removeFavoriteCategory용)
     * */
    private FavoriteCategory findFavoriteCategoryFromFavorites(List<FavoriteCategory> favorites, int categoryId) {
        for (FavoriteCategory fc : favorites) {
            if (fc.getCategoryId() == categoryId) {
                return fc;
            }
        }
        return null;
    }
    

    /**
     * 즐겨찾기 카테고리 샘플 데이터 (View 테스트용)
     * 추후 Controller/Service를 통해 실제 데이터로 대체
     */
    private List<Category> getSampleFavoriteCategories() {
        List<Category> favorites = new ArrayList<>();
        favorites.add(new Category(7, "GPU"));
        favorites.add(new Category(6, "CPU"));
        return favorites;
    }

    /**
     * 알림 확인 (전체 목록)
     */
    private void viewNotifications() {
        while (true) {
            // TODO: Controller 연동 - 전체 알림 조회
            // List<Notification> list = notificationController.getNotifications(SessionManager.getCurrentUserId());
            List<Notification> list = getSampleNotifications();

            NotificationView.printNotificationList(list);
            String input = sc.nextLine().trim();

            switch (input) {
                case "1":
                    viewUnreadNotifications();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    /**
     * 안읽은 알림 보기 및 읽음 처리
     */
    private void viewUnreadNotifications() {
        while (true) {
            // TODO: Controller 연동 - 안읽은 알림 조회
            // List<Notification> unreadList = notificationController.getUnreadNotifications(SessionManager.getCurrentUserId());
            List<Notification> unreadList = getSampleUnreadNotifications();

            NotificationView.printUnreadNotificationList(unreadList);
            String input = sc.nextLine().trim();

            if ("0".equals(input)) {
                return;
            }

            try {
                int notificationId = Integer.parseInt(input);

                // TODO: Controller 연동 - 읽음 처리
                // notificationController.markAsRead(notificationId);

                CommonView.printSuccessMessage("알림을 읽음 처리했습니다.");
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }

    /**
     * 샘플 전체 알림 데이터 (View 테스트용)
     * 추후 Controller/Service를 통해 실제 데이터로 대체
     */
    private List<Notification> getSampleNotifications() {
        List<Notification> list = new ArrayList<>();
        list.add(new Notification(1, SessionManager.getCurrentUserId(),
                "[구매알림] \"LG 그램 17인치 노트북\" 구매가 완료되었습니다.", "0", "2024-03-10"));
        list.add(new Notification(2, SessionManager.getCurrentUserId(),
                "[판매알림] \"RTX 4070 Ti SUPER\" 상품이 구매 확정되었습니다.", "0", "2024-03-09"));
        list.add(new Notification(3, SessionManager.getCurrentUserId(),
                "[시스템] 다나가에 오신 것을 환영합니다!", "1", "2024-03-01"));
        return list;
    }

    /**
     * 샘플 안읽은 알림 데이터 (View 테스트용)
     * 추후 Controller/Service를 통해 실제 데이터로 대체
     */
    private List<Notification> getSampleUnreadNotifications() {
        List<Notification> list = new ArrayList<>();
        list.add(new Notification(1, SessionManager.getCurrentUserId(),
                "[구매알림] \"LG 그램 17인치 노트북\" 구매가 완료되었습니다.", "0", "2024-03-10"));
        list.add(new Notification(2, SessionManager.getCurrentUserId(),
                "[판매알림] \"RTX 4070 Ti SUPER\" 상품이 구매 확정되었습니다.", "0", "2024-03-09"));
        return list;
    }

    /**
     * 잔액 충전
     */
    private void chargeBalance() {
        while (true) {
            int currentBalance = SessionManager.getCurrentUser().getBalance();

            BalanceView.printChargeBalanceHeader(currentBalance);

            String input = sc.nextLine().trim();

            // 돌아가기
            if ("0".equals(input)) {
                return;
            }

            // 금액 입력 검증
            try {
                int chargeAmount = Integer.parseInt(input);

                // 금액 유효성 검증
                if (chargeAmount <= 0) {
                    CommonView.printErrorMessage("0원보다 큰 금액을 입력해주세요.");
                    continue;
                }

                //최대 충전제한 필요시
//                if (chargeAmount > 10000000) {
//                    System.out.println("\n1회 최대 충전 금액은 10,000,000원입니다.");
//                    continue;
//                }

                // 충전 확인 화면
                BalanceView.printChargeConfirmScreen(chargeAmount, currentBalance);

                String confirm = sc.nextLine().trim();

                if ("1".equals(confirm)) {
                    // 충전 처리 (추후 Controller/Service 연동)
                   
                    BalanceView.printChargeSuccess(chargeAmount, currentBalance + chargeAmount);
                    return;
                } else if ("0".equals(confirm)) {
                    CommonView.printCancelMessage("충전을 취소했습니다.");
                    continue;
                } else {
                    CommonView.printInvalidInputMessage();
                    continue;
                }

            } catch (NumberFormatException e) {
                CommonView.printInvalidNumberMessage();
            }
        }
    }
}
