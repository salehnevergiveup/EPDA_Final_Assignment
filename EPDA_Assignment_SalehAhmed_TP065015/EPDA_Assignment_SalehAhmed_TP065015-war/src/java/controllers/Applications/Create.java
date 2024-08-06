/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Applications;

import controllers.enums.ApplicationStatus;
import controllers.enums.BaseRoute;
import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
import helpers.Auth;
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
import javax.validation.ValidationException;
import middlewares.Gate;
import model.Application;
import model.EJB.ApplicationFacade;
import model.EJB.MyJobFacade;
import model.MyJob;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class Create extends HttpServlet {

    @EJB
    private ApplicationFacade applicationFacade;

    @EJB
    private MyJobFacade myJobFacade;

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
        Gate.authorise(request, response, "Create Application");

        /**
         * Handle POST request
         */
        if (HttpHelper.CheckRequestType(request, "POST")) {
            handlePostRequest(request, response);
        }
    }

    private void handlePostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            /**
             * Collect Data
             */
            MyUser user = Auth.user(request);
            String selfDescription = HttpHelper.getParam(request, "selfDescription");
            long jobId = Long.parseLong(HttpHelper.getParam(request, "id"));
            MyJob job = myJobFacade.find(jobId);

            /**
             * Validate Data
             */
            if (job == null) {
                HttpHelper.setSession(request, "Validation_Error", "An I/O error occurred while fetching Applicaiton Job");
                HttpHelper.back(request, response);
                return;
            }

            /**
             * Check Application History
             */
            boolean hasApplication = applicationFacade.hasJobseekerApplied(user.getId(), job.getId());

            if (hasApplication) {
                HttpHelper.setSession(request, "Validation_Error", "You Have Applied For This Job Already");
                HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get());
                return;
            }

            /**
             * Store Application
             */
            HttpHelper.setSession(request, "Comment", "true");

            Application application = new Application(ApplicationStatus.PENDING.getStatus(), selfDescription, new Date(), job, user);
            applicationFacade.create(application);

            HttpHelper.setSession(request, "Success_Message", "Application compeleted successfully");

            HttpHelper.forward(request, response, new Route().add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.VIEW.getPath()).get() + "?id=" + application.getId());

        } catch (NumberFormatException e) {
            Logger.getLogger(controllers.Applications.Create.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            System.out.println("Error_Message An unexpected error occurred while Handling Commnets");
            HttpHelper.back(request, response);
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
