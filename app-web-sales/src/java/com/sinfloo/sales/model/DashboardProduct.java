
package com.sinfloo.sales.model;

public class DashboardProduct {
    private String nameProduct;
    private int quanty;

    public DashboardProduct() {
    }

    public DashboardProduct(String nameProduct, int quanty) {
        this.nameProduct = nameProduct;
        this.quanty = quanty;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getQuanty() {
        return quanty;
    }

    public void setQuanty(int quanty) {
        this.quanty = quanty;
    }
    
     
}
