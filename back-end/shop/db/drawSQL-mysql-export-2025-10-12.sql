-- Bảng người dùng
CREATE TABLE `users` (
    `id` CHAR(36) NOT NULL,
    `email` VARCHAR(160) NOT NULL,
    `user_name` VARCHAR(160),
    `password` VARCHAR(160) NOT NULL,
    `date_of_birth` VARCHAR(255),
    `address` VARCHAR(160),
    `phone` VARCHAR(20),
    `avatar` VARCHAR(1000),
    `is_active` TINYINT DEFAULT 1,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `users_email_unique` (`email`)
);

-- Bảng vai trò
CREATE TABLE `roles` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `role_name` VARCHAR(50) NOT NULL
);

-- Bảng gán quyền người dùng
CREATE TABLE `user_roles` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` CHAR(36) NOT NULL,
    `role_id` INT UNSIGNED NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`) ON DELETE CASCADE
);

-- Danh mục sản phẩm
CREATE TABLE `categories` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `category_name` VARCHAR(100) NOT NULL
);

-- Thương hiệu
CREATE TABLE `brands` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `brand_name` VARCHAR(50) NOT NULL
);

-- Sản phẩm
CREATE TABLE `products` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_name` VARCHAR(160) NOT NULL,
    `description` TEXT,
    `category_id` INT,
    `brand_id` INT UNSIGNED,
    `price` DECIMAL(10, 2),
    `price_before_discount` DECIMAL(10, 2),
    `quantity` INT,
    `sold` INT DEFAULT 0,
    `view` INT DEFAULT 0,
    `rating` DECIMAL(3, 2) DEFAULT 0.0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`category_id`) REFERENCES `categories`(`id`),
    FOREIGN KEY (`brand_id`) REFERENCES `brands`(`id`)
);

-- Chi tiết sản phẩm (mỗi sản phẩm có thể có nhiều biến thể)
CREATE TABLE `product_details` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` INT NOT NULL,
    `color_id` INT,
    `size_id` INT,
    `stock_quantity` INT DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
);

-- Ảnh sản phẩm
CREATE TABLE `images` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` INT NOT NULL,
    `url` VARCHAR(1000) NOT NULL,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
);

-- Kích thước
CREATE TABLE `sizes` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `size_value` INT NOT NULL UNIQUE
);

-- Màu sắc
CREATE TABLE `colors` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `color_name` VARCHAR(50) NOT NULL UNIQUE
);

-- Liên kết nhiều-nhiều giữa sản phẩm và size
CREATE TABLE `product_sizes` (
    `product_id` INT NOT NULL,
    `size_id` INT NOT NULL,
    PRIMARY KEY (`product_id`, `size_id`),
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`size_id`) REFERENCES `sizes`(`id`) ON DELETE CASCADE
);

-- Liên kết nhiều-nhiều giữa sản phẩm và màu sắc
CREATE TABLE `product_colors` (
    `product_id` INT NOT NULL,
    `color_id` INT NOT NULL,
    PRIMARY KEY (`product_id`, `color_id`),
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`color_id`) REFERENCES `colors`(`id`) ON DELETE CASCADE
);

-- Đơn hàng
CREATE TABLE `orders` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` CHAR(36) NOT NULL,
    `status` TINYINT DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
);

-- Chi tiết đơn hàng
CREATE TABLE `order_details` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `product_id` INT NOT NULL,
    `price` DECIMAL(10, 2),
    `quantity` INT DEFAULT 1,
    `total_money` DECIMAL(10, 2),
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
);
