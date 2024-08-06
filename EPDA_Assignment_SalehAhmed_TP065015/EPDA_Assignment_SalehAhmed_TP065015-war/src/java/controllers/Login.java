/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.enums.AccountStatus;
import controllers.enums.JspFile;
import helpers.Auth;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.RedirectAfterLogin;
import model.EJB.MyUserFacade;

/**
 *
 * @author saleh
 */
public class Login extends HttpServlet {

    @EJB
    private MyUserFacade myUserFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
 
        try (PrintWriter out = response.getWriter()) {
 
            String emailOrUsername = HttpHelper.getParam(request, "username");
            String password = HttpHelper.getParam(request, "password");
            if (emailOrUsername.isEmpty() || password.isEmpty()) {
                HttpHelper.setSession(request, "Validation_Error", "Your credential do not match in the system");
                HttpHelper.redirectTo(request, response, new Route().add(JspFile.LOGIN.getPath()).get());
            }

            Auth auth = new Auth(myUserFacade);
            if (!auth.tryToLoging(emailOrUsername, password)) {
                HttpHelper.setSession(request, "Validation_Error", "Your credential do not match in the system");
                HttpHelper.incldue(request, response, new Route().add(JspFile.LOGIN.getPath()).get());
            } else {
                HttpHelper.setSession(request, "user", auth.user(emailOrUsername));
                if (!Auth.user(request).getStatus().equals(AccountStatus.ACTIVE.getStatus())) {
                    HttpHelper.setSession(request, "Validation_Error", "Your account is " + Auth.user(request).getStatus() + ". Sorry, you can't log in.");
                    HttpHelper.removeSession(request, "user");
                    HttpHelper.incldue(request, response, new Route().add(JspFile.LOGIN.getPath()).get());
                }
                RedirectAfterLogin.handle(request, response);
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
