                     -- Drop tables if they already exist (to avoid conflicts during re-run)
                     DROP TABLE IF EXISTS orders;
                     DROP TABLE IF EXISTS product;
                     DROP TABLE IF EXISTS category;
                     DROP TABLE IF EXISTS users;

                     -- Create the users table
                     CREATE TABLE users (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(50) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         role VARCHAR(20) NOT NULL
                     );

                     -- Create the category table
                     CREATE TABLE category (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL UNIQUE
                     );

                     -- Create the product table
                     CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description VARCHAR(255),
                         price DOUBLE NOT NULL,
                         quantity INT NOT NULL,
                         category_id BIGINT,
                         FOREIGN KEY (category_id) REFERENCES category(id)
                     );

                     -- Create the orders table
                     CREATE TABLE orders (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         product_id BIGINT NOT NULL,
                         order_quantity INT NOT NULL,
                         totalprice DOUBLE NOT NULL,
                         order_date DATE NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES users(id),
                         FOREIGN KEY (product_id) REFERENCES product(id)
                     );
