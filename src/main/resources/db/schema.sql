-- Create users table
CREATE TABLE users (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,              -- 사용자 계정 ID
    role VARCHAR(255) NOT NULL,                         -- 권한 ('ADMIN': 관리자, 'USER': 사용자)
    created_at DATETIME(0) DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Create point_spec table
CREATE TABLE point_spec (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,              -- 포인트 정책 ID
    min_save_amount INTEGER,                            -- 1회에 최소 적립가능 수량
    max_save_amount INTEGER,                            -- 1회에 최대 적립가능 수량
    min_days_from_now INTEGER,                          -- 최소 가능 만료일
    max_days_from_now INTEGER,                          -- 최대 가능 만료일
    default_expiry_days INTEGER,                        -- 기반 만료일
    created_at DATETIME(0) DEFAULT CURRENT_TIMESTAMP
);

-- Create user_point_limit table
CREATE TABLE user_point_limit (
    user_id INTEGER,                                    -- 사용자 계정 ID
    point_spec_id INTEGER,                              -- 포인트 정책 ID
    max_earning_limit INTEGER NOT NULL,                 -- 포인트 최대 적립 가능 수량
    updated_at DATETIME(0) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, point_spec_id),
    CONSTRAINT fk_user_point_limit_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_point_limit_point_spec FOREIGN KEY (point_spec_id) REFERENCES point_spec(id)
);

-- Create point table (포인트 적립/차감)
CREATE TABLE point (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,              -- 포인트 적립내역 ID
    user_id INTEGER NOT NULL,                           -- 사용자 계정 ID
    point_spec_id INTEGER NOT NULL,                     -- 포인트 정책 ID
    amount INTEGER NOT NULL,                            -- 포인트 수량
    expiry_date DATETIME(0),                            -- 포인트 만료일
    type VARCHAR(255) NOT NULL,                         -- 타입 ('MANUAL': 관리자, 'AUTO': 자동)
    state INTEGER NOT NULL,                             -- 상태 (0: 적립완료, 1: 적립취소, 2: 사용완료, 3: 사용취소)
    created_at DATETIME(0) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at DATETIME(0) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_point_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_point_point_spec FOREIGN KEY (point_spec_id) REFERENCES point_spec(id)
);

--Create point_balance table (포인트 잔액)
CREATE TABLE point_balance (
    point_id INTEGER PRIMARY KEY,                       -- 포인트 적립 내역 ID
    remaining_amount INTEGER NOT NULL,                  -- 잔여 포인트 수량
    created_at DATETIME(0) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at DATETIME(0) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_point_balance_point FOREIGN KEY (point_id) REFERENCES point(id)
);

-- Create point_usage table (포인트 사용)
CREATE TABLE point_usage (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,              -- 포인트 사용내역 ID
    order_id VARCHAR(255),                              -- 주문번호
    point_id INTEGER,                                   -- 사용된 포인트 ID
    amount INTEGER,                                     -- 사용된 포인트 수량
    created_at DATETIME(0) DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME(0) DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_point_usage_point FOREIGN KEY (point_id) REFERENCES point(id)
);

