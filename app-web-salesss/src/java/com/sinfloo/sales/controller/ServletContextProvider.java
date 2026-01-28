  /**
package com.sinfloo.sales.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;

public class ServletContextProvider extends HttpServlet {

    private static ServletContext context;

    public static void setServletContext(ServletContext context) {
        ServletContextProvider.context = context;
    }

    public static ServletContext getServletContext() {
        return context;
    }
}
 **/      

package com.sinfloo.sales.controller;

import jakarta.servlet.ServletContext;

public class ServletContextProvider {

    private static ServletContext context;

    public static void setServletContext(ServletContext context) {
        ServletContextProvider.context = context;
    }

    public static ServletContext getServletContext() {
        return context;
    }
}

