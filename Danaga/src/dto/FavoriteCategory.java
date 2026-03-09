package dto;

import java.io.Serializable;

/**
 * 즐겨찾기 카테고리 DTO
 */
public class FavoriteCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private int categoryId;

    // JOIN용 추가 필드
    private String categoryName;

    // 기본 생성자
    public FavoriteCategory() {
    }

    // 등록용 생성자
    public FavoriteCategory(String userId, int categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
    }

    // Getters and Setters
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
                "userId='" + userId + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
