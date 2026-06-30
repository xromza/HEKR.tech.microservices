BEGIN;

INSERT INTO categories (id, name) VALUES
(1, 'Мужская одежда'),
(2, 'Женская одежда'),
(3, 'Аксессуары'),
(4, 'Обувь');

INSERT INTO discounts (category_id, discount) VALUES
(3, 0.05),
(4, 0.03);

INSERT INTO products (id, category_id, title, description, price_retail, price_wholesale, wholesale_threshold, is_active, brand) VALUES
(1, 1, 'КУРТКА ДУТАЯ', 'Стильная дутая куртка для холодной погоды.', 19600.00, 12500.00, 3, true, 'SAINTS KELLY'),
(2, 1, 'ДЖИНСЫ ШИРОКИЕ', 'Широкие джинсы из качественного денима.', 18250.00, 17500.00, 5, true, 'SAINTS KELLY'),
(3, 4, 'ЛОФФЕРЫ КОЖАНЫЕ', 'Классические кожаные лофферы на массивной подошве.', 112158.00, 109100.00, 2, true, 'SAINTS KELLY'),
(4, 1, 'ФУТБОЛКА OVERSIZE', 'Базовая оверсайз футболка с принтом.', 29000.00, 27100.00, 10, true, 'UNDERCOVER'),
(5, 1, 'РУБАШКА В ПОЛОСКУ', 'Хлопковая рубашка в вертикальную полоску.', 48600.00, 40500.00, 5, true, 'UNDERCOVER'),
(6, 1, 'ДЖИНСОВАЯ КУРТКА "J-LIVERY"', 'Джинсовая куртка с контрастным воротником.', 58600.00, 45500.00, 3, true, 'DIESEL'),
(7, 3, 'ОЧКИ СОЛНЦЕЗАЩИТНЫЕ', 'Солнцезащитные очки в массивной оправе.', 118851.00, 105500.00, 2, true, 'PRADA'),
(8, 1, 'ШЕРСТЯНЫЕ БРЮКИ', 'Классические брюки из мягкой шерсти.', 48600.00, 40500.00, 5, true, 'DOLCE & GABBANA'),
(9, 4, 'НИЗКИЕ КЕДЫ "ERIC"', 'Дизайнерские кеды с массивными шнурками.', 34400.00, 31300.00, 4, true, 'MAISON MIHARA');

INSERT INTO products (id, category_id, title, description, price_retail, price_wholesale, wholesale_threshold, is_active, brand) VALUES
(10, 2, 'ЖАКЕТ С КОНТРАСТНЫМИ МАНЖЕТАМИ', 'Женский жакет с белыми отворотами.', 19600.00, 12500.00, 3, true, 'SAINTS KELLY'),
(11, 2, 'ДЖИНСЫ ШИРОКИЕ BLUE', 'Классический синий деним.', 18250.00, 17500.00, 5, true, 'SAINTS KELLY'),
(12, 2, 'БРЮКИ С БАНТОМ', 'Брюки цвета хаки с декоративным узлом на поясе.', 112158.00, 109100.00, 2, true, 'SAINTS KELLY'),
(13, 2, 'ЮБКА МИДИ С РАЗРЕЗОМ', 'Твидовая юбка миди в розовом цвете.', 29000.00, 27100.00, 10, true, 'UNDERCOVER'),
(14, 2, 'ТОЛСТОВКА НА МОЛНИИ', 'Желтый анорак с воротником-стойкой.', 48600.00, 40500.00, 5, true, 'UNDERCOVER'),
(15, 2, 'ДЖИНСОВАЯ КУРТКА "J-LIVERY" (LADY)', 'Женская куртка из денима с кожаным воротником.', 58600.00, 45500.00, 3, true, 'DIESEL'),
(16, 3, 'ОЧКИ PRADA BLACK', 'Солнцезащитные очки в массивной оправе (V2).', 118851.00, 105500.00, 2, true, 'PRADA');

INSERT INTO product_variants (id, product_id, size, color, weight, sku, is_active) VALUES
(1, 1, 'L', 'Черный', 1.20, 'SK-JKT-BLK-L', true),
(2, 2, '32', 'Серый', 0.80, 'SK-JNS-GRY-32', true),
(3, 3, '42', 'Черный', 1.10, 'SK-LOF-BLK-42', true),
(4, 4, 'XL', 'Белый', 0.30, 'UC-TEE-WHT-XL', true),
(5, 5, 'M', 'Голубой', 0.40, 'UC-SHT-BLU-M', true),
(6, 6, 'L', 'Синий', 0.90, 'DS-DNM-BLU-L', true),
(7, 7, 'ONE SIZE', 'Черный', 0.10, 'PR-SUN-BLK-OS', true),
(8, 8, '50', 'Черный', 0.60, 'DG-TRZ-BLK-50', true),
(9, 9, '41', 'Черный/Белый', 0.95, 'MM-KED-BW-41', true),
(10, 10, 'S', 'Черный', 0.80, 'SK-W-JKT-BLK-S', true),
(11, 11, '28', 'Синий', 0.70, 'SK-W-JNS-BLU-28', true),
(12, 12, 'M', 'Хаки', 0.65, 'SK-W-TRZ-KHK-M', true),
(13, 13, 'S', 'Розовый', 0.45, 'UC-W-SKR-PNK-S', true),
(14, 14, 'L', 'Желтый', 0.55, 'UC-W-ANR-YEL-L', true),
(15, 15, 'XL', 'Серый', 0.95, 'DS-M-DNM-GRY-XL', true),
(16, 16, 'ONE SIZE', 'Черный', 0.15, 'PR-U-SUN-BLK-OS', true);

INSERT INTO images (id, url, variant_id, type, sort_order, created_at) VALUES
-- Мужская коллекция
(1, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/jacket_main_pf3q0q.png', 1, 'MAIN', 0, NOW()),
(2, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/jacket_back_sbakfh.png', 1, 'GALLERY', 1, NOW()),
(3, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/jacket_left_fnesux.png', 1, 'GALLERY', 2, NOW()),
(4, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/woman_main_salhpi.png', 2, 'MAIN', 0, NOW()),
(5, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/lofers_main_m4psva.png', 3, 'MAIN', 0, NOW()),
(6, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/tshirt_main_fy5znn.png', 4, 'MAIN', 0, NOW()),
(7, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/shirt_main_sqwafi.png', 5, 'MAIN', 0, NOW()),
(8, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/jdeliver_olr3fb.png', 6, 'MAIN', 0, NOW()),
(9, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/glasses_main_lcn7se.png', 7, 'MAIN', 0, NOW()),
(10, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/pants_black_main_m4crjm.png', 8, 'MAIN', 0, NOW()),
(11, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/boots_main_ttxljb.png', 9, 'MAIN', 0, NOW()),
-- Женская коллекция
(12, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/pidjak_main_q5zxiw.png', 10, 'MAIN', 0, NOW()),
(13, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/jeans_main_zm2yoq.png', 11, 'MAIN', 0, NOW()),
(14, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/pants_main_a3pmif.png', 12, 'MAIN', 0, NOW()),
(15, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/skirt_main_s17fwb.png', 13, 'MAIN', 0, NOW()),
(16, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/v1778696825/yellow_main_susggd.png', 14, 'MAIN', 0, NOW()),
(17, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/jdeliver_olr3fb.png', 15, 'MAIN', 0, NOW()),
(18, 'https://res.cloudinary.com/dcc2qkmq7/image/upload/glasses_main_lcn7se.png', 16, 'MAIN', 0, NOW());

SELECT setval('categories_id_seq', (SELECT MAX(id) FROM categories));
SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));
SELECT setval('product_variants_id_seq', (SELECT MAX(id) FROM product_variants));
SELECT setval('images_id_seq', (SELECT MAX(id) FROM images));

COMMIT;