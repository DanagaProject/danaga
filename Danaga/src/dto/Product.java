package dto;

/**
 * 상품 DTO
 *
 * itemCondition 값 매핑 (code 테이블):
 * - code_id 1 → name '상'
 * - code_id 2 → name '중'
 * - code_id 3 → name '하'
 *
 * status 값 매핑 (code 테이블):
 * - code_id 9 → name 'COMPLETED' (완료)
 * - code_id 10 → name 'ON_SALE' (판매중)
 * - code_id 11 → name 'RESERVED' (예약됨)
 */
public class Product {
    private int productId;
    private String sellerId;
    private int categoryId;
    private String title;
    private int price;
    private String description;
    private int conditionId; // DB 실제 컬럼
    private int statusId; // DB 실제 컬럼
    private String createdAt;

    // JOIN용 추가 필드
    private String categoryName;
    private String sellerName;
    private String itemCondition; // code 테이블과 JOIN해서 얻은 name 값 (상/중/하)
    private String status; // code 테이블과 JOIN해서 얻은 name 값 (ON_SALE/RESERVED)

    // 기본 생성자
    public Product() {
    }

    // 등록용 생성자 (ID, createdAt 제외)
    public Product(String sellerId, int categoryId, String title, int price,
                   String description, int conditionId) {
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.title = title;
        this.price = price;
        this.description = description;
        this.conditionId = conditionId;
        this.statusId = 10; // ON_SALE
    }

    // 전체 필드 생성자
    public Product(int productId, String sellerId, int categoryId, String title,
                   int price, String description, int conditionId,
                   int statusId, String createdAt) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.title = title;
        this.price = price;
        this.description = description;
        this.conditionId = conditionId;
        this.statusId = statusId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", conditionId=" + conditionId +
                ", statusId=" + statusId +
                ", itemCondition='" + itemCondition + '\'' +
                ", status='" + status + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
