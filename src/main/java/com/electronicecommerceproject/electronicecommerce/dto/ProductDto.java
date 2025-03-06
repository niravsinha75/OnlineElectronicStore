package com.electronicecommerceproject.electronicecommerce.dto;

import com.electronicecommerceproject.electronicecommerce.entity.Category;

public class ProductDto {
    private int productId;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private Category category;
    private String image; // Store Base64 Image

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getBase64Image() { return image; }
    public void setBase64Image(String base64Image) { this.image = base64Image; }
}

