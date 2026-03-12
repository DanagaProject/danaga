package dto;

/**
 * 즐겨찾기 카테고리 DTO
 */
public class FavoriteCategory {
    private int favId;
    private String userId;
    private int categoryId;

    // JOIN용 추가 필드
    private String categoryName;

    // 기본 생성자
    public FavoriteCategory() {
    }

    // 등록용 생성자 (favId 제외)
    public FavoriteCategory(String userId, int categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
    }

    // 전체 필드 생성자
    public FavoriteCategory(int favId, String userId, int categoryId) {
        this.favId = favId;
        this.userId = userId;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public int getFavId() {
        return favId;
    }

    public void setFavId(int favId) {
        this.favId = favId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "FavoriteCategory{" +
                "favId=" + favId +
                ", userId='" + userId + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
