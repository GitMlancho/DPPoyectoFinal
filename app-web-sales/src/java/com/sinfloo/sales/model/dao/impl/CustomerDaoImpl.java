
package com.sinfloo.sales.model.dao.impl;

import com.sinfloo.sales.config.ConnectDB;
import com.sinfloo.sales.constants.ConstantMessages;
import com.sinfloo.sales.constants.Constants;
import com.sinfloo.sales.model.Customer;
import com.sinfloo.sales.model.Departament;
import com.sinfloo.sales.model.District;
import com.sinfloo.sales.model.Province;
import com.sinfloo.sales.model.Ubigeo;
import com.sinfloo.sales.model.dao.CustomerDao;
import com.sinfloo.sales.utils.Utils;
import java.util.Map;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDaoImpl implements CustomerDao{

    private static final Logger LOGGER=Logger.getLogger(CustomerDaoImpl.class.getName());
    
    @Override
    public Customer getObjectByTypeDocAndNumdoc(String typedoc, String numdoc) {
        Customer customer=null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query = new StringBuilder();
        query.append("select ");
        query.append(" c.n_id_customer");
        query.append(" ,c.c_code");
        query.append(" ,c.c_type_doc");
        query.append(" ,c.c_num_doc");
        query.append(" ,c.c_business_name");
        query.append(" ,p.n_id_person");
        query.append(" ,p.c_name");
        query.append(" ,p.c_first_name");
        query.append(" ,p.c_last_name");
        query.append(" ,p.c_civil_status");
        query.append(" ,p.d_date_birth"); //Error
        query.append(" ,p.c_gender");
        query.append(" ,c.c_address");
        query.append(" ,c.c_phone_main");
        query.append(" ,c.c_email_main");
        query.append(" ,u.n_id_ubigeo");
        query.append(",u.c_departamento_inei ");
        query.append(",u.c_departamento ");
        query.append(",u.c_provincia_inei ");
        query.append(",u.c_provincia ");
        query.append(",u.c_ubigeo_inei ");
        query.append(",u.c_distrito ");
        query.append(" from m_customer c inner join m_person p ");
        query.append(" On c.n_id_person=p.n_id_person left join m_ubigeo u ON c.n_id_ubigeo=u.n_id_ubigeo"); //Error
        query.append(" where c.c_type_doc=? and c.c_num_doc=?");
        try {
            conn = ConnectDB.getConnection();
            ps = conn.prepareStatement(query.toString());
            ps.setString(1, typedoc);
            ps.setString(2,numdoc);
            
            rs=ps.executeQuery();
            
            while (rs.next()) {     
                customer= new Customer();
                customer.setCustomerId(rs.getInt("n_id_customer"));
                customer.setCustomerCode(rs.getString("c_code"));
                customer.setTypedoc(rs.getString("c_type_doc"));
                customer.setNumdoc(rs.getString("c_num_doc"));
                customer.setBussinesName(rs.getString("c_business_name"));
                customer.setPersonId(rs.getInt("n_id_person"));
                customer.setName(rs.getString("c_name"));
                customer.setFirstName(rs.getString("c_first_name"));
                customer.setLastName(rs.getString("c_last_name"));
                customer.setCivilState(rs.getString("c_civil_status"));
                customer.setGender(rs.getString("c_gender"));
                customer.setBirthDate(Utils.convertStringToLocalDate(rs.getString("d_date_birth")));
                customer.setAddress(rs.getString("c_address"));
                customer.setNumberPhone(rs.getString("c_phone_main"));
                customer.setEmail(rs.getString("c_email_main"));
                customer.setUbigeo(new Ubigeo(rs.getInt("n_id_ubigeo"),
                        new Departament(rs.getString("c_departamento_inei"), rs.getString("c_departamento")),
                        new Province(rs.getString("c_provincia_inei"), rs.getString("c_provincia")),
                        new District(rs.getString("c_ubigeo_inei"), rs.getString("c_distrito"))));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "ERROR:{0}",e.getLocalizedMessage());
        }catch(Exception e){
            LOGGER.log(Level.INFO, "ERROR:{0}",e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }finally{
            try {
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
                if(conn!=null){
                    ConnectDB.releaseConnection(conn);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.INFO, "ERROR:{0}",e.getLocalizedMessage());
            }
        }
        /**
        if(customer==null){
            throw new NullPointerException(ConstantMessages.MESSAGE_VAL_510);
        }**/

        return customer;
        
    }

    @Override
    public int saveObject(Map<String, Object> requestBody) {
        int r=0;
        int personId=0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        if(requestBody.get("typeDoc").toString().equals("6")){
            mappingRequestToValidateEmptyOrNullBussines(requestBody);
        }else{
            mappingRequestToValidateEmptyOrNullPerson(requestBody);
        }
        
        Utils.validTypeAndNumberDoc(requestBody.get("typeDoc").toString(), requestBody.get("numDoc").toString());
        
        StringBuilder queryInsertPerson = new StringBuilder();
        queryInsertPerson.append("insert into m_person(");
        queryInsertPerson.append("c_name");
        queryInsertPerson.append(",c_first_name");
        queryInsertPerson.append(",c_last_name");
        queryInsertPerson.append(",c_civil_status");
        queryInsertPerson.append(",c_gender");
        queryInsertPerson.append(",d_date_birth");
        queryInsertPerson.append(",c_state");
        queryInsertPerson.append(")values(?,?,?,?,?,?,?)");
        
        StringBuilder queryInsertCustomer = new StringBuilder();
        queryInsertCustomer.append("insert into m_customer(");
        queryInsertCustomer.append("c_code ");
        queryInsertCustomer.append(",c_type_doc ");
        queryInsertCustomer.append(",c_num_doc ");
        queryInsertCustomer.append(",c_business_name ");
        queryInsertCustomer.append(",c_address ");
        queryInsertCustomer.append(",c_phone_main ");
        queryInsertCustomer.append(",c_email_main ");
        queryInsertCustomer.append(",c_create_user ");
        queryInsertCustomer.append(",n_id_person ");
        queryInsertCustomer.append(",n_id_ubigeo ");
        queryInsertCustomer.append(",c_state) values(?,?,?,?,?,?,?,?,?,?,?)");
        
        
        try {
            conn = ConnectDB.getConnection();
            ps = conn.prepareStatement(queryInsertPerson.toString(),PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, requestBody.get("name").toString().trim().toUpperCase());
            ps.setString(2, requestBody.get("firstName").toString().trim().toUpperCase());
            ps.setString(3, requestBody.get("lastName").toString().trim().toUpperCase());
            ps.setString(4, requestBody.get("selectCivilState").toString().trim());
            ps.setString(5, requestBody.get("selectGender").toString().trim());
            ps.setString(6, requestBody.get("birthDate").toString().trim());
            ps.setString(7, "A");
            
            r=ps.executeUpdate();
            
            rs=ps.getGeneratedKeys();
            
            while (rs.next()) {                
                personId=rs.getInt(1);
            }
            
            ps=conn.prepareStatement(queryInsertCustomer.toString(),PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, "C".concat(Utils.generateCodeWithDate()));
            ps.setString(2, requestBody.get("typeDoc").toString().trim());
            ps.setString(3, requestBody.get("numDoc").toString().trim());
            ps.setString(4, requestBody.get("businessName").toString().trim().toUpperCase());
            ps.setString(5, requestBody.get("address").toString().trim());
            ps.setString(6, requestBody.get("numberPhone").toString().trim());
            ps.setString(7, requestBody.get("email").toString().trim());
            ps.setString(8, requestBody.get("sessionUser").toString().trim());
            ps.setInt(9, personId);
            ps.setInt(10, 1);/**Agregar de forma dinámica**/
            ps.setString(11, "A");
            
            r=ps.executeUpdate();
            
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "ERROR:{0}",e.getLocalizedMessage());
        }catch(Exception e){
            LOGGER.log(Level.INFO, "ERROR:{0}",e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }finally{
            try {
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
                if(conn!=null){
                    ConnectDB.releaseConnection(conn);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.INFO, "ERROR:{0}",e.getLocalizedMessage());
            }
        }
        return r;
    }
    
    private void mappingRequestToValidateEmptyOrNullPerson(Map<String,Object>requestBody){
        for (Map.Entry<String,Object>entry:requestBody.entrySet()) {
            if(mandatoryColumnsPerson().contains(entry.getKey())){
                Utils.isNullOrEmty(entry.getKey(), entry.getValue().toString());
            }
        }
        if(requestBody.get("birthDate").toString().isBlank()){
            requestBody.put("birthDate","1900-01-01");
        }
    }
    private void mappingRequestToValidateEmptyOrNullBussines(Map<String,Object>requestBody){
        for (Map.Entry<String,Object>entry:requestBody.entrySet()) {
            if(mandatoryColumnsBusiness().contains(entry.getKey())){
                Utils.isNullOrEmty(entry.getKey(), entry.getValue().toString());
            }
        }
        if(requestBody.get("birthDate").toString().isBlank()){
            requestBody.put("birthDate","1900-01-01");
        }
    }
    
    private Set<String>mandatoryColumnsBusiness(){
        Set<String>columns=new HashSet<>();
        columns.add("businessName");
        columns.add("typeDoc");
        columns.add("numDoc");
        columns.add("email");
        return columns;
    }
    private Set<String>mandatoryColumnsPerson(){
        Set<String>columns=new HashSet<>();
        columns.add("name");
        columns.add("firstName");
        columns.add("lastName");
        columns.add("typeDoc");
        columns.add("numDoc");
        columns.add("email");
        return columns;
    }
    
}
