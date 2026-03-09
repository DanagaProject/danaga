package dto;

import java.io.Serializable;

/**
 * 주문 DTO
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderId;
    private int productId;
    private String buyerId;
    private OrderStatus status;
    private String createdAt;

    // JOIN용 추가 필드
    private String productTitle;
    private int productPrice;
    private String sellerId;

    // 기본 생성자
    public Order() {
    }

    // 등록용 생성자 (ID, createdAt 제외)
    public Order(int productId, String buyerId) {
        this.productId = productId;
        this.buyerId = buyerId;
        this.status = OrderStatus.PENDING;
    }

    // 전체 필드 생성자
    public Order(int orderId, int productId, String buyerId, OrderStatus status, String createdAt) {
        this.orderId = orderId;
        this.productId = productId;
        this.buyerId = buyerId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", productTitle='" + productTitle + '\'' +
                ", productPrice=" + productPrice +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
