
package com.sinfloo.sales.model.dao.impl;

import com.sinfloo.sales.config.ConnectDB;
import com.sinfloo.sales.model.DashboardProduct;
import com.sinfloo.sales.model.DashboardSale;
import com.sinfloo.sales.model.dao.DashboardDao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardDaoImpl implements DashboardDao{

    private static final Logger LOGGER = Logger.getLogger(DashboardDaoImpl.class.getName());
    
    @Override
    public List<DashboardSale> getDataSale(Date dateIni, Date dateEnd) {
        List<DashboardSale> dashboardSales = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            
            StringBuilder query = new StringBuilder();
            query.append("select ");
            query.append("YEAR(d_date_emi) as year, ");
            query.append("MONTH(d_date_emi) as month, ");
            query.append("MONTHNAME(d_date_emi) as monthName, ");
            query.append("sum(n_amount_total) as total ");
            query.append("from ");
            query.append("sale ");
            query.append("where d_date_emi between ? and ? ");
            query.append("group by YEAR(d_date_emi),MONTH(d_date_emi), MONTHNAME(d_date_emi) ");
            query.append("order by 1,2 ");
            
            connection = ConnectDB.getConnection();
            ps = connection.prepareStatement(query.toString());
            ps.setDate(1, dateIni);
            ps.setDate(2, dateEnd);
            rs = ps.executeQuery();
            while (rs.next()) {                
                DashboardSale ds=new DashboardSale(rs.getString("monthName"), rs.getDouble("total"));
                dashboardSales.add(ds);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
            if (connection != null) {
                ConnectDB.releaseConnection(connection);
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    ConnectDB.releaseConnection(connection);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
            }
        }
        return dashboardSales;
    }

    @Override
    public List<DashboardProduct> getDataProduct(Date dateIni, Date dateEnd) {
        List<DashboardProduct> dashboardProducts = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            
            StringBuilder query = new StringBuilder();
            query.append("select ");
            query.append("p.c_name as name, ");
            query.append("sum(ds.n_quantity) as quanty ");
            query.append("from ");
            query.append("sale s INNER JOIN sale_detail ds ON s.n_id_sale=ds.n_id_sale ");
            query.append("INNER JOIN  m_product p ON ds.n_id_product=p.n_id_product ");
            query.append("where d_date_emi between ? and ? ");
            query.append("group by p.c_name ");
            query.append("order by 2 desc ");
            query.append("limit 10");
            
            connection = ConnectDB.getConnection();
            ps = connection.prepareStatement(query.toString());
            ps.setDate(1, dateIni);
            ps.setDate(2, dateEnd);
            rs = ps.executeQuery();
            while (rs.next()) {                
                DashboardProduct dp=new DashboardProduct(rs.getString(1), rs.getInt(2));
                dashboardProducts.add(dp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
            if (connection != null) {
                ConnectDB.releaseConnection(connection);
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    ConnectDB.releaseConnection(connection);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
            }
        }
        return dashboardProducts;
    }
    
}
