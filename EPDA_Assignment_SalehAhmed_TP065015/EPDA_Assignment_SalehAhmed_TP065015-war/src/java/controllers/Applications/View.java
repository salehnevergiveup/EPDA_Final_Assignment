/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Applications;

import controllers.enums.BaseRoute;
import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import controllers.enums.ServletPackage;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
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
public class View extends HttpServlet {

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
        Gate.authorise(request, response, "Read Application");
        try (PrintWriter out = response.getWriter()) {
            /**
             * Handle POST request
             */
            try {
                /**
                 * Collect Data
                 */
                Long applicationId = Long.parseLong(HttpHelper.getParam(request, "id"));

                /**
                 * Validate Data
                 */
                if (applicationId == null) {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                    HttpHelper.back(request, response);
                    return;
                }

                Application application = applicationFacade.find(applicationId);

                if (application == null) {
                    HttpHelper.setSession(request, "Validation_Error", "An I/O error occurred while fetching Applicaiton");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * View the Data
                 */
                request.setAttribute("application", application);
                HttpHelper.incldue(request, response, new Route()
                        .add(JspPackage.APPLICATIONS.getPath())
                        .add(JspFile.VIEW.getPath()).get());

            } catch (NumberFormatException e) {
                Logger.getLogger(controllers.Applications.Create.class.getName()).log(Level.SEVERE, null, e);
            } catch (Exception e) {
                System.out.println("Error_Message An unexpected error occurred while Fetching the Applicaiton status");
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
