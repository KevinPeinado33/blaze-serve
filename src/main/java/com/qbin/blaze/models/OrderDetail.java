package com.qbin.blaze.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("orders_detail")
public class OrderDetail {

    @Id
    private String _id;

    private Integer quantity;

    private Float cost;

    @DBRef(lazy = true)
    private Product product;

    // Getters and setters
    public String get_id() {
        return _id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
