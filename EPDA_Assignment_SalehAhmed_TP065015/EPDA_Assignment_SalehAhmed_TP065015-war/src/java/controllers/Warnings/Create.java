/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Warnings;

import Service.Email.SendEmail;
import controllers.enums.AccountStatus;
import helpers.Auth;
import helpers.HttpHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.MyUserFacade;
import model.EJB.WarningFacade;
import model.MyUser;
import model.Warning;

/**
 *
 * @author saleh
 */
public class Create extends HttpServlet {

    @EJB
    private MyUserFacade myUserFacade;

    @EJB
    private WarningFacade warningFacade;

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
        /**
         * Check the Auth user based on the permission
         */
        Gate.authorise(request, response, "Create Warning");

        /**
         * Handle request
         */
        try (PrintWriter out = response.getWriter()) {
            try {
                /**
                 * Data collection
                 */
                MyUser management = Auth.user(request);
                Long jobseekerId = Long.parseLong(HttpHelper.getParam(request, "id"));

                /**
                 * Validation of the data
                 */
                if (jobseekerId == null) {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                    HttpHelper.back(request, response);
                }

                MyUser jobseeker = myUserFacade.find(jobseekerId);

                if (jobseeker == null) {
                    HttpHelper.setSession(request, "Validation_Error", "An I/O error occurred while fetching users data");
                    HttpHelper.back(request, response);
                    return;
                }

                if (jobseeker.getWarningCounter() >= 3) {
                    HttpHelper.setSession(request, "Validation_Error", "The account has already been suspended.");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Handle the warning logic
                 */
                int warningCount = jobseeker.getWarningCounter() + 1;
                jobseeker.setWarningCounter(warningCount);
                if (warningCount >= 3) {
                    jobseeker.setStatus(AccountStatus.SUSPENDED.getStatus());
                    myUserFacade.edit(jobseeker);

                    new SendEmail()
                            .to(jobseeker.getEmail())
                            .subject("Account Suspended")
                            .salute()
                            .content("Your account has been suspended due to receiving 3 warnings.")
                            .content("If you believe this is a mistake, please contact support.")
                            .signature()
                            .send();
                } else {
                    myUserFacade.edit(jobseeker);

                    new SendEmail()
                            .to(jobseeker.getEmail())
                            .subject("Warning Issued")
                            .salute()
                            .content("You have received a warning. Your current warning count is: " + warningCount + ".")
                            .content("Your account will be suspended after 3 warnings.")
                            .signature()
                            .send();
                }

                /**
                 * Create Warning
                 */
                warningFacade.create(new Warning(new Date(), management, jobseeker));
                HttpHelper.setSession(request, "Success_Message", "Warning sent successfully");
                HttpHelper.back(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                HttpHelper.setSession(request, "Error", "An error occurred while fetching customers");
                HttpHelper.back(request, response);
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
