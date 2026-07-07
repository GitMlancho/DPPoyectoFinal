
package com.sinfloo.sales.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    
    private static final double IVA=0.18;
    
    private int saleId;
    private String type;
    private String serie;
    private int correlative;
    private LocalDate transactionDate;
    private Customer customer;
    private List<SaleDetails>details;
    private double amount_net;
    private double amount_iva;
    private double amount_total;
    private Employes employe;
    private String state;
    
    private String filename;

    public Sale() {
        details=new ArrayList<>();
    }

    public Sale(int saleId, String type, String serial, int correlative, LocalDate trasactionDate, Customer customer, List<SaleDetails> details, double amount_net, double amount_iva, double amount_total, Employes employes, String state) {
        this.saleId = saleId;
        this.type = type;
        this.serie = serial;
        this.correlative = correlative;
        this.transactionDate = trasactionDate;
        this.customer = customer;
        this.details = details;
        this.amount_net = amount_net;
        this.amount_iva = amount_iva;
        this.amount_total = amount_total;
        this.employe = employes;
        this.state = state;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getCorrelative() {
        return correlative;
    }

    public void setCorrelative(int correlative) {
        this.correlative = correlative;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SaleDetails> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetails> details) {
        this.details = details;
    }

    public double getAmount_net() {
        amount_net=amount_total-(amount_total*IVA);
        return amount_net;
    }

    public void setAmount_net(double amount_net) {
        this.amount_net = amount_net;
    }

    public double getAmount_iva() {
        amount_iva=amount_total*IVA;
        return amount_iva;
    }

    public void setAmount_iva(double amount_iva) {
        this.amount_iva = amount_iva;
    }

    public double getAmount_total() {
        return amount_total;
    }

    public void setAmount_total(double amount_total) {
        this.amount_total = amount_total;
    }

    public Employes getEmploye() {
        return employe;
    }

    public void setEmploye(Employes employe) {
        this.employe = employe;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    
    
}
