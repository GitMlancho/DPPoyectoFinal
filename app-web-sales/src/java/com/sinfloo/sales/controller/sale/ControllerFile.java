
package com.sinfloo.sales.controller.sale;

import com.sinfloo.sales.constants.ConstantEnpoint;
import com.sinfloo.sales.constants.ConstantMessages;
import com.sinfloo.sales.constants.Constants;
import com.sinfloo.sales.model.Employes;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.AccessDeniedException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ControllerFile extends HttpServlet {

    
    private static final Logger LOGGER = Logger.getLogger(ControllerFile.class.getName());
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo() == null ? ConstantEnpoint.ROOT : request.getPathInfo();
        HttpSession session = request.getSession();
        Employes sessionUser = (Employes) session.getAttribute(Constants.USERLOGIN);
        try {
            if (sessionUser != null) {
                switch (pathInfo) {
                    case ConstantEnpoint.ROOT:
                        String pathFilename=request.getParameter("filename");
                        File file=new File(pathFilename);
                        String filename=file.getName();
                        response.setContentType("application/pdf");
                        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
                        
                        try(FileInputStream fis=new FileInputStream(file);OutputStream os=response.getOutputStream()) {
                            response.setContentLength((int) file.length());
                            byte[] buffer=new byte[4096];
                            int bytesRead;
                            while ((bytesRead=fis.read(buffer))!= -1) {                                
                                os.write(buffer,0,bytesRead);
                            }
                        } 
                        break;
                    default:
                        throw new Exception(ConstantMessages.MSG_PAGE_NOT_FOUND);
                }                  
                    
            } else {
                throw new AccessDeniedException(ConstantMessages.MESSAGE_VAL_404);
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "ERROR: {0}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(ConstantEnpoint.PAGE_NOTFOUND).forward(request, response);
        }
        
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

}
