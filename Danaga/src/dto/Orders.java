package dto;

/**
 * 주문 DTO (orders 테이블)
 * status 값 매핑 (code 테이블):
 * - code_id 4 → name 'PENDING' (대기중)
 * - code_id 5 → name 'SHIPPING' (배송중)
 * - code_id 6 → name 'CANCEL_REQUEST' (취소 요청)
 * - code_id 7 → name 'CANCEL_COMPLETED' (취소 완료)
 * - code_id 8 → name 'CANCEL_REJECTED' (취소 거부)
 * - code_id 9 → name 'COMPLETED' (완료)
 */
public class Orders {
    private int ordersId; // DB 컬럼: orders_id
    private int productId;
    private String buyerId;
    private int statusId; // DB 실제 컬럼
    private String createdAt;

    // JOIN용 추가 필드
    private String productTitle;
    private int productPrice;
    private String sellerId;
    private String status; // code 테이블과 JOIN해서 얻은 name 값 (PENDING/SHIPPING/COMPLETED 등)

    // 기본 생성자
    public Orders() {
    }

    // 등록용 생성자 (ID, createdAt 제외)
    public Orders(int productId, String buyerId) {
        this.productId = productId;
        this.buyerId = buyerId;
        this.statusId = 4; // PENDING
    }

    // 전체 필드 생성자
    public Orders(int ordersId, int productId, String buyerId, int statusId, String createdAt) {
        this.ordersId = ordersId;
        this.productId = productId;
        this.buyerId = buyerId;
        this.statusId = statusId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ordersId=" + ordersId +
                ", productTitle='" + productTitle + '\'' +
                ", productPrice=" + productPrice +
                ", statusId=" + statusId +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
