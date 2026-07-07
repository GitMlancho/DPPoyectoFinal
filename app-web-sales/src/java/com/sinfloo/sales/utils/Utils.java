
package com.sinfloo.sales.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.*;

public class Utils {
    
    private static final Logger LOGGER=Logger.getLogger(Utils.class.getName());
    
    public static String isNullOrEmty(String key, String value){
        if(value==null){
            value="";
        }
        boolean r=value.isBlank();
        
        if (r) {
            throw new NullPointerException("El campo "+key+" es obligario");
        }
        return value;
    }
    
    public static double convertStringtoDecimal(String value){
        double r=0.00;
        try {
            r=Double.parseDouble(value);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.INFO, "ERROR:{0}",e.getLocalizedMessage());
        }
        return r;
    }
    
    public static  long convertStringToNumeric(String value){
        long r=0;
        try {
            r=Long.parseLong(value);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.INFO, "ERROR:{0}",e.getLocalizedMessage());
        }
        
        return r;
    }
    
    public static String generateCodeWithDate(){
        SimpleDateFormat format=new SimpleDateFormat("YYYYMMDDhhmmss");
        return format.format(new Date());
    }
    
    public static LocalDate convertStringToLocalDate(String value){
        LocalDate date=null;
        try {
            if(value==null){
                value="";
            }
            if(value.isBlank()){
                value="1900-01-01";
            }
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date=LocalDate.parse(value,formatter);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "ERROR convert date :{0}", e.getMessage());
        }
        return date;
    }
    
    public static void validTypeAndNumberDoc(String type, String num){
        try {
            switch (type) {
                case "1":
                    if(convertStringToNumeric(num.trim())==0 || num.trim().length()!=8){
                        throw new Exception("El DNI debe ser numérico y con 8 digitos");
                    }
                    break;
                case "4":
                    if(num.trim().length()!=9){
                        throw new Exception("El Carnet de Ext. debe tener 9 digitos");
                    }
                    break;
                case "6":
                    if(convertStringToNumeric(num.trim())==0 || num.trim().length()!=11){
                        throw new Exception("El RUC debe ser numérico y con 11 digitos");
                    }
                    break;
                case "7":
                    if(convertStringToNumeric(num.trim())==0 || num.trim().length()!=12){
                        throw new Exception("El PASAPORTE debe ser numérico y con 12 digitos");
                    }
                    break;
            }
        } catch (Exception e) {
        }
    }
}
