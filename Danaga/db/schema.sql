-- 다나가 (Danaga) 중고 거래 플랫폼 DB 스키마
-- DATETIME 사용 (TIMESTAMP 대신)
create database danaga_local_my;
use danaga_local_my;

select * from users;
select * from categories;
select * from products;
select * from orders;
select * from notifications;
select * from favorite_categories;

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

-- 3. 상품 상태 테이블
CREATE TABLE item_conditions (
    condition_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 4. 상품 테이블
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    seller_id VARCHAR(50) NOT NULL,
    category_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    price INT NOT NULL,
    description TEXT,
    condition_id INT NOT NULL,
    status CHAR(1) DEFAULT '0' COMMENT '0:on_sale, 1:reserved, 2:completed',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES users(user_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    FOREIGN KEY (condition_id) REFERENCES item_conditions(condition_id)
);

-- 5. 주문 테이블
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    buyer_id VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (buyer_id) REFERENCES users(user_id)
);

-- 6. 알림 테이블
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 7. 즐겨찾기 카테고리 테이블
CREATE TABLE favorite_categories (
  fav_id int NOT NULL AUTO_INCREMENT,
  user_id varchar(50) NOT NULL,
  category_id int NOT NULL,
  created_at datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (fav_id),
  UNIQUE KEY unique_user_category (user_id,category_id),
  KEY category_id (category_id),
  FOREIGN KEY (user_id) REFERENCES user (user_id),
  FOREIGN KEY (category_id) REFERENCES category (category_id)
);

-- 인덱스 생성
CREATE INDEX idx_products_seller ON products(seller_id);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_condition ON products(condition_id);
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
    ('기타');

-- 초기 데이터: 상품 상태
INSERT INTO item_conditions (name) VALUES
    ('상'),
    ('중'),
    ('하');

-- 초기 데이터: 관리자 계정 (비밀번호: admin)
INSERT INTO users (user_id, password, balance, status, role) VALUES
    ('admin', 'admin', 0, 'ACTIVE', 'ADMIN');

-- 주요 변경사항:
-- TIMESTAMP → DATETIME 변경
-- created_at: DATETIME DEFAULT CURRENT_TIMESTAMP
-- updated_at: DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
-- ON DELETE CASCADE 제거
-- favorite_categories에 AUTO_INCREMENT PRIMARY KEY (fav_id) 추가
-- item_condition을 별도 테이블(item_conditions)로 분리
-- products.status를 CHAR(1) 타입으로 변경 (0:on_sale, 1:reserved, 2:completed)
