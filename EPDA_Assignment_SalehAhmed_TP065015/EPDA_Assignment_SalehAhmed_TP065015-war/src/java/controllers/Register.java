/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import helpers.Auth;
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
import model.EJB.CustomerInfoFacade;
import model.EJB.JobseekerInfoFacade;
import model.EJB.MyRoleFacade;
import model.EJB.MyUserFacade;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class Register extends HttpServlet {

    @EJB
    private MyUserFacade myUserFacade;

    @EJB
    private MyRoleFacade myRoleFacade;

    @EJB
    private JobseekerInfoFacade jobseekerInfoFacade;

    @EJB
    private CustomerInfoFacade customerInfoFacade;

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

            String name = HttpHelper.getParam(request, "name");
            String username = HttpHelper.getParam(request, "username");
            String password = HttpHelper.getParam(request, "password");
            String confirmPassword = HttpHelper.getParam(request, "confirmPassword");
            String email = HttpHelper.getParam(request, "email");
            String phoneNumber = HttpHelper.getParam(request, "phoneNumber");
            String role = HttpHelper.getParam(request, "role");

            // Additional fields for Customer
            String customerAddress = HttpHelper.getParam(request, "customerAddress");
            String companyName = HttpHelper.getParam(request, "companyName");
            String website = HttpHelper.getParam(request, "website");

            // Additional fields for Jobseeker
            String jobseekerAddress = HttpHelper.getParam(request, "jobseekerAddress");
            String hobbies = HttpHelper.getParam(request, "hobbies");
            String gender = HttpHelper.getParam(request, "gender");
            String skills = HttpHelper.getParam(request, "skills");
            String ageStr = HttpHelper.getParam(request, "age");

            if (name.isEmpty()
                    || username.isEmpty()
                    || email.isEmpty()
                    || password.isEmpty()
                    || role.isEmpty()
                    || phoneNumber.isEmpty()
                    || confirmPassword.isEmpty()
                    || !Validation.checkEmail(email)
                    || !Validation.checkPassword(password)
                    || !password.equals(confirmPassword)) {
                HttpHelper.setSession(request, "Validation_Error", "Please fill all fields correctly.");
                HttpHelper.incldue(request, response, new Route().add(JspFile.REGISTER.getPath()).get());
                return;
            }

            if ("Jobseeker".equals(role)) {
                if (jobseekerAddress.isEmpty()
                        || hobbies.isEmpty()
                        || skills.isEmpty()
                        || gender.isEmpty()
                        ||ageStr.isEmpty()) {
                    HttpHelper.setSession(request, "Validation_Error", "Please fill all the fields correctly.");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get());
                    return;

                }
            }

            if ("Customer".equals(role)) {
                if (customerAddress.isEmpty()
                        || companyName.isEmpty()
                        || website.isEmpty()) {
                    HttpHelper.setSession(request, "Validation_Error", "Please fill all the fields correctly.");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get());
                    return;

                }
            }

            Auth user = new Auth(myUserFacade, myRoleFacade, customerInfoFacade, jobseekerInfoFacade);
            if (user.tryToRegisterEmial(email)) {
                HttpHelper.setSession(request, "Error_Message", "The email is already being used!");
                HttpHelper.incldue(request, response, new Route().add(JspFile.REGISTER.getPath()).get());
                return;
            }

            if (user.tryToRegisterUserName(username)) {
                HttpHelper.setSession(request, "Error_Message", "The username is already being used!");
                HttpHelper.incldue(request, response, new Route().add(JspFile.REGISTER.getPath()).get());
                return;
            }

            if (HttpHelper.getSession(request, "errorEmail") != null) {
                HttpHelper.setSession(request, "Error_Message", "There was an issue with your email registration.");
                HttpHelper.incldue(request, response, new Route().add(JspFile.REGISTER.getPath()).get());
            }

            if (HttpHelper.getSession(request, "errorUserName") != null) {
                HttpHelper.setSession(request, "Error_Message", "There was an issue with your username registration.");
                HttpHelper.incldue(request, response, new Route().add(JspFile.REGISTER.getPath()).get());
            }

            MyUser  newUser = user.register(name, username, password, email, phoneNumber, role);
            if (role.equalsIgnoreCase("Customer")) {
                user.registerCustomerInfo(newUser, customerAddress, companyName, website);
            } else if (role.equalsIgnoreCase("Jobseeker")) {
                int age =   Integer.parseInt(ageStr);
                user.registerJobseekerInfo(newUser, jobseekerAddress, hobbies, gender, skills, age);
            }
            HttpHelper.setSession(request, "Success_Message", "Account Create You May Login in now");
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
