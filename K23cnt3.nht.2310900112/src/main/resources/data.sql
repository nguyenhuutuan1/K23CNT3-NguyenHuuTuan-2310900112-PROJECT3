-- ============================================
-- DATA.SQL - SAMPLE DATA FOR GROCERY STORE
-- ============================================
-- This file contains sample data for development and testing
-- Run after schema creation

-- ============================================
-- 1. DISABLE FOREIGN KEY CHECKS
-- ============================================
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 2. CLEAR EXISTING DATA (Optional)
-- ============================================
-- TRUNCATE TABLE chitiethoadon;
-- TRUNCATE TABLE hoadon;
-- TRUNCATE TABLE sanpham;
-- TRUNCATE TABLE loaisanpham;
-- TRUNCATE TABLE nhacungcap;
-- TRUNCATE TABLE khachhang;
-- TRUNCATE TABLE nhanvien;

-- ============================================
-- 3. NHÀ CUNG CẤP (SUPPLIERS)
-- ============================================
INSERT INTO nhacungcap (MaNCC, TenNCC, DiaChi, DienThoai, Email, MaSoThue, NguoiLienHe, TrangThai, NgayTao) VALUES
(1, 'Công ty TNHH Thực phẩm ABC', '123 Lê Lợi, Quận 1, TP.HCM', '02838234567', 'contact@abcfood.com.vn', '0300123456', 'Nguyễn Văn A', 'DANG_HOP_TAC', '2023-01-15 08:30:00'),
(2, 'Công ty CP Thực phẩm XYZ', '456 Nguyễn Huệ, Quận 1, TP.HCM', '02838237890', 'info@xyzfood.com', '0300123457', 'Trần Thị B', 'DANG_HOP_TAC', '2023-02-20 09:15:00'),
(3, 'Công ty TNHH VinaMilk', '789 Trần Hưng Đạo, Quận 5, TP.HCM', '02838231122', 'sales@vinamilk.com', '0300123458', 'Lê Văn C', 'DANG_HOP_TAC', '2023-03-10 10:00:00'),
(4, 'Công ty CP Acecook Việt Nam', '321 Cách Mạng Tháng 8, Quận 3, TP.HCM', '02838233344', 'info@acecook.com.vn', '0300123459', 'Phạm Thị D', 'DANG_HOP_TAC', '2023-04-05 14:20:00'),
(5, 'Công ty TNHH Nestlé Việt Nam', '159 Pasteur, Quận 1, TP.HCM', '02838235566', 'contact@nestle.com.vn', '0300123460', 'Vũ Văn E', 'DANG_HOP_TAC', '2023-05-12 16:45:00'),
(6, 'Công ty TNHH Coca-Cola Việt Nam', '222 Nguyễn Văn Linh, Quận 7, TP.HCM', '02838237788', 'info@cocacola.com.vn', '0300123461', 'Hoàng Thị F', 'DANG_HOP_TAC', '2023-06-18 11:30:00'),
(7, 'Công ty CP Đường Biên Hòa', '111 Lý Thường Kiệt, Quận 10, TP.HCM', '02838239900', 'sales@duongbienhoa.com', '0300123462', 'Đặng Văn G', 'TAM_NGUNG', '2023-07-22 13:15:00'),
(8, 'Công ty TNHH Masan Consumer', '333 Hai Bà Trưng, Quận 3, TP.HCM', '02838231133', 'contact@masan.com.vn', '0300123463', 'Bùi Thị H', 'DANG_HOP_TAC', '2023-08-30 15:40:00');

-- ============================================
-- 4. LOẠI SẢN PHẨM (CATEGORIES)
-- ============================================
INSERT INTO loaisanpham (MaLoai, TenLoai, MoTa, Icon, ThuTuHienThi, TrangThai, NgayTao) VALUES
(1, 'Thực phẩm khô', 'Các loại thực phẩm khô: gạo, mì, bột, đường...', 'fas fa-wheat', 1, 'HOAT_DONG', '2023-01-10 08:00:00'),
(2, 'Đồ hộp', 'Các loại đồ hộp: cá hộp, thịt hộp, pate...', 'fas fa-can-food', 2, 'HOAT_DONG', '2023-01-10 08:00:00'),
(3, 'Gia vị', 'Các loại gia vị: nước mắm, dầu ăn, tương ớt...', 'fas fa-mortar-pestle', 3, 'HOAT_DONG', '2023-01-10 08:00:00'),
(4, 'Bánh kẹo', 'Các loại bánh kẹo, snack...', 'fas fa-cookie', 4, 'HOAT_DONG', '2023-01-10 08:00:00'),
(5, 'Đồ uống', 'Các loại đồ uống: nước suối, nước ngọt, trà...', 'fas fa-glass-whiskey', 5, 'HOAT_DONG', '2023-01-10 08:00:00'),
(6, 'Sữa và sản phẩm từ sữa', 'Sữa tươi, sữa đặc, sữa chua...', 'fas fa-wine-bottle', 6, 'HOAT_DONG', '2023-01-10 08:00:00'),
(7, 'Đồ đông lạnh', 'Các loại thực phẩm đông lạnh', 'fas fa-snowflake', 7, 'HOAT_DONG', '2023-01-10 08:00:00'),
(8, 'Rau củ quả tươi', 'Rau củ quả tươi theo mùa', 'fas fa-carrot', 8, 'HOAT_DONG', '2023-01-10 08:00:00'),
(9, 'Thịt cá tươi sống', 'Thịt, cá, hải sản tươi sống', 'fas fa-fish', 9, 'HOAT_DONG', '2023-01-10 08:00:00'),
(10, 'Đồ gia dụng', 'Các đồ dùng gia đình', 'fas fa-home', 10, 'HOAT_DONG', '2023-01-10 08:00:00');

-- ============================================
-- 5. SẢN PHẨM (PRODUCTS)
-- ============================================
INSERT INTO sanpham (MaSP, TenSP, MaLoai, MaNCC, DonViTinh, DonGia, SoLuong, HinhAnh, MoTa, TrangThai, NgayTao) VALUES
-- Thực phẩm khô (MaLoai = 1)
(1, 'Gạo Jasmine 5kg', 1, 1, 'Bao', 150000.00, 100, 'gao-jasmine.jpg', 'Gạo thơm ngon chất lượng cao, xuất xứ Thái Lan', 'DANG_BAN', '2024-01-01 09:00:00'),
(2, 'Mì tôm Hảo Hảo 30 gói', 1, 4, 'Thùng', 120000.00, 80, 'mi-tom-hao-hao.jpg', 'Mì tôm chua cay vị đặc biệt, 30 gói/thùng', 'DANG_BAN', '2024-01-02 10:30:00'),
(3, 'Bột ngọt Ajinomoto 500g', 1, 5, 'Gói', 35000.00, 150, 'bot-ngot-ajinomoto.jpg', 'Bột ngọt chất lượng cao, hàng nhập khẩu', 'DANG_BAN', '2024-01-03 11:45:00'),
(4, 'Đường trắng Biên Hòa 1kg', 1, 2, 'Gói', 22000.00, 120, 'duong-trang-bien-hoa.jpg', 'Đường tinh luyện, chất lượng cao', 'DANG_BAN', '2024-01-04 14:20:00'),
(5, 'Bột mì Đức Phong 1kg', 1, 1, 'Gói', 30000.00, 90, 'bot-mi-duc-phong.jpg', 'Bột mì đa dụng, làm bánh ngon', 'DANG_BAN', '2024-01-05 16:00:00'),

-- Đồ hộp (MaLoai = 2)
(6, 'Cá ngừ đại dương hộp 200g', 2, 2, 'Hộp', 45000.00, 75, 'ca-ngu-hop.jpg', 'Cá ngừ đại dương ngâm dầu, giàu dinh dưỡng', 'DANG_BAN', '2024-01-06 08:15:00'),
(7, 'Thịt heo hộp 250g', 2, 1, 'Hộp', 55000.00, 60, 'thit-heo-hop.jpg', 'Thịt heo hầm sẵn, tiện lợi', 'DANG_BAN', '2024-01-07 09:30:00'),
(8, 'Pate gan heo 170g', 2, 3, 'Hộp', 28000.00, 90, 'pate-gan-heo.jpg', 'Pate gan heo mịn, thơm ngon', 'DANG_BAN', '2024-01-08 10:45:00'),
(9, 'Cá mòi hộp 125g', 2, 6, 'Hộp', 32000.00, 110, 'ca-moi-hop.jpg', 'Cá mòi ngâm sốt cà chua', 'DANG_BAN', '2024-01-09 13:20:00'),

-- Gia vị (MaLoai = 3)
(10, 'Nước mắm Nam Ngư 500ml', 3, 2, 'Chai', 35000.00, 50, 'nuoc-mam-nam-ngu.jpg', 'Nước mắm đặc sản Phú Quốc, độ đạm cao', 'DANG_BAN', '2024-01-10 15:00:00'),
(11, 'Dầu ăn Neptune 1 lít', 3, 1, 'Chai', 42000.00, 70, 'dau-an-neptune.jpg', 'Dầu ăn tinh luyện, không cholesterol', 'DANG_BAN', '2024-01-11 16:30:00'),
(12, 'Tương ớt Chin-su 250g', 3, 4, 'Chai', 18000.00, 110, 'tuong-ot-chin-su.jpg', 'Tương ớt cay vừa, hương vị đặc trưng', 'DANG_BAN', '2024-01-12 17:45:00'),
(13, 'Hạt nêm Knorr 400g', 3, 5, 'Gói', 45000.00, 85, 'hat-nem-knorr.jpg', 'Hạt nêm từ thịt thật, không chất bảo quản', 'DANG_BAN', '2024-01-13 18:20:00'),

-- Bánh kẹo (MaLoai = 4)
(14, 'Bánh quy bơ Richy 200g', 4, 5, 'Gói', 28000.00, 60, 'banh-quy-boi-richy.jpg', 'Bánh quy bơ thơm ngon, giòn tan', 'DANG_BAN', '2024-01-14 09:15:00'),
(15, 'Kẹo dẻo Alpenliebe 150g', 4, 3, 'Gói', 15000.00, 200, 'keo-deo-alpenliebe.jpg', 'Kẹo dẻo vị trái cây, nhiều hương vị', 'DANG_BAN', '2024-01-15 10:30:00'),
(16, 'Snack khoai tây Oishi 60g', 4, 4, 'Gói', 10000.00, 180, 'snack-oishi.jpg', 'Snack khoai tây vị phô mai, giòn tan', 'DANG_BAN', '2024-01-16 11:45:00'),
(17, 'Bánh Oreo 137g', 4, 5, 'Gói', 32000.00, 95, 'banh-oreo.jpg', 'Bánh sandwich kem vị vani', 'DANG_BAN', '2024-01-17 14:00:00'),

-- Đồ uống (MaLoai = 5)
(18, 'Nước suối Lavie 500ml', 5, 5, 'Chai', 5000.00, 300, 'nuoc-suoi-lavie.jpg', 'Nước suối tinh khiết, đóng chai', 'DANG_BAN', '2024-01-18 15:30:00'),
(19, 'Coca Cola 1.5 lít', 5, 2, 'Chai', 22000.00, 150, 'coca-cola-1.5l.jpg', 'Nước ngọt có gas, chai 1.5 lít', 'DANG_BAN', '2024-01-19 16:45:00'),
(20, 'Trà xanh 0 độ 500ml', 5, 4, 'Chai', 12000.00, 120, 'tra-xanh-0-do.jpg', 'Trà xanh không đường, tốt cho sức khỏe', 'DANG_BAN', '2024-01-20 17:20:00'),
(21, 'Nước cam ép Twister 1 lít', 5, 6, 'Chai', 35000.00, 80, 'nuoc-cam-twister.jpg', 'Nước cam ép nguyên chất, bổ sung vitamin C', 'DANG_BAN', '2024-01-21 18:00:00'),

-- Sữa (MaLoai = 6)
(22, 'Sữa tươi Vinamilk 1 lít', 6, 3, 'Hộp', 32000.00, 90, 'sua-tuoi-vinamilk.jpg', 'Sữa tươi tiệt trùng, giàu dinh dưỡng', 'DANG_BAN', '2024-01-22 08:30:00'),
(23, 'Sữa đặc Ông Thọ 380g', 6, 3, 'Lon', 28000.00, 80, 'sua-dac-ong-tho.jpg', 'Sữa đặc có đường, thơm ngon', 'DANG_BAN', '2024-01-23 09:45:00'),
(24, 'Sữa chua Vinamilk 100g', 6, 3, 'Hộp', 5000.00, 250, 'sua-chua-vinamilk.jpg', 'Sữa chua ăn, có lợi cho tiêu hóa', 'DANG_BAN', '2024-01-24 10:20:00'),
(25, 'Sữa đậu nành Fami 1 lít', 6, 8, 'Hộp', 25000.00, 70, 'sua-dau-nanh-fami.jpg', 'Sữa đậu nành không đường, tốt cho sức khỏe', 'DANG_BAN', '2024-01-25 11:30:00'),

-- Đồ đông lạnh (MaLoai = 7)
(26, 'Chả giò đông lạnh 500g', 7, 1, 'Gói', 65000.00, 40, 'cha-gio-dong-lanh.jpg', 'Chả giò đã vo sẵn, tiện lợi', 'DANG_BAN', '2024-01-26 14:15:00'),
(27, 'Cá viên đông lạnh 500g', 7, 2, 'Gói', 55000.00, 55, 'ca-vien-dong-lanh.jpg', 'Cá viên dai ngon, chế biến nhanh', 'DANG_BAN', '2024-01-27 15:30:00'),

-- Rau củ quả (MaLoai = 8)
(28, 'Rau muống tươi 500g', 8, 1, 'Bó', 10000.00, 60, 'rau-muong-tuoi.jpg', 'Rau muống tươi, sạch, không thuốc trừ sâu', 'DANG_BAN', '2024-01-28 16:45:00'),
(29, 'Cà chua Đà Lạt 500g', 8, 2, 'Kg', 25000.00, 45, 'ca-chua-da-lat.jpg', 'Cà chua Đà Lạt tươi ngon', 'DANG_BAN', '2024-01-29 17:20:00'),

-- Thịt cá (MaLoai = 9)
(30, 'Thịt heo ba chỉ 500g', 9, 1, 'Kg', 120000.00, 30, 'thit-heo-ba-chi.jpg', 'Thịt heo ba chỉ tươi ngon', 'DANG_BAN', '2024-01-30 18:00:00');

-- ============================================
-- 6. KHÁCH HÀNG (CUSTOMERS)
-- ============================================
INSERT INTO khachhang (MaKH, HoTen, NgaySinh, GioiTinh, DiaChi, DienThoai, Email, DiemTichLuy, TrangThai, NgayDangKy) VALUES
(1, 'Nguyễn Văn An', '1990-05-15', 'Nam', '789 Lý Thường Kiệt, Quận 10, TP.HCM', '0903123456', 'an.nguyen@email.com', 1250, 'HOAT_DONG', '2023-11-01 09:00:00'),
(2, 'Trần Thị Bình', '1985-08-20', 'Nữ', '321 Cách Mạng Tháng 8, Quận 3, TP.HCM', '0903987654', 'binh.tran@email.com', 850, 'HOAT_DONG', '2023-11-05 10:30:00'),
(3, 'Lê Hoàng Cường', '1992-11-30', 'Nam', '456 Nguyễn Văn Cừ, Quận 5, TP.HCM', '0905111222', 'cuong.le@email.com', 2100, 'HOAT_DONG', '2023-11-10 14:15:00'),
(4, 'Phạm Thị Dung', '1995-07-22', 'Nữ', '159 Trần Hưng Đạo, Quận 1, TP.HCM', '0905333444', 'dung.pham@email.com', 450, 'HOAT_DONG', '2023-11-15 16:45:00'),
(5, 'Vũ Minh Đức', '1988-03-10', 'Nam', '753 Lê Văn Sỹ, Quận Phú Nhuận, TP.HCM', '0905555666', 'duc.vu@email.com', 3200, 'HOAT_DONG', '2023-11-20 11:20:00'),
(6, 'Hoàng Thị Hương', '1993-12-05', 'Nữ', '246 Nguyễn Thị Minh Khai, Quận 1, TP.HCM', '0905777888', 'huong.hoang@email.com', 980, 'HOAT_DONG', '2023-11-25 13:40:00'),
(7, 'Đặng Văn Hùng', '1991-09-18', 'Nam', '111 Nguyễn Thái Học, Quận 1, TP.HCM', '0905999000', 'hung.dang@email.com', 150, 'HOAT_DONG', '2023-12-01 15:30:00'),
(8, 'Bùi Thị Lan', '1994-04-25', 'Nữ', '222 Lê Văn Sỹ, Quận 3, TP.HCM', '0906111222', 'lan.bui@email.com', 0, 'HOAT_DONG', '2023-12-05 17:00:00');

-- ============================================
-- 7. NHÂN VIÊN (EMPLOYEES)
-- ============================================
INSERT INTO nhanvien (MaNV, HoTen, NgaySinh, GioiTinh, DiaChi, DienThoai, Email, ChucVu, Luong, TrangThai, NgayBatDau) VALUES
(1, 'Lê Văn Cường', '1992-03-10', 'Nam', '159 Pasteur, Quận 1, TP.HCM', '0905111222', 'cuong.le@taphoa.com', 'QUAN_LY', 15000000.00, 'DANG_LAM', '2022-01-15'),
(2, 'Phạm Thị Dung', '1995-12-25', 'Nữ', '753 Nguyễn Trãi, Quận 5, TP.HCM', '0905333444', 'dung.pham@taphoa.com', 'NHAN_VIEN_BAN_HANG', 8500000.00, 'DANG_LAM', '2022-03-20'),
(3, 'Trần Văn Hải', '1990-08-15', 'Nam', '456 Lê Văn Việt, Quận 9, TP.HCM', '0905444555', 'hai.tran@taphoa.com', 'NHAN_VIEN_KHO', 7500000.00, 'DANG_LAM', '2022-06-10'),
(4, 'Nguyễn Thị Lan', '1994-06-18', 'Nữ', '321 Nguyễn Văn Linh, Quận 7, TP.HCM', '0905666777', 'lan.nguyen@taphoa.com', 'NHAN_VIEN_THU_NGAN', 9000000.00, 'DANG_LAM', '2022-09-05'),
(5, 'Hoàng Văn Minh', '1993-11-30', 'Nam', '789 Nguyễn Văn Trỗi, Quận Phú Nhuận, TP.HCM', '0905888999', 'minh.hoang@taphoa.com', 'NHAN_VIEN_GIAO_HANG', 8000000.00, 'DANG_LAM', '2023-01-10'),
(6, 'Vũ Thị Nga', '1996-02-14', 'Nữ', '147 Lý Chính Thắng, Quận 3, TP.HCM', '0906000111', 'nga.vu@taphoa.com', 'NHAN_VIEN_BAN_HANG', 8200000.00, 'THU_VIEC', '2023-05-15');

-- ============================================
-- 8. HÓA ĐƠN (ORDERS)
-- ============================================
INSERT INTO hoadon (MaHD, MaKH, MaNV, NgayLapHD, TongTien, TrangThai, GhiChu, PhuongThucThanhToan) VALUES
(1, 1, 2, '2024-01-15 10:30:00', 225000.00, 'DA_GIAO', 'Giao hàng trước 17h', 'TIEN_MAT'),
(2, 2, 4, '2024-01-16 14:45:00', 150000.00, 'DA_GIAO', 'Không giao hàng buổi trưa', 'CHUYEN_KHOAN'),
(3, 3, 2, '2024-01-17 09:15:00', 320000.00, 'DANG_GIAO', 'Gọi điện trước khi giao', 'TIEN_MAT'),
(4, 1, 4, '2024-01-18 16:20:00', 185000.00, 'DA_XAC_NHAN', 'Để hàng tại cổng', 'TIEN_MAT'),
(5, 5, 2, '2024-01-19 11:30:00', 450000.00, 'CHO_XU_LY', 'Mua sỉ cho công ty', 'CHUYEN_KHOAN'),
(6, 6, 4, '2024-01-20 15:45:00', 120000.00, 'DA_HUY', 'Khách hàng hủy đơn', 'TIEN_MAT'),
(7, 7, 2, '2024-01-21 13:20:00', 275000.00, 'DA_GIAO', NULL, 'TIEN_MAT'),
(8, 3, 4, '2024-01-22 10:00:00', 189000.00, 'DA_XAC_NHAN', 'Giao hàng sáng thứ 2', 'CHUYEN_KHOAN');

-- ============================================
-- 9. CHI TIẾT HÓA ĐƠN (ORDER DETAILS)
-- ============================================
INSERT INTO chitiethoadon (MaCTHD, MaHD, MaSP, SoLuong, DonGia, ThanhTien) VALUES
-- Hóa đơn 1
(1, 1, 1, 1, 150000.00, 150000.00),
(2, 1, 10, 2, 35000.00, 70000.00),
(3, 1, 18, 1, 5000.00, 5000.00),

-- Hóa đơn 2
(4, 2, 2, 1, 120000.00, 120000.00),
(5, 2, 15, 2, 15000.00, 30000.00),

-- Hóa đơn 3
(6, 3, 1, 2, 150000.00, 300000.00),
(7, 3, 20, 1, 12000.00, 12000.00),
(8, 3, 24, 2, 5000.00, 10000.00),

-- Hóa đơn 4
(9, 4, 11, 1, 42000.00, 42000.00),
(10, 4, 12, 3, 18000.00, 54000.00),
(11, 4, 22, 2, 32000.00, 64000.00),
(12, 4, 18, 5, 5000.00, 25000.00),

-- Hóa đơn 5
(13, 5, 1, 2, 150000.00, 300000.00),
(14, 5, 19, 3, 22000.00, 66000.00),
(15, 5, 23, 2, 28000.00, 56000.00),
(16, 5, 28, 3, 10000.00, 30000.00),

-- Hóa đơn 6
(17, 6, 16, 5, 10000.00, 50000.00),
(18, 6, 18, 10, 5000.00, 50000.00),
(19, 6, 20, 2, 12000.00, 24000.00),

-- Hóa đơn 7
(20, 7, 3, 3, 35000.00, 105000.00),
(21, 7, 6, 2, 45000.00, 90000.00),
(22, 7, 14, 2, 28000.00, 56000.00),
(23, 7, 26, 1, 65000.00, 65000.00),

-- Hóa đơn 8
(24, 8, 4, 2, 22000.00, 44000.00),
(25, 8, 8, 3, 28000.00, 84000.00),
(26, 8, 13, 1, 45000.00, 45000.00),
(27, 8, 17, 2, 32000.00, 64000.00);

-- ============================================
-- 10. CẬP NHẬT TỔNG TIỀN HÓA ĐƠN (Tính toán lại)
-- ============================================
-- Thực tế Spring JPA sẽ tự tính, nhưng đây là backup
UPDATE hoadon h
JOIN (
    SELECT MaHD, SUM(ThanhTien) as Tong
    FROM chitiethoadon
    GROUP BY MaHD
) c ON h.MaHD = c.MaHD
SET h.TongTien = c.Tong;

-- ============================================
-- 11. CẬP NHẬT ĐIỂM TÍCH LŨY KHÁCH HÀNG
-- ============================================
-- Mỗi 10,000 VNĐ = 1 điểm
UPDATE khachhang k
JOIN (
    SELECT MaKH, SUM(TongTien) as TongMua
    FROM hoadon
    WHERE TrangThai IN ('DA_GIAO', 'DA_XAC_NHAN', 'DANG_GIAO')
    GROUP BY MaKH
) h ON k.MaKH = h.MaKH
SET k.DiemTichLuy = FLOOR(h.TongMua / 10000);

-- ============================================
-- 12. THÊM DỮ LIỆU CHO TESTING (Optional)
-- ============================================
-- Thêm khách hàng test
INSERT INTO khachhang (HoTen, NgaySinh, GioiTinh, DiaChi, DienThoai, Email, DiemTichLuy, TrangThai, NgayDangKy) VALUES
('Test Customer 1', '2000-01-01', 'Nam', '123 Test Street', '0900000001', 'test1@email.com', 0, 'HOAT_DONG', NOW()),
('Test Customer 2', '2000-02-02', 'Nữ', '456 Test Avenue', '0900000002', 'test2@email.com', 0, 'HOAT_DONG', NOW());

-- Thêm sản phẩm test
INSERT INTO sanpham (TenSP, MaLoai, MaNCC, DonViTinh, DonGia, SoLuong, MoTa, TrangThai, NgayTao) VALUES
('Sản phẩm Test 1', 1, 1, 'Cái', 10000.00, 50, 'Sản phẩm dùng để test', 'DANG_BAN', NOW()),
('Sản phẩm Test 2', 2, 2, 'Hộp', 20000.00, 30, 'Sản phẩm test số 2', 'DANG_BAN', NOW());

-- ============================================
-- 13. TẠO VIEWS CHO BÁO CÁO (Optional)
-- ============================================
-- View tổng hợp doanh thu theo tháng
CREATE OR REPLACE VIEW v_doanhthu_thang AS
SELECT
    YEAR(NgayLapHD) as Nam,
    MONTH(NgayLapHD) as Thang,
    COUNT(*) as SoDonHang,
    SUM(TongTien) as TongDoanhThu,
    AVG(TongTien) as TrungBinhDon
FROM hoadon
WHERE TrangThai IN ('DA_GIAO', 'DA_XAC_NHAN')
GROUP BY YEAR(NgayLapHD), MONTH(NgayLapHD);

-- View top sản phẩm bán chạy
CREATE OR REPLACE VIEW v_top_sanpham AS
SELECT
    s.MaSP,
    s.TenSP,
    l.TenLoai,
    COUNT(ct.MaSP) as SoLanBan,
    SUM(ct.SoLuong) as TongSoLuongBan,
    SUM(ct.ThanhTien) as TongDoanhThu
FROM sanpham s
LEFT JOIN chitiethoadon ct ON s.MaSP = ct.MaSP
LEFT JOIN loaisanpham l ON s.MaLoai = l.MaLoai
GROUP BY s.MaSP
ORDER BY TongSoLuongBan DESC;

-- View khách hàng thân thiết
CREATE OR REPLACE VIEW v_khachhang_thanthiet AS
SELECT
    k.MaKH,
    k.HoTen,
    k.DienThoai,
    k.Email,
    COUNT(h.MaHD) as SoDonHang,
    SUM(h.TongTien) as TongChiTieu,
    MAX(h.NgayLapHD) as LanMuaCuoi
FROM khachhang k
LEFT JOIN hoadon h ON k.MaKH = h.MaKH
WHERE h.TrangThai IN ('DA_GIAO', 'DA_XAC_NHAN', 'DANG_GIAO')
GROUP BY k.MaKH
ORDER BY TongChiTieu DESC;

-- ============================================
-- 14. ENABLE FOREIGN KEY CHECKS
-- ============================================
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 15. LOG SUCCESS MESSAGE
-- ============================================
SELECT '=== DATA.SQL EXECUTED SUCCESSFULLY ===' as Message;
SELECT 'Total Suppliers: ' || COUNT(*) FROM nhacungcap;
SELECT 'Total Categories: ' || COUNT(*) FROM loaisanpham;
SELECT 'Total Products: ' || COUNT(*) FROM sanpham;
SELECT 'Total Customers: ' || COUNT(*) FROM khachhang;
SELECT 'Total Employees: ' || COUNT(*) FROM nhanvien;
SELECT 'Total Orders: ' || COUNT(*) FROM hoadon;
SELECT '=== READY FOR DEVELOPMENT ===' as Message;