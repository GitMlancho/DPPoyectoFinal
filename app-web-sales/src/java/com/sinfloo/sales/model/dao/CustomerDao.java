
package com.sinfloo.sales.model.dao;

import com.sinfloo.sales.model.Customer;
import java.util.Map;

public interface CustomerDao {
    public Customer getObjectByTypeDocAndNumdoc(String typedoc, String numdoc);
    public int saveObject(Map<String,Object> requestBody);
}
