
package com.sinfloo.sales.model;

public class DashboardSale {
    private String monthName;
    private double total;

    public DashboardSale() {
    }

    public DashboardSale(String monthName, double total) {
        this.monthName = monthName;
        this.total = total;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
}
