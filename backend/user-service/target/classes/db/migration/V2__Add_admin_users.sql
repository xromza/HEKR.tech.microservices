INSERT INTO users (id, login, password_hash, role, created_at, is_approved, client_type, phone, email) VALUES
(0, 'system', '0', 'ADMIN', NOW(), false, 'LEGAL', '0', '0'),
(1, 'admin_main', '$2a$10$q1eAwtoUGtKRRHFF/4JyBOzQOGgYdzEQwykqWOdqkBXFQ.nXI4OhK', 'ADMIN', NOW(), true, 'INDIVIDUAL', '+74951000001', 'admin@hekr.tech');
