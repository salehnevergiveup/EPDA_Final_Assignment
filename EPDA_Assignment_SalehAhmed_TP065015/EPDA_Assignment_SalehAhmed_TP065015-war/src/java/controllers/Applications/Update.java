/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Applications;

import controllers.enums.ApplicationStatus;
import controllers.enums.BaseRoute;
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.Application;
import model.EJB.ApplicationFacade;

/**
 *
 * @author saleh
 */
public class Update extends HttpServlet {

    @EJB
    private ApplicationFacade applicationFacade;

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
         * Check the Auth user
         */
        Gate.authorise(request, response, "Update Application");
        try (PrintWriter out = response.getWriter()) {

            /**
             * Handle request
             */
            try {
                /**
                 * Collect Data
                 */
                String applicationId = HttpHelper.getParam(request, "id");
                String status = HttpHelper.getParam(request, "Type");
                /**
                 * Validate Data
                 */
                if (applicationId == null || status == null) {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                    HttpHelper.back(request, response);
                    return;
                }

                Application application = applicationFacade.find(Long.parseLong(applicationId));

                if (application == null) {
                    HttpHelper.setSession(request, "Validation_Error", "An I/O error occurred while fetching Applicaiton");
                    HttpHelper.back(request, response);
                    return;
                }

                if (ApplicationStatus.DELETED.getStatus().equals(status)
                        || ApplicationStatus.APPROVED.getStatus().equals(status)
                        || ApplicationStatus.PENDING.getStatus().equals(status)
                        || ApplicationStatus.REJECTED.getStatus().equals(status)
                        || ApplicationStatus.INTERVIEW.getStatus().equals(status)) {
                    application.setStatus(status);
                    application.setUpdatedAt(new Date());
                    applicationFacade.edit(application);
                } else {
                    HttpHelper.setSession(request, "Validation_Error", "Status Not Found");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Updating the Data
                 */
                String message = ApplicationStatus.DELETED.getStatus().equals(status) ? "Application has been deleted successfully."
                        : ApplicationStatus.INTERVIEW.getStatus().equals(status) ? "We will contact you soon for an interview."
                        : "Application Status Updated";

                HttpHelper.setSession(request, "Success_Message", message);
                HttpHelper.forward(request, response, new Route()
                        .add(ServletPackage.APPLICATIONS.getPath())
                        .add(ServletFile.INDEX.getPath())
                        .get());

            } catch (NumberFormatException e) {
                Logger.getLogger(controllers.Applications.Create.class.getName()).log(Level.SEVERE, null, e);
            } catch (Exception e) {
                System.out.println("Error_Message An unexpected error occurred while Updateing the Applicaiton status");
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
