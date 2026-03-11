-- 다나가 (Danaga) 중고 거래 플랫폼 DB 스키마
-- DATETIME 사용 (TIMESTAMP 대신)
create database danaga_local_my;
use danaga_local_my;

-- 1. 회원 테이블
CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    balance INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    role VARCHAR(20) DEFAULT 'USER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. 카테고리 테이블
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 3. 상품 테이블
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    seller_id VARCHAR(50) NOT NULL,
    category_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    price INT NOT NULL,
    description TEXT,
    item_condition VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'ON_SALE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

-- 4. 주문 테이블
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    buyer_id VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 5. 알림 테이블
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 6. 즐겨찾기 카테고리 테이블
CREATE TABLE favorite_categories (
    user_id VARCHAR(50) NOT NULL,
    category_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, category_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

-- 인덱스 생성
CREATE INDEX idx_products_seller ON products(seller_id);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_status ON products(status);
CREATE INDEX idx_orders_buyer ON orders(buyer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_read ON notifications(is_read);

-- 초기 데이터: 카테고리
INSERT INTO categories (name) VALUES
    ('노트북'),
    ('데스크탑'),
    ('모니터'),
    ('키보드'),
    ('마우스'),
    ('CPU'),
    ('GPU'),
    ('RAM'),
    ('저장장치'),
    ('기타');

-- 초기 데이터: 관리자 계정 (비밀번호: admin)
INSERT INTO users (user_id, password, balance, status, role) VALUES
    ('admin', 'admin', 0, 'ACTIVE', 'ADMIN');

-- 주요 변경사항:
-- TIMESTAMP → DATETIME 변경
-- created_at: DATETIME DEFAULT CURRENT_TIMESTAMP
-- updated_at: DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
