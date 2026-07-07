
package com.sinfloo.sales.model;

public class SaleDetails {
    private Product product;
    private double price;
    private int quanty;
    private double total;

    public SaleDetails() {
        
    }

    public SaleDetails(Product product, double price, int quanty, double total) {
        this.product = product;
        this.price = price;
        this.quanty = quanty;
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuanty() {
        return quanty;
    }

    public void setQuanty(int quanty) {
        this.quanty = quanty;
    }

    public double getTotal() {
        total=price*quanty;
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
}
