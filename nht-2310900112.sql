-- TẠO DATABASE MỚI
CREATE DATABASE tap_hoa_nht CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tap_hoa_nht;

-- ============================================
-- 1. BẢNG DANH MỤC SẢN PHẨM
-- ============================================
CREATE TABLE danh_muc (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ten_danh_muc VARCHAR(100) NOT NULL,
    mo_ta TEXT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_ten_danh_muc (ten_danh_muc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 2. BẢNG SẢN PHẨM
-- ============================================
CREATE TABLE san_pham (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ten_san_pham VARCHAR(200) NOT NULL,
    mo_ta TEXT,
    gia DECIMAL(15,2) NOT NULL,
    so_luong_ton INT NOT NULL DEFAULT 0,
    hinh_anh VARCHAR(500),
    danh_muc_id BIGINT,
    trang_thai VARCHAR(50) DEFAULT 'AVAILABLE',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (danh_muc_id) REFERENCES danh_muc(id) ON DELETE SET NULL,
    INDEX idx_danh_muc (danh_muc_id),
    INDEX idx_trang_thai (trang_thai),
    INDEX idx_ten_san_pham (ten_san_pham)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 3. BẢNG KHÁCH HÀNG
-- FIX: Bỏ UNIQUE constraint cho email để tránh lỗi duplicate
-- ============================================
CREATE TABLE khach_hang (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    so_dien_thoai VARCHAR(20),
    dia_chi TEXT,
    mat_khau VARCHAR(255),
    role VARCHAR(20) DEFAULT 'CUSTOMER',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_so_dien_thoai (so_dien_thoai)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 4. BẢNG ĐƠN HÀNG
-- ============================================
CREATE TABLE don_hang (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ma_don_hang VARCHAR(50) UNIQUE NOT NULL,
    khach_hang_id BIGINT,
    tong_tien DECIMAL(15,2) NOT NULL,
    phuong_thuc_thanh_toan VARCHAR(50) NOT NULL,
    trang_thai_thanh_toan VARCHAR(50) DEFAULT 'CHUA_THANH_TOAN',
    trang_thai_don_hang VARCHAR(50) DEFAULT 'CHO_XU_LY',
    dia_chi_giao_hang TEXT,
    ghi_chu TEXT,
    ngay_dat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (khach_hang_id) REFERENCES khach_hang(id) ON DELETE SET NULL,
    INDEX idx_khach_hang (khach_hang_id),
    INDEX idx_ma_don_hang (ma_don_hang),
    INDEX idx_trang_thai_don (trang_thai_don_hang),
    INDEX idx_trang_thai_tt (trang_thai_thanh_toan),
    INDEX idx_ngay_dat (ngay_dat)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 5. BẢNG CHI TIẾT ĐƠN HÀNG
-- ============================================
CREATE TABLE chi_tiet_don_hang (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    don_hang_id BIGINT NOT NULL,
    san_pham_id BIGINT NOT NULL,
    so_luong INT NOT NULL,
    gia_ban DECIMAL(15,2) NOT NULL,
    thanh_tien DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (don_hang_id) REFERENCES don_hang(id) ON DELETE CASCADE,
    FOREIGN KEY (san_pham_id) REFERENCES san_pham(id) ON DELETE RESTRICT,
    INDEX idx_don_hang (don_hang_id),
    INDEX idx_san_pham (san_pham_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 6. BẢNG THANH TOÁN
-- ============================================
CREATE TABLE thanh_toan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    don_hang_id BIGINT NOT NULL,
    so_tien DECIMAL(15,2) NOT NULL,
    phuong_thuc VARCHAR(50) NOT NULL,
    trang_thai VARCHAR(50) DEFAULT 'THANH_CONG',
    ma_giao_dich VARCHAR(100),
    ngay_thanh_toan TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (don_hang_id) REFERENCES don_hang(id) ON DELETE CASCADE,
    INDEX idx_don_hang (don_hang_id),
    INDEX idx_ma_giao_dich (ma_giao_dich)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- DỮ LIỆU MẪU
-- ============================================

-- 1. DANH MỤC (5 danh mục)
INSERT INTO danh_muc (ten_danh_muc, mo_ta) VALUES
('Đồ uống', 'Nước ngọt, nước suối, trà, café các loại'),
('Bánh kẹo', 'Bánh snack, kẹo, socola các loại'),
('Mì gói', 'Mì tôm, mì ly, hủ tiếu các loại'),
('Gia vị', 'Dầu ăn, nước mắm, hạt nêm, gia vị'),
('Vệ sinh cá nhân', 'Dầu gội, sữa tắm, kem đánh răng');

-- 2. SẢN PHẨM (20 sản phẩm với đường dẫn ảnh)
INSERT INTO san_pham (ten_san_pham, mo_ta, gia, so_luong_ton, danh_muc_id, hinh_anh, trang_thai) VALUES
-- Đồ uống (4 sản phẩm)
('Coca Cola 330ml', 'Nước ngọt có gas Coca Cola lon 330ml', 10000, 100, 1, '/images/products/cocacola.jpg', 'AVAILABLE'),
('Pepsi 330ml', 'Nước ngọt có gas Pepsi lon 330ml', 10000, 80, 1, '/images/products/pepsi.jpg', 'AVAILABLE'),
('Nước suối Lavie 500ml', 'Nước suối Lavie chai 500ml', 5000, 200, 1, '/images/products/lavie.jpg', 'AVAILABLE'),
('Trà xanh không độ 350ml', 'Trà xanh không độ C2 chai 350ml', 8000, 50, 1, '/images/products/tra-xanh.jpg', 'AVAILABLE'),

-- Bánh kẹo (5 sản phẩm)
('Snack Oishi 40g', 'Snack khoai tây vị bò nướng Oishi', 5000, 120, 2, '/images/products/oishi.jpg', 'AVAILABLE'),
('Bánh Oreo 133g', 'Bánh quy Oreo vị vani', 15000, 60, 2, '/images/products/oreo.jpg', 'AVAILABLE'),
('Kẹo Mentos 37.5g', 'Kẹo Mentos vị trái cây', 8000, 90, 2, '/images/products/mentos.jpg', 'AVAILABLE'),
('Snack Lays 56g', 'Snack khoai tây Lays vị kem chua hành', 12000, 75, 2, '/images/products/lays.jpg', 'AVAILABLE'),
('Kẹo Alpenliebe 420g', 'Kẹo sữa Alpenliebe túi 420g', 35000, 40, 2, '/images/products/alpenliebe.jpg', 'AVAILABLE'),

-- Mì gói (5 sản phẩm)
('Mì Hảo Hảo tôm chua cay', 'Mì ăn liền Hảo Hảo tôm chua cay gói 75g', 4000, 300, 3, '/images/products/haohao.jpg', 'AVAILABLE'),
('Mì Kokomi tôm', 'Mì ăn liền Kokomi tôm gói 70g', 3500, 250, 3, '/images/products/kokomi.jpg', 'AVAILABLE'),
('Mì ly Vifon phở bò', 'Mì ly Vifon hương vị phở bò', 12000, 100, 3, '/images/products/vifon.jpg', 'AVAILABLE'),
('Mì Omachi sườn heo', 'Mì Omachi tôm hùm xào sườn heo', 6000, 150, 3, '/images/products/omachi.jpg', 'AVAILABLE'),
('Hủ tiếu Nam Vang 65g', 'Hủ tiếu Nam Vang ăn liền', 5000, 80, 3, '/images/products/hutieu.jpg', 'AVAILABLE'),

-- Gia vị (3 sản phẩm)
('Dầu ăn Simply 1L', 'Dầu ăn thực vật Simply chai 1 lít', 45000, 40, 4, '/images/products/simply.jpg', 'AVAILABLE'),
('Nước mắm Nam Ngư 500ml', 'Nước mắm truyền thống Nam Ngư chai 500ml', 25000, 50, 4, '/images/products/namngu.jpg', 'AVAILABLE'),
('Hạt nêm Knorr 400g', 'Hạt nêm Knorr thịt thăn xương ống gói 400g', 30000, 70, 4, '/images/products/knorr.jpg', 'AVAILABLE'),

-- Vệ sinh cá nhân (3 sản phẩm)
('Dầu gội Clear 650g', 'Dầu gội sạch gàu Clear Men chai 650g', 120000, 30, 5, '/images/products/clear.jpg', 'AVAILABLE'),
('Sữa tắm Lifebuoy 850g', 'Sữa tắm bảo vệ vượt trội Lifebuoy chai 850g', 80000, 45, 5, '/images/products/lifebuoy.jpg', 'AVAILABLE'),
('Kem đánh răng PS 230g', 'Kem đánh răng P/S toàn diện tuýp 230g', 42000, 60, 5, '/images/products/ps.jpg', 'AVAILABLE');

-- 3. KHÁCH HÀNG (1 Admin + 3 User)
INSERT INTO khach_hang (ho_ten, email, so_dien_thoai, dia_chi, mat_khau, role) VALUES
-- ADMIN
('Admin NHT', 'admin@nht.com', '0328942958', 'Hà Nội, Việt Nam', 'admin123', 'ADMIN'),

-- USER
('Nguyễn Hữu Tuấn', 'nht@nht.com', '0328942958', 'Hà Nội, Việt Nam', 'nht2310900112', 'CUSTOMER'),
('Nguyễn Văn A', 'user@nht.com', '0987654321', 'Hà Nội, Việt Nam', '123456', 'CUSTOMER'),
('Trần Thị B', 'tranb@gmail.com', '0912345678', 'Hồ Chí Minh, Việt Nam', '123456', 'CUSTOMER');

-- 4. ĐƠN HÀNG MẪU (3 đơn hàng với trạng thái khác nhau)
INSERT INTO don_hang (ma_don_hang, khach_hang_id, tong_tien, phuong_thuc_thanh_toan, trang_thai_thanh_toan, trang_thai_don_hang, dia_chi_giao_hang, ghi_chu) VALUES
-- Đơn 1: Hoàn thành
('DH20241228001', 2, 135000, 'TIEN_MAT', 'DA_THANH_TOAN', 'HOAN_THANH', 'Số 123, Đường Giải Phóng, Hà Nội', 'Giao hàng buổi chiều'),
-- Đơn 2: Đang giao
('DH20241228002', 3, 89000, 'CHUYEN_KHOAN', 'DA_THANH_TOAN', 'DANG_GIAO', 'Số 456, Đường Láng, Hà Nội', 'Gọi trước khi giao'),
-- Đơn 3: Chờ xử lý
('DH20241228003', 2, 245000, 'TIEN_MAT', 'CHUA_THANH_TOAN', 'CHO_XU_LY', 'Số 789, Đường Nguyễn Trãi, Hà Nội', NULL);

-- 5. CHI TIẾT ĐƠN HÀNG
-- Đơn 1
INSERT INTO chi_tiet_don_hang (don_hang_id, san_pham_id, so_luong, gia_ban, thanh_tien) VALUES
(1, 1, 3, 10000, 30000),  -- 3 Coca
(1, 5, 5, 5000, 25000),   -- 5 Snack Oishi
(1, 10, 20, 4000, 80000); -- 20 Mì Hảo Hảo

-- Đơn 2
INSERT INTO chi_tiet_don_hang (don_hang_id, san_pham_id, so_luong, gia_ban, thanh_tien) VALUES
(2, 2, 2, 10000, 20000),  -- 2 Pepsi
(2, 6, 3, 15000, 45000),  -- 3 Oreo
(2, 11, 4, 6000, 24000);  -- 4 Mì Kokomi

-- Đơn 3
INSERT INTO chi_tiet_don_hang (don_hang_id, san_pham_id, so_luong, gia_ban, thanh_tien) VALUES
(3, 16, 1, 45000, 45000),  -- 1 Dầu ăn
(3, 17, 2, 25000, 50000),  -- 2 Nước mắm
(3, 18, 3, 30000, 90000),  -- 3 Hạt nêm
(3, 19, 1, 60000, 60000);  -- 1 Dầu gội (giảm giá)

-- 6. THANH TOÁN (cho 2 đơn đã thanh toán)
INSERT INTO thanh_toan (don_hang_id, so_tien, phuong_thuc, trang_thai, ma_giao_dich) VALUES
(1, 135000, 'TIEN_MAT', 'THANH_CONG', 'TT20241228001'),
(2, 89000, 'CHUYEN_KHOAN', 'THANH_CONG', 'CK20241228002MBB');

-- ============================================
-- KIỂM TRA DỮ LIỆU
-- ============================================
SELECT '✅ DATABASE TẠP HÓA NHT ĐÃ ĐƯỢC TẠO THÀNH CÔNG!' AS status;

SELECT 
    'THỐNG KÊ DATABASE' AS info,
    (SELECT COUNT(*) FROM danh_muc) AS so_danh_muc,
    (SELECT COUNT(*) FROM san_pham) AS so_san_pham,
    (SELECT COUNT(*) FROM khach_hang) AS so_khach_hang,
    (SELECT COUNT(*) FROM don_hang) AS so_don_hang,
    (SELECT COUNT(*) FROM chi_tiet_don_hang) AS so_chi_tiet,
    (SELECT COUNT(*) FROM thanh_toan) AS so_thanh_toan;

-- Hiển thị thông tin đăng nhập
SELECT '📋 THÔNG TIN ĐĂNG NHẬP' AS info;
SELECT 
    ho_ten AS 'Họ tên',
    email AS 'Email',
    mat_khau AS 'Password',
    role AS 'Vai trò'
FROM khach_hang 
ORDER BY FIELD(role, 'ADMIN', 'CUSTOMER'), id;

-- Hiển thị danh sách sản phẩm
SELECT '📦 DANH SÁCH SẢN PHẨM' AS info;
SELECT 
    sp.id AS 'ID',
    sp.ten_san_pham AS 'Tên sản phẩm',
    dm.ten_danh_muc AS 'Danh mục',
    CONCAT(FORMAT(sp.gia, 0), ' đ') AS 'Giá',
    sp.so_luong_ton AS 'Tồn kho',
    sp.trang_thai AS 'Trạng thái'
FROM san_pham sp
LEFT JOIN danh_muc dm ON sp.danh_muc_id = dm.id
ORDER BY dm.ten_danh_muc, sp.ten_san_pham;

-- Hiển thị đơn hàng
SELECT '🛒 DANH SÁCH ĐƠN HÀNG' AS info;
SELECT 
    dh.ma_don_hang AS 'Mã ĐH',
    kh.ho_ten AS 'Khách hàng',
    CONCAT(FORMAT(dh.tong_tien, 0), ' đ') AS 'Tổng tiền',
    dh.phuong_thuc_thanh_toan AS 'PT Thanh toán',
    dh.trang_thai_thanh_toan AS 'TT Thanh toán',
    dh.trang_thai_don_hang AS 'TT Đơn hàng'
FROM don_hang dh
LEFT JOIN khach_hang kh ON dh.khach_hang_id = kh.id
ORDER BY dh.ngay_dat DESC;

-- ============================================
-- HOÀN TẤT!
-- ============================================
SELECT '
🎉 DATABASE ĐÃ SẴN SÀNG!
📊 Đã tạo: 5 danh mục, 20 sản phẩm, 4 users, 3 đơn hàng mẫu
🔐 Admin: admin@nht.com / admin123
👤 User: nht@nht.com / nht2310900112
✅ Đã fix lỗi email duplicate - Giờ có thể đặt hàng bình thường!
' AS message;

-- ============================================
-- 1. BẢNG DANH MỤC SẢN PHẨM
-- ============================================
CREATE TABLE danh_muc (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ten_danh_muc VARCHAR(100) NOT NULL,
    mo_ta TEXT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 2. BẢNG SẢN PHẨM
-- ============================================
CREATE TABLE san_pham (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ten_san_pham VARCHAR(200) NOT NULL,
    mo_ta TEXT,
    gia DECIMAL(15,2) NOT NULL,
    so_luong_ton INT NOT NULL DEFAULT 0,
    hinh_anh VARCHAR(500),
    danh_muc_id BIGINT,
    trang_thai VARCHAR(50) DEFAULT 'AVAILABLE',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (danh_muc_id) REFERENCES danh_muc(id) ON DELETE SET NULL,
    INDEX idx_danh_muc (danh_muc_id),
    INDEX idx_trang_thai (trang_thai)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 3. BẢNG KHÁCH HÀNG
-- ============================================
CREATE TABLE khach_hang (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    so_dien_thoai VARCHAR(20),
    dia_chi TEXT,
    mat_khau VARCHAR(255),
    role VARCHAR(20) DEFAULT 'CUSTOMER',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role),
    UNIQUE KEY unique_email_for_registered (email, role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 4. BẢNG ĐỚN HÀNG
-- ============================================
CREATE TABLE don_hang (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ma_don_hang VARCHAR(50) UNIQUE NOT NULL,
    khach_hang_id BIGINT,
    tong_tien DECIMAL(15,2) NOT NULL,
    phuong_thuc_thanh_toan VARCHAR(50) NOT NULL,
    trang_thai_thanh_toan VARCHAR(50) DEFAULT 'CHUA_THANH_TOAN',
    trang_thai_don_hang VARCHAR(50) DEFAULT 'CHO_XU_LY',
    dia_chi_giao_hang TEXT,
    ghi_chu TEXT,
    ngay_dat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (khach_hang_id) REFERENCES khach_hang(id) ON DELETE SET NULL,
    INDEX idx_khach_hang (khach_hang_id),
    INDEX idx_ma_don_hang (ma_don_hang),
    INDEX idx_trang_thai_don (trang_thai_don_hang),
    INDEX idx_trang_thai_tt (trang_thai_thanh_toan)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 5. BẢNG CHI TIẾT ĐƠN HÀNG
-- ============================================
CREATE TABLE chi_tiet_don_hang (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    don_hang_id BIGINT NOT NULL,
    san_pham_id BIGINT NOT NULL,
    so_luong INT NOT NULL,
    gia_ban DECIMAL(15,2) NOT NULL,
    thanh_tien DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (don_hang_id) REFERENCES don_hang(id) ON DELETE CASCADE,
    FOREIGN KEY (san_pham_id) REFERENCES san_pham(id) ON DELETE RESTRICT,
    INDEX idx_don_hang (don_hang_id),
    INDEX idx_san_pham (san_pham_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 6. BẢNG THANH TOÁN
-- ============================================
CREATE TABLE thanh_toan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    don_hang_id BIGINT NOT NULL,
    so_tien DECIMAL(15,2) NOT NULL,
    phuong_thuc VARCHAR(50) NOT NULL,
    trang_thai VARCHAR(50) DEFAULT 'THANH_CONG',
    ma_giao_dich VARCHAR(100),
    ngay_thanh_toan TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (don_hang_id) REFERENCES don_hang(id) ON DELETE CASCADE,
    INDEX idx_don_hang (don_hang_id),
    INDEX idx_ma_giao_dich (ma_giao_dich)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- DỮ LIỆU MẪU
-- ============================================

-- 1. DANH MỤC (5 danh mục)
INSERT INTO danh_muc (ten_danh_muc, mo_ta) VALUES
('Đồ uống', 'Nước ngọt, nước suối, trà, café các loại'),
('Bánh kẹo', 'Bánh snack, kẹo, socola các loại'),
('Mì gói', 'Mì tôm, mì ly, hủ tiếu các loại'),
('Gia vị', 'Dầu ăn, nước mắm, hạt nêm, gia vị'),
('Vệ sinh cá nhân', 'Dầu gội, sữa tắm, kem đánh răng');

-- 2. SẢN PHẨM (20 sản phẩm với đường dẫn ảnh)
INSERT INTO san_pham (ten_san_pham, mo_ta, gia, so_luong_ton, danh_muc_id, hinh_anh, trang_thai) VALUES
-- Đồ uống (4 sản phẩm)
('Coca Cola 330ml', 'Nước ngọt có gas Coca Cola lon 330ml', 10000, 100, 1, '/images/products/cocacola.jpg', 'AVAILABLE'),
('Pepsi 330ml', 'Nước ngọt có gas Pepsi lon 330ml', 10000, 80, 1, '/images/products/pepsi.jpg', 'AVAILABLE'),
('Nước suối Lavie 500ml', 'Nước suối Lavie chai 500ml', 5000, 200, 1, '/images/products/lavie.jpg', 'AVAILABLE'),
('Trà xanh không độ 350ml', 'Trà xanh không độ C2 chai 350ml', 8000, 50, 1, '/images/products/tra-xanh.jpg', 'AVAILABLE'),

-- Bánh kẹo (5 sản phẩm)
('Snack Oishi 40g', 'Snack khoai tây vị bò nướng Oishi', 5000, 120, 2, '/images/products/oishi.jpg', 'AVAILABLE'),
('Bánh Oreo 133g', 'Bánh quy Oreo vị vani', 15000, 60, 2, '/images/products/oreo.jpg', 'AVAILABLE'),
('Kẹo Mentos 37.5g', 'Kẹo Mentos vị trái cây', 8000, 90, 2, '/images/products/mentos.jpg', 'AVAILABLE'),
('Snack Lays 56g', 'Snack khoai tây Lays vị kem chua hành', 12000, 75, 2, '/images/products/lays.jpg', 'AVAILABLE'),
('Kẹo Alpenliebe 420g', 'Kẹo sữa Alpenliebe túi 420g', 35000, 40, 2, '/images/products/alpenliebe.jpg', 'AVAILABLE'),

-- Mì gói (5 sản phẩm)
('Mì Hảo Hảo tôm chua cay', 'Mì ăn liền Hảo Hảo tôm chua cay gói 75g', 4000, 300, 3, '/images/products/haohao.jpg', 'AVAILABLE'),
('Mì Kokomi tôm', 'Mì ăn liền Kokomi tôm gói 70g', 3500, 250, 3, '/images/products/kokomi.jpg', 'AVAILABLE'),
('Mì ly Vifon phở bò', 'Mì ly Vifon hương vị phở bò', 12000, 100, 3, '/images/products/vifon.jpg', 'AVAILABLE'),
('Mì Omachi sườn heo', 'Mì Omachi tôm hùm xào sườn heo', 6000, 150, 3, '/images/products/omachi.jpg', 'AVAILABLE'),
('Hủ tiếu Nam Vang 65g', 'Hủ tiếu Nam Vang ăn liền', 5000, 80, 3, '/images/products/hutieu.jpg', 'AVAILABLE'),

-- Gia vị (3 sản phẩm)
('Dầu ăn Simply 1L', 'Dầu ăn thực vật Simply chai 1 lít', 45000, 40, 4, '/images/products/simply.jpg', 'AVAILABLE'),
('Nước mắm Nam Ngư 500ml', 'Nước mắm truyền thống Nam Ngư chai 500ml', 25000, 50, 4, '/images/products/namngu.jpg', 'AVAILABLE'),
('Hạt nêm Knorr 400g', 'Hạt nêm Knorr thịt thăn xương ống gói 400g', 30000, 70, 4, '/images/products/knorr.jpg', 'AVAILABLE'),

-- Vệ sinh cá nhân (3 sản phẩm)
('Dầu gội Clear 650g', 'Dầu gội sạch gàu Clear Men chai 650g', 120000, 30, 5, '/images/products/clear.jpg', 'AVAILABLE'),
('Sữa tắm Lifebuoy 850g', 'Sữa tắm bảo vệ vượt trội Lifebuoy chai 850g', 80000, 45, 5, '/images/products/lifebuoy.jpg', 'AVAILABLE'),
('Kem đánh răng PS 230g', 'Kem đánh răng P/S toàn diện tuýp 230g', 42000, 60, 5, '/images/products/ps.jpg', 'AVAILABLE');

-- 3. KHÁCH HÀNG (1 Admin + 3 User)
INSERT INTO khach_hang (ho_ten, email, so_dien_thoai, dia_chi, mat_khau, role) VALUES
-- ADMIN
('Admin NHT', 'admin@nht.com', '0328942958', 'Hà Nội', 'admin123', 'ADMIN'),

-- USER
('Nguyễn Hữu Tuấn', 'nht@nht.com', '0328942958', 'Hà Nội, Việt Nam', 'nht2310900112', 'CUSTOMER'),
('Nguyễn Văn A', 'user@nht.com', '0987654321', 'Hà Nội, Việt Nam', '123456', 'CUSTOMER'),
('Trần Thị B', 'tranb@gmail.com', '0912345678', 'Hồ Chí Minh, Việt Nam', '123456', 'CUSTOMER');

-- 4. ĐƠN HÀNG MẪU (3 đơn hàng)
INSERT INTO don_hang (ma_don_hang, khach_hang_id, tong_tien, phuong_thuc_thanh_toan, trang_thai_thanh_toan, trang_thai_don_hang, dia_chi_giao_hang, ghi_chu) VALUES
-- Đơn 1: Hoàn thành
('DH20241228001', 2, 135000, 'TIEN_MAT', 'DA_THANH_TOAN', 'HOAN_THANH', 'Số 123, Đường Giải Phóng, Hà Nội', 'Giao hàng buổi chiều'),
-- Đơn 2: Đang giao
('DH20241228002', 3, 89000, 'CHUYEN_KHOAN', 'DA_THANH_TOAN', 'DANG_GIAO', 'Số 456, Đường Láng, Hà Nội', 'Gọi trước khi giao'),
-- Đơn 3: Chờ xử lý
('DH20241228003', 2, 245000, 'TIEN_MAT', 'CHUA_THANH_TOAN', 'CHO_XU_LY', 'Số 789, Đường Nguyễn Trãi, Hà Nội', NULL);

-- 5. CHI TIẾT ĐƠN HÀNG
-- Đơn 1
INSERT INTO chi_tiet_don_hang (don_hang_id, san_pham_id, so_luong, gia_ban, thanh_tien) VALUES
(1, 1, 3, 10000, 30000),  -- 3 Coca
(1, 5, 5, 5000, 25000),   -- 5 Snack Oishi
(1, 8, 20, 4000, 80000);  -- 20 Mì Hảo Hảo

-- Đơn 2
INSERT INTO chi_tiet_don_hang (don_hang_id, san_pham_id, so_luong, gia_ban, thanh_tien) VALUES
(2, 2, 2, 10000, 20000),  -- 2 Pepsi
(2, 6, 3, 15000, 45000),  -- 3 Oreo
(2, 9, 4, 6000, 24000);   -- 4 Mì Kokomi

-- Đơn 3
INSERT INTO chi_tiet_don_hang (don_hang_id, san_pham_id, so_luong, gia_ban, thanh_tien) VALUES
(3, 16, 1, 45000, 45000),  -- 1 Dầu ăn
(3, 17, 2, 25000, 50000),  -- 2 Nước mắm
(3, 18, 3, 30000, 90000),  -- 3 Hạt nêm
(3, 19, 1, 120000, 60000); -- 1 Dầu gội (giảm còn 60k)

-- 6. THANH TOÁN (cho 2 đơn đã thanh toán)
INSERT INTO thanh_toan (don_hang_id, so_tien, phuong_thuc, trang_thai, ma_giao_dich) VALUES
(1, 135000, 'TIEN_MAT', 'THANH_CONG', 'TT20241228001'),
(2, 89000, 'CHUYEN_KHOAN', 'THANH_CONG', 'CK20241228002VCB');

-- ============================================
-- KIỂM TRA DỮ LIỆU
-- ============================================
SELECT 'Đã tạo database thành công!' AS status;
SELECT COUNT(*) AS so_danh_muc FROM danh_muc;
SELECT COUNT(*) AS so_san_pham FROM san_pham;
SELECT COUNT(*) AS so_khach_hang FROM khach_hang;
SELECT COUNT(*) AS so_don_hang FROM don_hang;
SELECT COUNT(*) AS so_chi_tiet FROM chi_tiet_don_hang;
SELECT COUNT(*) AS so_thanh_toan FROM thanh_toan;

-- Hiển thị thông tin đăng nhập
SELECT 'THÔNG TIN ĐĂNG NHẬP' AS info;
SELECT ho_ten, email, mat_khau, role FROM khach_hang ORDER BY role DESC;

-- ============================================
-- HOÀN TẤT!
-- ============================================