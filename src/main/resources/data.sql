
-- Insert sample data into the users table
INSERT INTO users (id, username, password, role) VALUES
(1, 'user', '$2a$10$ok.gvyOiSFsGBbE6u45v8.bTXGlkjKu7RzsPaEgHwXVah1SKE8B9e', 'USER'),
(2, 'admin', '$2a$10$ok.gvyOiSFsGBbE6u45v8.bTXGlkjKu7RzsPaEgHwXVah1SKE8B9e', 'ADMIN');

-- Insert sample data into the category table
INSERT INTO category (id, name) VALUES
(1, 'Electronics'),
(2, 'Books'),
(3, 'Clothing');

-- Insert sample data into the product table
INSERT INTO product (id, name, description, price, quantity, category_id) VALUES
(1, 'Smartphone', 'Latest 5G smartphone', 699.99, 50, 1),
(2, 'Java Programming Book', 'Comprehensive guide to Java', 39.99, 100, 2),
(3, 'T-shirt', 'Cotton T-shirt', 19.99, 200, 3);

-- Insert sample data into the orders table
INSERT INTO orders (id, user_id, product_id, order_quantity, totalprice, order_date) VALUES
(1, 1, 1, 2, 1399.98, '2024-03-19'),
(2, 2, 2, 1, 39.99, '2024-03-18');
