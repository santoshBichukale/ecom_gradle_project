-- Insert Users with roles USER and ADMIN
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$NU.WIZU4zwwLkLKbhgnFdurZE6A7YuUc6I4FNwPuColAGQB6pYBP6', 'ADMIN'),
('user1', '$2a$10$NU.WIZU4zwwLkLKbhgnFdurZE6A7YuUc6I4FNwPuColAGQB6pYBP6', 'USER'),
('user2', '$2a$10$NU.WIZU4zwwLkLKbhgnFdurZE6A7YuUc6I4FNwPuColAGQB6pYBP6', 'USER'),
('user3', '$2a$10$NU.WIZU4zwwLkLKbhgnFdurZE6A7YuUc6I4FNwPuColAGQB6pYBP6', 'USER');

-- Insert Categories
INSERT INTO category (name) VALUES
('Electronics'),
('Clothing'),
('Food'),
('Books');

-- Insert Products
INSERT INTO product (name, description, price, quantity, category_id) VALUES
('Smartphone', 'Latest model smartphone', 699.99, 50, 1),
('Laptop', 'High-performance laptop', 1200.00, 30, 1),
('T-shirt', 'Cotton T-shirt', 19.99, 100, 2),
('Novel', 'Bestselling novel', 14.99, 200, 4);

-- Insert Orders
INSERT INTO orders (user_id, product_id, order_quantity, total_price, order_date) VALUES
(2, 1, 1, 699.99, '2025-03-19'),
(3, 3, 2, 39.98, '2025-03-18'),
(2, 4, 1, 14.99, '2025-03-17');
