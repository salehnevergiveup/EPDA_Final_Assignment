/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import controllers.enums.BaseRoute;
import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;

/**
 *
 * @author saleh
 */
public class Route {
    private String route = "";
    public Route add(String path) {  
        route += "/"+path;
        return this; 
    }
    public String get() { 
        return route;
    }

    public Object add(JspPackage jspPackage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

