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
    private String updatedAt;
    
    // View 가공용 추가 변수
    private String formattedDate;
    private String guideMessage;
    // DAO 조인 쿼리에서 가져올 상태 이름
    private String statusName;
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


    public Orders(int ordersId, int productId, String buyerId, int statusId, String createdAt, String updatedAt) {

        this.ordersId = ordersId;
        this.productId = productId;
        this.buyerId = buyerId;
        this.statusId = statusId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Orders(int ordersId, int productId, String buyerId, int statusId, String createdAt ) {
    	this(ordersId, productId, buyerId, statusId, createdAt, null);
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

    public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
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
    
    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getGuideMessage() {
        return guideMessage;
    }

    public void setGuideMessage(String guideMessage) {
        this.guideMessage = guideMessage;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

	@Override
	public String toString() {
		return "Orders [ordersId=" + ordersId + ", productId=" + productId + ", buyerId=" + buyerId + ", statusId="
				+ statusId + ", createdAt=" + createdAt + ", updatedAt="+ updatedAt + ", status=" + status + "]";
	}
}