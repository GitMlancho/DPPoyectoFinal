
package com.sinfloo.sales.config;

import com.sinfloo.sales.controller.ServletContextProvider;
import java.util.Properties;
import jakarta.servlet.ServletContext;

public class ConnectDB {
    
    private static final String PROPERTIES_FILE="application.properties";
    
    private static final String URL;
    private static final String USER;
    private static final String PASS;
    private static final String DRIVER;

    static {
        Properties properties=loadProperties();
        URL=properties.getProperty("dababase.url");
        USER=properties.getProperty("dababase.user");
        PASS=properties.getProperty("dababase.password");
        DRIVER=properties.getProperty("dababase.driver");
    }
    private static Properties loadProperties(){
    Properties properties=new Properties();
    ServletContext context=ServletContextProvider.getServletContext();
    String path=context
    return properties;
    }
    
}

