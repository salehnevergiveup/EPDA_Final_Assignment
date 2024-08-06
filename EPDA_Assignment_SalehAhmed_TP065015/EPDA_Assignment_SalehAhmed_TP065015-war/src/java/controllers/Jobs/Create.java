/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Jobs;

import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
import helpers.Auth;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.MyJobFacade;
import model.EJB.MyUserFacade;
import model.MyJob;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class Create extends HttpServlet {

    @EJB
    private MyJobFacade myJobFacade;

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

        /**
         * Check the Auth user based on the permission
         */
        Gate.authorise(request, response, "Create Job");

        /**
         * Handle Post request
         */
        try (PrintWriter out = response.getWriter()) {
            try {
                /**
                 * Collect Data
                 */
                MyUser user = Auth.user(request);
                String title = HttpHelper.getParam(request, "title");
                String statusString = HttpHelper.getParam(request, "status");
                String dueDateString = HttpHelper.getParam(request, "dueDate");
                String description = HttpHelper.getParam(request, "text");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                /**
                 * Validate Data
                 */
                if (title.isEmpty() || description.isEmpty() || dueDateString.isEmpty() || statusString.isEmpty()) {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.JOBS.getPath()).add(JspFile.CREATE.getPath()).get());
                    return;
                }

                LocalDate localDate = LocalDate.parse(dueDateString, formatter);
                Date dueDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                if (dueDate.before(new Date())) {
                    HttpHelper.setSession(request, "Validation_Error", "Due date is in the past");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.JOBS.getPath()).add(JspFile.CREATE.getPath()).get());
                    return;
                }

                /**
                 * Store Data
                 */
                HttpHelper.setSession(request, "Success_Message", "Job compeleted successfully");
                MyJob job = new MyJob(title, description, statusString == "Active", new Date(), dueDate, user);
                myJobFacade.create(job);
                HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get());

            } catch (NumberFormatException e) {
                Logger.getLogger(controllers.Applications.Create.class.getName()).log(Level.SEVERE, null, e);
            } catch (Exception e) {
                System.out.println("Error_Message An unexpected error occurred while Handling Commnets");
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
