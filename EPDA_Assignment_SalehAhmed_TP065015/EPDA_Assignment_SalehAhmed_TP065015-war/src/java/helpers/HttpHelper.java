/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author saleh
 */
public class HttpHelper {
    
    public static String getParam(HttpServletRequest request, String parameter) {  
        return request.getParameter(parameter) == null? "" : request.getParameter(parameter); 
    }
    
   public static void back(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getHeader("Referer"));
    }

    public static void forward(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        if (!response.isCommitted()) {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    public static void redirectTo(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (!response.isCommitted()) {
            response.sendRedirect(request.getContextPath() + url);
        }
    }
    
    public static void incldue(HttpServletRequest req, HttpServletResponse res, String url) throws ServletException, IOException {
        req.getRequestDispatcher(url).include(req, res);
    }
    
    public static void setSession(HttpServletRequest req, String key, Object value) {
        req.getSession().setAttribute(key, value);
    }
    
    public static Object getSession(HttpServletRequest request, String parameter) {
        return request.getSession().getAttribute(parameter);
    }
    
    public static Object getOnce(HttpServletRequest request, String parameter) {
        Object temp = request.getSession().getAttribute(parameter);
        HttpHelper.setSession(request, parameter, null);
        return temp;
    }
    
    public static boolean CheckRequestType(HttpServletRequest request, String method) {  
        return request.getMethod().toUpperCase().equals(method); 
    }
    
    public static void createAttribute(HttpServletRequest request,String name, Object c) { 
        request.setAttribute(name, c);
    }

    public static void removeSession(HttpServletRequest req, String key) {  
        HttpSession session = req.getSession(false); 
        if (session != null) {
        session.removeAttribute(key);
       }
    }
}
