

## Permisstion 
* 'SUPER_ADMIN',
* 'ADMIN',
* 'STAFF',
* 'CUSTOMER'
## Data seed (Mẫu)
```sql
INSERT INTO roles (name, description, is_system) VALUES
('SUPER_ADMIN', 'Toàn quyền hệ thống',     TRUE),
('ADMIN',       'Quản trị shop',             TRUE),
('STAFF',       'Nhân viên kho/CSKH',        TRUE),
('CUSTOMER',    'Khách hàng thông thường',   TRUE);

INSERT INTO permissions (resource, action, description) VALUES
('PRODUCT',  'CREATE',  'Thêm sản phẩm mới'),
('PRODUCT',  'READ',    'Xem sản phẩm'),
('PRODUCT',  'UPDATE',  'Sửa sản phẩm'),
('PRODUCT',  'DELETE',  'Xóa sản phẩm'),
('ORDER',    'READ',    'Xem đơn hàng'),
('ORDER',    'UPDATE',  'Cập nhật trạng thái đơn'),
('ORDER',    'DELETE',  'Hủy đơn hàng'),
('USER',     'MANAGE',  'Quản lý tài khoản người dùng'),
('COUPON',   'MANAGE',  'Quản lý mã giảm giá'),
('REPORT',   'READ',    'Xem báo cáo doanh thu');

-- ADMIN: quản lý sản phẩm + đơn hàng + coupon + report (trừ MANAGE USER)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'ADMIN'
  AND (p.resource, p.action) IN (
    ('PRODUCT','CREATE'),('PRODUCT','READ'),('PRODUCT','UPDATE'),('PRODUCT','DELETE'),
    ('ORDER','READ'),('ORDER','UPDATE'),('ORDER','DELETE'),
    ('COUPON','MANAGE'),('REPORT','READ')
  );

-- STAFF: xem & cập nhật đơn hàng, xem sản phẩm
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'STAFF'
  AND (p.resource, p.action) IN (
    ('PRODUCT','READ'),('ORDER','READ'),('ORDER','UPDATE')
  );

-- CUSTOMER: chỉ xem sản phẩm
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'CUSTOMER' AND p.resource = 'PRODUCT' AND p.action = 'READ';
```

