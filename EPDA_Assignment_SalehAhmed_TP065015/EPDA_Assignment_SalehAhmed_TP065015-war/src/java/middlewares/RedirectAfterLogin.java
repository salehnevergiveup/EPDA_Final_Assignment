/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewares;

import controllers.enums.AccountStatus;
import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
import helpers.Auth;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class RedirectAfterLogin {

    public static void handle(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        MyUser user = (MyUser) req.getSession().getAttribute("user");

        if (user != null) {
            if(user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) {  
                HttpHelper.redirectTo(req, res, new Route().add(ServletFile.DASHBOARD.getPath()).get());
            }
            HttpHelper.redirectTo(req, res, new Route().add(ServletPackage.PROFILES.getPath()).add(ServletFile.VIEW.getPath()).get());
            
        }
    }
}

