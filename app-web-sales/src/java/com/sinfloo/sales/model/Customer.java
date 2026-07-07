
package com.sinfloo.sales.model;

public class Customer extends Person{
    private int customerId;
    private String customerCode;
    private String typedoc;
    private String numdoc;
    private String bussinesName;
    private String numberPhone;
    private String address;
    private String email;
    private Ubigeo ubigeo;
    private String state;

    public Customer() {
    }

    public Customer(int customerId, String customerCode, String typedoc, String numdoc, String bussinesName, String numberPhone, String address, String email, Ubigeo ubigeo, String state) {
        this.customerId = customerId;
        this.customerCode = customerCode;
        this.typedoc = typedoc;
        this.numdoc = numdoc;
        this.bussinesName = bussinesName;
        this.numberPhone = numberPhone;
        this.address = address;
        this.email = email;
        this.ubigeo = ubigeo;
        this.state = state;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

   

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getTypedoc() {
        return typedoc;
    }

    public void setTypedoc(String typedoc) {
        this.typedoc = typedoc;
    }

    public String getNumdoc() {
        return numdoc;
    }

    public void setNumdoc(String numdoc) {
        this.numdoc = numdoc;
    }

    public String getBussinesName() {
        return bussinesName;
    }

    public void setBussinesName(String bussinesName) {
        this.bussinesName = bussinesName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Ubigeo getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(Ubigeo ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    
}
