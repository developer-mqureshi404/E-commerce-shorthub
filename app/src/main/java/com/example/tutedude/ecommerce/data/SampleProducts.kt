package com.example.tutedude.ecommerce.data

object SampleProducts {
    val products = listOf(
        SampleProduct(
            title = "iPhone 14 Pro Max",
            description = "Latest Apple smartphone with A16 Bionic chip, 6.7-inch Super Retina XDR display, and advanced camera system.",
            price = 1099.99,
            category = "Electronics",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1678652197831-2d180705cd2c?w=500"
            )
        ),
        SampleProduct(
            title = "Samsung Galaxy S23 Ultra",
            description = "Flagship Android phone with 200MP camera, S Pen stylus, and powerful Snapdragon 8 Gen 2 processor.",
            price = 1199.99,
            category = "Electronics",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=500"
            )
        ),
        SampleProduct(
            title = "Nike Air Max 270",
            description = "Comfortable running shoes with Max Air cushioning and breathable mesh upper. Available in multiple colors.",
            price = 149.99,
            category = "Fashion",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500"
            )
        ),
        SampleProduct(
            title = "Levi's 501 Jeans",
            description = "Classic straight-fit denim jeans. The original blue jean since 1873. 100% cotton.",
            price = 89.99,
            category = "Fashion",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1542272604-787c3835535d?w=500"
            )
        ),
        SampleProduct(
            title = "Instant Pot Duo",
            description = "7-in-1 electric pressure cooker. Pressure cook, slow cook, rice cooker, steamer, and more.",
            price = 89.99,
            category = "Home & Kitchen",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1585515320310-259814833e62?w=500"
            )
        ),
        SampleProduct(
            title = "Dyson V15 Vacuum",
            description = "Cordless stick vacuum with laser dust detection and intelligent suction adjustment.",
            price = 649.99,
            category = "Home & Kitchen",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=500"
            )
        ),
        SampleProduct(
            title = "Wilson Basketball",
            description = "Official size basketball with superior grip and durability. Perfect for indoor and outdoor play.",
            price = 29.99,
            category = "Sports",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1546519638-68e109498ffc?w=500"
            )
        ),
        SampleProduct(
            title = "Yoga Mat Premium",
            description = "Non-slip exercise mat with extra cushioning. 6mm thick, eco-friendly material.",
            price = 39.99,
            category = "Sports",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=500"
            )
        ),
        SampleProduct(
            title = "The Midnight Library",
            description = "Bestselling novel by Matt Haig. A dazzling novel about all the choices that go into a life well lived.",
            price = 16.99,
            category = "Books",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500"
            )
        ),
        SampleProduct(
            title = "LEGO Star Wars Set",
            description = "Build the iconic Millennium Falcon with 1,351 pieces. Perfect for ages 9+.",
            price = 159.99,
            category = "Toys",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=500"
            )
        )
    )
}

data class SampleProduct(
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrls: List<String>
)