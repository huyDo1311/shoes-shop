CREATE TABLE `users`(
    `id` CHAR(36) NOT NULL,
    `email` VARCHAR(160) NOT NULL,
    `userName` VARCHAR(160) NULL,
    `password` VARCHAR(160) NOT NULL,
    `dateOfBirth` DATE NULL,
    `address` VARCHAR(160) NULL,
    `phone` VARCHAR(20) NULL,
    `avatar` VARCHAR(1000) NULL,
    PRIMARY KEY(`id`)
);
ALTER TABLE
    `users` ADD UNIQUE `users_email_unique`(`email`);
CREATE TABLE `categories`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `categoryName` VARCHAR(100) NOT NULL
);
CREATE TABLE `products`(
    `id` CHAR(36) NOT NULL,
    `productName` VARCHAR(160) NOT NULL,
    `description` TEXT NULL,
    `categoryId` INT NULL,
    `image` VARCHAR(1000) NULL,
    `price` DECIMAL(10, 2) NULL,
    `priceBeforeDiscount` DECIMAL(10, 2) NULL,
    `quantity` INT NULL,
    `sold` INT NULL,
    `view` INT NULL,
    `rating` DECIMAL(3, 2) NULL,
    PRIMARY KEY(`id`)
);
CREATE TABLE `images`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `productId` INT NULL,
    `url` VARCHAR(1000) NOT NULL
);
CREATE TABLE `sizes`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `sizeValue` INT NOT NULL
);
ALTER TABLE
    `sizes` ADD UNIQUE `sizes_sizevalue_unique`(`sizeValue`);
CREATE TABLE `product_sizes`(
    `productId` INT NOT NULL,
    `sizeId` INT NOT NULL,
    PRIMARY KEY(`productId`)
);
ALTER TABLE
    `product_sizes` ADD PRIMARY KEY(`sizeId`);
CREATE TABLE `colors`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `colorName` VARCHAR(50) NOT NULL
);
ALTER TABLE
    `colors` ADD UNIQUE `colors_colorname_unique`(`colorName`);
CREATE TABLE `product_colors`(
    `productId` INT NOT NULL,
    `colorId` INT NOT NULL,
    PRIMARY KEY(`productId`)
);
ALTER TABLE
    `product_colors` ADD PRIMARY KEY(`colorId`);
CREATE TABLE `purchases`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `userId` BIGINT NULL,
    `productId` INT NULL,
    `buyCount` INT NULL DEFAULT '1',
    `price` DECIMAL(10, 2) NULL,
    `priceBeforeDiscount` DECIMAL(10, 2) NULL,
    `status` TINYINT NULL
);
CREATE TABLE `brand`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `brandName` VARCHAR(50) NOT NULL
);
CREATE TABLE `roles`(
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `roleName` VARCHAR(50) NOT NULL
);
ALTER TABLE
    `products` ADD CONSTRAINT `products_id_foreign` FOREIGN KEY(`id`) REFERENCES `product_sizes`(`productId`);
ALTER TABLE
    `users` ADD CONSTRAINT `users_username_foreign` FOREIGN KEY(`userName`) REFERENCES `roles`(`id`);
ALTER TABLE
    `products` ADD CONSTRAINT `products_view_foreign` FOREIGN KEY(`view`) REFERENCES `brand`(`id`);
ALTER TABLE
    `products` ADD CONSTRAINT `products_categoryid_foreign` FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`);
ALTER TABLE
    `sizes` ADD CONSTRAINT `sizes_id_foreign` FOREIGN KEY(`id`) REFERENCES `product_sizes`(`sizeId`);
ALTER TABLE
    `colors` ADD CONSTRAINT `colors_id_foreign` FOREIGN KEY(`id`) REFERENCES `product_colors`(`colorId`);
ALTER TABLE
    `products` ADD CONSTRAINT `products_id_foreign` FOREIGN KEY(`id`) REFERENCES `product_colors`(`productId`);
ALTER TABLE
    `images` ADD CONSTRAINT `images_productid_foreign` FOREIGN KEY(`productId`) REFERENCES `products`(`id`);
ALTER TABLE
    `purchases` ADD CONSTRAINT `purchases_productid_foreign` FOREIGN KEY(`productId`) REFERENCES `products`(`id`);
ALTER TABLE
    `purchases` ADD CONSTRAINT `purchases_userid_foreign` FOREIGN KEY(`userId`) REFERENCES `users`(`id`);