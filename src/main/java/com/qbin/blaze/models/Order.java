package com.qbin.blaze.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("orders")
public class Order {

    @Id
    private String _id;

    private String numOrder;

    private Date date;

    private String consumer;

    private String status;

    private Float subTotal;

    private Float totalCityTax;

    private Float totalCountyTax;

    private Float totalStateTax;

    private Float totalFederalTax;

    private Float totalTaxes;

    private Float totalAmount;

    @DBRef(lazy = true)
    private List<Product> products;

    //Getters and setters
    public String get_id() {
        return _id;
    }

    public String getNumOrder() {
        return numOrder;
    }

    public void setNumOrder(String numOrder) {
        this.numOrder = numOrder;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }

    public Float getTotalCityTax() {
        return totalCityTax;
    }

    public void setTotalCityTax(Float totalCityTax) {
        this.totalCityTax = totalCityTax;
    }

    public Float getTotalCountyTax() {
        return totalCountyTax;
    }

    public void setTotalCountyTax(Float totalCountyTax) {
        this.totalCountyTax = totalCountyTax;
    }

    public Float getTotalStateTax() {
        return totalStateTax;
    }

    public void setTotalStateTax(Float totalStateTax) {
        this.totalStateTax = totalStateTax;
    }

    public Float getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(Float totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Float getTotalFederalTax() {
        return totalFederalTax;
    }

    public void setTotalFederalTax(Float totalFederalTax) {
        this.totalFederalTax = totalFederalTax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
