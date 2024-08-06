/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Profiles;

import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import helpers.HttpHelper;
import helpers.Route;
import helpers.Validation;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.EJB.MyUserFacade;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class UpdatePassword extends HttpServlet {

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
            MyUser user = (MyUser) HttpHelper.getSession(request, "user");

            String oldPassword = HttpHelper.getParam(request, "oldPassword");
            String newPassword = HttpHelper.getParam(request, "newPassword");
            String confirmationPassword = HttpHelper.getParam(request, "confirmPassword");

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmationPassword.isEmpty()) {
                HttpHelper.setSession(request, "Error_Message", "all fields requried");
                HttpHelper.incldue(request, response, new Route().add(JspPackage.PROFILE.getPath()).add(JspFile.UPDATE_PASSWORD.getPath()).get());
            }

            if (!user.getPassword().equals(oldPassword)) {
                HttpHelper.setSession(request, "Validation_Error", "The Old Password is not Correct");
                HttpHelper.incldue(request, response, new Route().add(JspPackage.PROFILE.getPath()).add(JspFile.UPDATE_PASSWORD.getPath()).get());
            }

            if (!newPassword.equals(confirmationPassword)) {
                HttpHelper.setSession(request, "Validation_Error", "Confirmation password Should be The same as New Password");
                HttpHelper.incldue(request, response, new Route().add(JspPackage.PROFILE.getPath()).add(JspFile.UPDATE_PASSWORD.getPath()).get());
            }

            if (!Validation.checkPassword(newPassword)) {
                HttpHelper.setSession(request, "Validation_Error", "The Password Should equal or Greater the 8 characters");
                HttpHelper.incldue(request, response, new Route().add(JspPackage.PROFILE.getPath()).add(JspFile.UPDATE_PASSWORD.getPath()).get());
            }

            user.setPassword(newPassword);
            myUserFacade.edit(user);
            HttpHelper.setSession(request, "Success_Message", "Password Updated Successfully");
            HttpHelper.redirectTo(request, response, new Route().add(JspPackage.PROFILE.getPath()).add(JspFile.VIEW.getPath()).get());
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
