package com.cybersoft.shop.seed;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductImagePool {

    private final Random random = new Random();

    private final Map<Integer, List<ImageSet>> pool = Map.of(
            1, List.of( // Thể thao
                    new ImageSet("https://res.cloudinary.com/dxsq3ivxs/image/upload/v1765696502/shoes_shop/1765696491294-sneaker-1.jpg", "https://res.cloudinary.com/dxsq3ivxs/image/upload/v1765696505/shoes_shop/1765696503517-sneaker-2.jpg", "https://res.cloudinary.com/dxsq3ivxs/image/upload/v1765696509/shoes_shop/1765696506578-sneaker-3.jpg  ")
            ),
            2, List.of( // Chạy bộ
                    new ImageSet("https://res.cloudinary.com/dxsq3ivxs/image/upload/v1764835155/shoes_shop/1764835141440-chaybo-1.jpg", "https://res.cloudinary.com/dxsq3ivxs/image/upload/v1764835158/shoes_shop/1764835155740-chaybo-2.jpg", "https://res.cloudinary.com/dxsq3ivxs/image/upload/v1764835161/shoes_shop/1764835159082-chaybo-3.jpg")
            ),
            3, List.of( // Leo núi
                    new ImageSet("https://res.cloudinary.com/dxsq3ivxs/image/upload/v1765696404/shoes_shop/1765696401942-leonui-1.jpg", "https://res.cloudinary.com/dxsq3ivxs/image/upload/v1765696408/shoes_shop/1765696406187-leonui-2.jpg", "https://res.cloudinary.com/dxsq3ivxs/image/upload/v1765696411/shoes_shop/1765696409165-leonui-3.jpg")
            ),
            4, List.of( // Giày Tây
                    new ImageSet("https://res.cloudinary.com/dxsq3ivxs/image/upload/v1765696394/shoes_shop/1765696384567-giaytay-1.jpg", "https://res.cloudinary.com/dxsq3ivxs/image/upload/v1765696397/shoes_shop/1765696396026-giaytay-2.jpg", "https://res.cloudinary.com/dxsq3ivxs/image/upload/v1765696400/shoes_shop/1765696398982-giaytay-3.jpg")
            ),
            5, List.of( // Dép
                    new ImageSet("https://res.cloudinary.com/dxsq3ivxs/image/upload/v1764835164/shoes_shop/1764835161671-dep-1.jpg", "https://res.cloudinary.com/dxsq3ivxs/image/upload/v1764835168/shoes_shop/1764835164958-dep-2.jpg", "https://res.cloudinary.com/dxsq3ivxs/image/upload/v1764835171/shoes_shop/1764835168575-dep-3.jpg")
            )
    );

    public ImageSet randomByCategory(int categoryId) {
        var sets = pool.get(categoryId);
        if (sets == null || sets.isEmpty()) {
            throw new RuntimeException("No image set for categoryId=" + categoryId);
        }
        return sets.get(random.nextInt(sets.size()));
    }
}
