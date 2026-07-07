
package com.sinfloo.sales.model.dao;

import com.sinfloo.sales.model.DashboardProduct;
import com.sinfloo.sales.model.DashboardSale;
import java.util.List;
import java.sql.Date;

public interface DashboardDao {
    public List<DashboardSale>getDataSale(Date dateIni, Date dateEnd);
    public List<DashboardProduct>getDataProduct(Date dateIni, Date dateEnd);
}
