package dto;

/**
 * 상품 DTO
 */
public class Product {
    private int productId;
    private String sellerId;
    private int categoryId;
    private String title;
    private int price;
    private String description;
    private int itemCondition;
    private int status;
    private String createdAt;

    // JOIN용 추가 필드
    private String categoryName;
    private String sellerName;

    // 기본 생성자
    public Product() {
    }

    // 등록용 생성자 (ID, status, createdAt 제외)
    public Product(String sellerId, int categoryId, String title, int price,
                   String description, int itemCondition) {
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.title = title;
        this.price = price;
        this.description = description;
        this.itemCondition = itemCondition;
        this.status = "ON_SALE";
    }

    // 전체 필드 생성자
    public Product(int productId, String sellerId, int categoryId, String title,
                   int price, String description, int itemCondition,
                   int status, String createdAt) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.title = title;
        this.price = price;
        this.description = description;
        this.itemCondition = itemCondition;
        this.status = status;
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

    public int getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(int itemCondition) {
        this.itemCondition = itemCondition;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", itemCondition=" + itemCondition +
                ", status=" + status +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
