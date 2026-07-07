package com.sinfloo.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sinfloo.sales.constants.ConstantEnpoint;
import com.sinfloo.sales.constants.ConstantMessages;
import com.sinfloo.sales.controller.mnto.ControllerProduct;
import com.sinfloo.sales.model.DashboardProduct;
import com.sinfloo.sales.model.DashboardSale;
import com.sinfloo.sales.model.Employes;
import com.sinfloo.sales.model.dao.DashboardDao;
import com.sinfloo.sales.model.dao.ProductDao;
import com.sinfloo.sales.model.dao.impl.DashboardDaoImpl;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;

public class ControllerMain extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ControllerMain.class.getName());

    private DashboardDao dashboardDao;

    private ObjectMapper objectMapper;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> result = new HashMap<>();
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        Employes employe = (Employes) session.getAttribute("sessionUser");
        try {
            if (employe != null) {
                switch (pathInfo) {
                    case ConstantEnpoint.HOME:
                        if (request.getMethod().equals(ConstantEnpoint.GET)) {
                            request.getRequestDispatcher(ConstantEnpoint.PAGE_MAIN).forward(request, response);
                        } else {
                            throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                        }
                        break;
                    case ConstantEnpoint.OBTAIN:
                        if (request.getMethod().equals(ConstantEnpoint.GET)) {
                            LocalDate dateEnd = LocalDate.now();
                            LocalDate dateIni = dateEnd.minusMonths(12);
                            proccessData(dateIni, dateEnd, result);
                        } else if (request.getMethod().equals(ConstantEnpoint.POST)) {
                            Map<String, Object> requestBody = getObjectMapper().readValue(request.getReader(), Map.class);

                            LocalDate dateEnd = LocalDate.parse((String) requestBody.get("dateEnd"));
                            LocalDate dateIni = LocalDate.parse((String) requestBody.get("dateIni"));
                            
                            proccessData(dateIni, dateEnd, result);

                        } else {
                            throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                        }
                        break;
                    case ConstantEnpoint.LOGOUT:
                        session.invalidate();
                        response.sendRedirect(request.getContextPath() + ConstantEnpoint.PAGE_LOGIN);
                        break;
                    default:
                        throw new AccessDeniedException(ConstantMessages.MSG_PAGE_NOT_FOUND);
                }
            } else {
                throw new AccessDeniedException(ConstantMessages.MESSAGE_VAL_404);
            }

            result.put(ConstantMessages.STATUS_KEY, ConstantMessages.STATUS_200);
            result.put(ConstantMessages.MESSAGE_KEY, ConstantMessages.MESSAGE_VAL_200);

        } catch (AccessDeniedException e) {
            LOGGER.log(Level.INFO, "ERROR: {0}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(ConstantEnpoint.PAGE_NOTFOUND).forward(request, response);
        } catch (NullPointerException | IOException e) {
            request.setAttribute("error", e.getMessage());
            if (e.getLocalizedMessage().equals(ConstantMessages.MESSAGE_VAL_404)) {
                response.sendRedirect(request.getContextPath() + ConstantEnpoint.PAGE_LOGIN);
            } else {
                request.getRequestDispatcher(ConstantEnpoint.PAGE_NOTFOUND).forward(request, response);
            }
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

    private void proccessData(LocalDate dateIni, LocalDate dateEnd, Map<String, Object> result) {
        Date dateIniSql = Date.valueOf(dateIni);
        Date dateEndSql = Date.valueOf(dateEnd);
        List<DashboardSale> dashboardSales = getDashboardDao().getDataSale(dateIniSql, dateEndSql);
        List<DashboardProduct> dashboardProducts = getDashboardDao().getDataProduct(dateIniSql, dateEndSql);
        result.put("sales", dashboardSales);
        result.put("products", dashboardProducts);
    }

    private DashboardDao getDashboardDao() {
        if (dashboardDao == null) {
            dashboardDao = new DashboardDaoImpl();
        }
        return dashboardDao;
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

}
