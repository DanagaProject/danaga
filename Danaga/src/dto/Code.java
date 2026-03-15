package dto;

/**
 * 공통 코드 DTO
 * - code 테이블 매핑
 * - group_name으로 코드 분류 (item_condition, order_status, product_status, user_status)
 */
public class Code {
    private int codeId;
    private String name;
    private String createdAt;
    private String groupName;

    // 기본 생성자
    public Code() {
    }

    // 전체 필드 생성자
    public Code(int codeId, String name, String createdAt, String groupName) {
        this.codeId = codeId;
        this.name = name;
        this.createdAt = createdAt;
        this.groupName = groupName;
    }

    // Getters and Setters
    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "Code{" +
                "codeId=" + codeId +
                ", name='" + name + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
