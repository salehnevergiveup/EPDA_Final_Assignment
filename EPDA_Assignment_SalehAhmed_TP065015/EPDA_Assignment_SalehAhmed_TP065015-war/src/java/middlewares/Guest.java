/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewares;

import helpers.Auth;
import helpers.HttpHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author saleh
 */
public class Guest {

    public static String UNAUTHORIZED = "/errors/401.jsp";

    public static void authorise(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (Auth.user(req) == null) {
            HttpHelper.redirectTo(req, res, Gate.UNAUTHORIZED);
            return;
        }
    }
}
