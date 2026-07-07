package com.sinfloo.sales.controller.sale;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sinfloo.sales.constants.ConstantEnpoint;
import com.sinfloo.sales.constants.ConstantMessages;
import com.sinfloo.sales.constants.Constants;
import com.sinfloo.sales.model.Customer;
import com.sinfloo.sales.model.Employes;
import com.sinfloo.sales.model.Sale;
import com.sinfloo.sales.model.SaleDetails;
import com.sinfloo.sales.model.dao.CustomerDao;
import com.sinfloo.sales.model.dao.SaleDao;
import com.sinfloo.sales.model.dao.impl.CustomerDaoImpl;
import com.sinfloo.sales.model.dao.impl.SaleDaoImpl;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ControllerSale extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ControllerSale.class.getName());

    private ObjectMapper objectMapper;
    private CustomerDao customerDao;
    private SaleDao saleDao;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> result = new HashMap<>();
        String pathInfo = request.getPathInfo() == null ? ConstantEnpoint.ROOT : request.getPathInfo();
        HttpSession session = request.getSession();
        HttpSession sessionSale = request.getSession();
        Employes sessionUser = (Employes) session.getAttribute(Constants.USERLOGIN);
        try {
            if (sessionUser != null) {
                switch (pathInfo) {
                    case ConstantEnpoint.ROOT:
                        request.getRequestDispatcher(ConstantEnpoint.PAGE_SALE).forward(request, response);
                        break;
                    case ConstantEnpoint.OBTAIN:
                        if (request.getMethod().equals(ConstantEnpoint.GET)) {
                           Sale sale=(Sale)sessionSale.getAttribute(Constants.SALE);
                           if(sale==null){
                               sale=new Sale();
                               sessionSale.setAttribute(Constants.SALE, sale);
                           }
                           result.put(Constants.SALE, sale);
                        } else {
                            throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                        }
                        break;
                    case ConstantEnpoint.OBTAIN_CUSTOMER:
                        if (request.getMethod().equals(ConstantEnpoint.POST)) {
                            Map<String, Object> requestBody = getObjectMapper().readValue(request.getReader(), Map.class);
                            Customer customer = getCustomerDao().getObjectByTypeDocAndNumdoc(requestBody.get("typedoc").toString(), requestBody.get("numdoc").toString());
                            Sale sale = (Sale) sessionSale.getAttribute(Constants.SALE);
                            if (sale == null) {
                                sale = new Sale();
                            }
                            sale.setCustomer(Optional.ofNullable(customer).orElse(new Customer()));
                            sale.setEmploye(sessionUser);

                            sessionSale.setAttribute(Constants.SALE, sale);
                            result.put(Constants.SALE, sale);
                        } else {
                            throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                        }
                        break;
                    case ConstantEnpoint.ADD:
                        if (request.getMethod().equals(ConstantEnpoint.POST)) {
                            SaleDetails saleDetails = getObjectMapper().readValue(request.getReader(), SaleDetails.class);
                            Sale sale = (Sale) sessionSale.getAttribute(Constants.SALE);
                            if (sale.getDetails().isEmpty()) {
                                if (saleDetails.getQuanty() > saleDetails.getProduct().getStock() || saleDetails.getQuanty() <= 0) {
                                    throw new Exception(ConstantMessages.MESSAGE_VAL_412);
                                }
                                sale.getDetails().add(saleDetails);
                                sale.setAmount_total(sale.getDetails().get(0).getTotal());
                            } else {
                                String addProduct = "1";
                                updateQuanty(sale, saleDetails, addProduct);
                            }

                            sessionSale.setAttribute(Constants.SALE, sale);
                            result.put(Constants.SALE, sale);
                        } else {
                            throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                        }
                        break;
                    case ConstantEnpoint.UPDATE:
                        if (request.getMethod().equals(ConstantEnpoint.POST)) {
                            SaleDetails saleDetails = getObjectMapper().readValue(request.getReader(), SaleDetails.class);
                            Sale sale = (Sale) sessionSale.getAttribute(Constants.SALE);
                            String addProduct = "0";
                            updateQuanty(sale, saleDetails, addProduct);

                            sessionSale.setAttribute(Constants.SALE, sale);
                            result.put(Constants.SALE, sale);
                        } else {
                            throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                        }
                        break;
                    case ConstantEnpoint.DELETE:
                        if (request.getMethod().equals(ConstantEnpoint.GET)) {
                            int productId = Integer.parseInt(request.getParameter("id"));
                            Sale sale = (Sale) sessionSale.getAttribute(Constants.SALE);
                            removeProductIdFromDetails(sale, productId);

                            sessionSale.setAttribute(Constants.SALE, sale);
                            result.put(Constants.SALE, sale);

                        } else {
                            throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                        }
                        break;
                    case ConstantEnpoint.CANCEL:
                        if (request.getMethod().equals(ConstantEnpoint.GET)) {
                            Sale sale=new Sale();
                            sessionSale.setAttribute(Constants.SALE, sale);
                            result.put(Constants.SALE, sale);
                        } else {
                            throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                        }
                        break;
                    case ConstantEnpoint.SAVE:
                        if (request.getMethod().equals(ConstantEnpoint.GET)) {
                            Sale sale = (Sale) sessionSale.getAttribute(Constants.SALE);
                            if(sale==null){
                                throw new IllegalArgumentException(ConstantMessages.MESSAGE_VAL_710);
                            }
                            sale.setTransactionDate(LocalDate.now());
                            sale.setType("F");
                            sale.setSerie("F001");
                            sale.setState("A");
                           
                            if(sale.getCustomer()==null || sale.getCustomer().getCustomerId()==0){
                                throw new IllegalArgumentException(ConstantMessages.MESSAGE_VAL_511);
                            }

                            if (sale.getDetails().isEmpty()) {
                                throw new IllegalArgumentException(ConstantMessages.MESSAGE_VAL_610);
                            }

                            Sale saleResponse = getSaleDao().saveSale(sale);    
                            
                            sale.setCorrelative(saleResponse.getSaleId());
                            
                            jar.app.generate.pdf.dto.Sale saleRequestPdf = getObjectMapper().convertValue(sale, jar.app.generate.pdf.dto.Sale.class);

                            String filename = jar.app.generate.pdf.ComprobantePDF.generarPDF(saleRequestPdf, "D:\\sale\\img\\logo.png", "D:\\sale\\comprobantes\\");

                            if (saleResponse.getSaleId() > 0) {
                                sale = new Sale();
                            }

                            sale.setFilename(filename);

                            sessionSale.setAttribute(Constants.SALE, sale);

                            result.put(Constants.SALE, sale);

                        } else {
                            throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                        }
                        break;
                    default:
                        throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                }
                result.put(ConstantMessages.STATUS_KEY, ConstantMessages.STATUS_200);
                result.put(ConstantMessages.MESSAGE_KEY, ConstantMessages.MESSAGE_VAL_200);
            } else {
                throw new AccessDeniedException(ConstantMessages.MESSAGE_VAL_404);
            }
        } catch (AccessDeniedException e) {
            LOGGER.log(Level.INFO, "ERROR: {0}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(ConstantEnpoint.PAGE_NOTFOUND).forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "ERROR: {0}", e.getMessage());
            result.put(ConstantMessages.STATUS_KEY, ConstantMessages.STATUS_400);
            result.put(ConstantMessages.MESSAGE_KEY, e.getMessage());
        }
        String resultJson = getObjectMapper().writeValueAsString(result);
        response.setContentType(ConstantMessages.CONTENT_TYPE_JSON);
        response.setCharacterEncoding(ConstantMessages.ENCODE_UFT8);
        response.getWriter().write(resultJson);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void updateQuanty(Sale sale, SaleDetails saleDetails, String addProduct) throws Exception {
        int pos = 0;
        double totalSale = 0.00;
        boolean duplicity = false;
        for (SaleDetails detail : sale.getDetails()) {
            if (saleDetails.getProduct().getCode().equals(detail.getProduct().getCode())) {
                int quanty = addProduct.equals("1") ? detail.getQuanty() + saleDetails.getQuanty() : saleDetails.getQuanty();
                if (quanty > detail.getProduct().getStock()) {
                    throw new Exception(ConstantMessages.MESSAGE_VAL_412);
                }
                sale.getDetails().get(pos).setQuanty(quanty);
                duplicity = true;
                break;
            }
            pos++;
        }
        if (!duplicity) {
            if (saleDetails.getQuanty() > saleDetails.getProduct().getStock() || saleDetails.getQuanty() <= 0) {
                throw new Exception(ConstantMessages.MESSAGE_VAL_412);
            }
            sale.getDetails().add(saleDetails);
        }
        for (SaleDetails detail : sale.getDetails()) {
            totalSale = totalSale + detail.getTotal();
        }

        sale.setAmount_total(totalSale);
    }

    private void removeProductIdFromDetails(Sale sale, int productId) {
        double totalSale = 0.00;
        int position = 0;
        for (SaleDetails detail : sale.getDetails()) {
            if (detail.getProduct().getProductId() == productId) {
                sale.getDetails().remove(position);
                break;
            }
            position++;
        }
        for (SaleDetails detail : sale.getDetails()) {
            totalSale = totalSale + detail.getTotal();
        }
        sale.setAmount_total(totalSale);
    }

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }
        return objectMapper;
    }

    private CustomerDao getCustomerDao() {
        if (customerDao == null) {
            customerDao = new CustomerDaoImpl();
        }
        return customerDao;
    }

    private SaleDao getSaleDao() {
        if (saleDao == null) {
            saleDao = new SaleDaoImpl();
        }
        return saleDao;
    }
}
