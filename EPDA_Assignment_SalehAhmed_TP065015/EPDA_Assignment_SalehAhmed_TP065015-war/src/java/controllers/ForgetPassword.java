/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import Service.Email.SendEmail;
import controllers.enums.JspFile;
import controllers.enums.ServletFile;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
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
public class ForgetPassword extends HttpServlet {

    @EJB
    private MyUserFacade myUserFacade;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";

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
           
            String email = HttpHelper.getParam(request, "email");
            MyUser user  = myUserFacade.findByEmail(email);  
            
            if ( user == null) {
                HttpHelper.setSession(request, "Validation_Error", "Email not found!");
                HttpHelper.redirectTo(request, response, new Route().add(ServletFile.FORGETPASSWORD.getPath()).get());
                return;
            }

            Random random = new Random();
            StringBuilder password = new StringBuilder(8);

            for (int i = 0; i < 8; i++) {
                password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }

            user.setPassword(password.toString());
            myUserFacade.edit(user);

            new SendEmail()
                    .to(email)
                    .subject("Reset Password")
                    .salute()
                    .content("Your new password is: " + password.toString())
                    .signature()
                    .send();

            HttpHelper.setSession(request, "Success_Message", "A new password has been sent to your email.");
            HttpHelper.redirectTo(request, response, new Route().add(JspFile.LOGIN.getPath()).get());
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
