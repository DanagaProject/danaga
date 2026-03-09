package dto;

import java.io.Serializable;

/**
 * 카테고리 DTO
 */
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private int categoryId;
    private String name;

    // 기본 생성자
    public Category() {
    }

    // 등록용 생성자 (ID 제외)
    public Category(String name) {
        this.name = name;
    }

    // 전체 필드 생성자
    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                '}';
    }
}
