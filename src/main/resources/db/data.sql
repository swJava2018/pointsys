-- users 생성
INSERT INTO users (id, role) VALUES (1, 'USER');

-- point_spec 생성
INSERT INTO point_spec (id, min_save_amount, max_save_amount, min_days_from_now, max_days_from_now, default_expiry_days) VALUES (1, 1, 100000, 1, 1825, 365);

-- user_point_limit 생성
INSERT INTO user_point_limit (user_id, point_spec_id, max_earning_limit) VALUES (1, 1, 50000);