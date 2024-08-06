/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Applications;

import controllers.enums.BaseRoute;
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
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
import middlewares.Gate;
import model.Application;
import model.EJB.ApplicationFacade;

/**
 *
 * @author saleh
 */
public class Delete extends HttpServlet {

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
        Gate.authorise(request, response, "Delete Application");
        try (PrintWriter out = response.getWriter()) {
            try {
                /**
                 * Collect Data
                 */
                Long applicationId = Long.parseLong(HttpHelper.getParam(request, "id"));

                /**
                 * Validate Data Delete Date
                 */
                if (applicationId == null) {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                    HttpHelper.back(request, response);
                }

                Application application = applicationFacade.find(applicationId);

                if (application == null) {
                    HttpHelper.setSession(request, "Error_Message", "Application not found.");
                } else {
                    applicationFacade.remove(application);
                    HttpHelper.setSession(request, "Success_Message", "Application deleted successfully.");
                }

            } catch (NumberFormatException e) {
                System.out.println("somehing went wrong");
                HttpHelper.back(request, response);

            }

            HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get());

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
