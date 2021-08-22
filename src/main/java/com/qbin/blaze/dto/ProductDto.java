package com.qbin.blaze.dto;

import com.qbin.blaze.models.Product;

public class ProductDto extends Product {

    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
