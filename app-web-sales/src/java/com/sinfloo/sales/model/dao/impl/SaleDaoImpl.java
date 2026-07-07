
package com.sinfloo.sales.model.dao.impl;

import com.sinfloo.sales.config.ConnectDB;
import com.sinfloo.sales.model.Sale;
import com.sinfloo.sales.model.SaleDetails;
import com.sinfloo.sales.model.dao.SaleDao;
import com.sun.jdi.request.DuplicateRequestException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SaleDaoImpl implements SaleDao{

     private static final Logger LOGGER = Logger.getLogger(SaleDaoImpl.class.getName());
     
    @Override
    public Sale getSaleById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Sale saveSale(Sale sale) {
       int saleId=0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int r;
        
        try {
            StringBuilder queryInsert=new StringBuilder();
            queryInsert.append("INSERT INTO sale(");            
            queryInsert.append("c_type_doc");
            queryInsert.append(",c_serie");
            queryInsert.append(",n_correlativo");
            queryInsert.append(",d_date_emi");
            queryInsert.append(",n_amount_net");
            queryInsert.append(",n_amount_igv");
            queryInsert.append(",n_amount_total");
            queryInsert.append(",c_state"); 
            queryInsert.append(",n_id_customer"); 
            queryInsert.append(",n_id_employe"); 
            queryInsert.append(")values(?,?,?,?,?,?,?,?,?,?)");
            
            StringBuilder queryInsertDetails=new StringBuilder();
            queryInsertDetails.append("INSERT INTO sale_detail(");
            queryInsertDetails.append("n_price");
            queryInsertDetails.append(",n_quantity");
            queryInsertDetails.append(",n_id_sale");
            queryInsertDetails.append(",n_id_product");
            queryInsertDetails.append(")values(?,?,?,?)");
            
            conn=ConnectDB.getConnection();
            
            ps=conn.prepareStatement(queryInsert.toString(),PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, sale.getType());
            ps.setString(2, sale.getSerie());
            ps.setInt(3, sale.getCorrelative());
            ps.setDate(4, Date.valueOf(sale.getTransactionDate()));
            ps.setDouble(5, sale.getAmount_net());
            ps.setDouble(6, sale.getAmount_iva());
            ps.setDouble(7, sale.getAmount_total());
            ps.setString(8, sale.getState());
            ps.setInt(9, sale.getCustomer().getCustomerId());
            ps.setInt(10, sale.getEmploye().getEmployeId());
            
            ps.executeUpdate();
            
            rs=ps.getGeneratedKeys();
            
            while (rs.next()) {
                 saleId=rs.getInt(1);
            }
            
            ps=conn.prepareStatement(queryInsertDetails.toString());
            
            for (SaleDetails detail : sale.getDetails()) {
                ps.setDouble(1, detail.getPrice());
                ps.setInt(2, detail.getQuanty());
                ps.setInt(3, saleId);
                ps.setInt(4, detail.getProduct().getProductId());
                r=ps.executeUpdate();
                if(r!=1){
                    throw new Exception("Error al guardar el detalle de venta");
                }
                
                updateStock(detail.getProduct().getProductId(),detail.getQuanty(),conn);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    ConnectDB.releaseConnection(conn);
                } catch (SQLException ex) {
                    LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
                }
            }
            throw new DuplicateRequestException(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    ConnectDB.releaseConnection(conn);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
            }
        }
        
        sale=new Sale();
        sale.setSaleId(saleId);
        
        return sale;
    }

    private void updateStock(int productId, int quanty, Connection conn){
        PreparedStatement ps = null;
       
        try {
            StringBuilder queryUpdate=new StringBuilder();
            queryUpdate.append("update m_product ");
            queryUpdate.append("set n_stock=n_stock-? ");
            queryUpdate.append("where n_id_product=? ");
            
            ps=conn.prepareStatement(queryUpdate.toString());
            
            ps.setInt(1, quanty);
            ps.setInt(2, productId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    ConnectDB.releaseConnection(conn);
                } catch (SQLException ex) {
                    LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
                }
            }
            throw new DuplicateRequestException(e.getMessage());
        } finally {
            try {                
                if (ps != null) {
                    ps.close();
                }               
            } catch (SQLException e) {
                LOGGER.log(Level.INFO, "ERROR:{0}", e.getMessage());
            }
        }
    }
    
    @Override
    public int getLastCorreltive(String serie) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
