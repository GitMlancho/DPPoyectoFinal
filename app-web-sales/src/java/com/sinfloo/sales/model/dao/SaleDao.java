
package com.sinfloo.sales.model.dao;

import com.sinfloo.sales.model.Sale;

public interface SaleDao {
    public Sale getSaleById(int id);
    public Sale saveSale(Sale sale);
    public int getLastCorreltive(String serie);
}
