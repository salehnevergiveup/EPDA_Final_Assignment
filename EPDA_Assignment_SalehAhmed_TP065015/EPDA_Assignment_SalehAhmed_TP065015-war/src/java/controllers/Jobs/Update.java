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
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.MyJobFacade;
import model.MyJob;

/**
 *
 * @author saleh
 */
public class Update extends HttpServlet {

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
         * check the auth user permission
         */
        Gate.authorise(request, response, "Update Job");
        try (PrintWriter out = response.getWriter()) {
            /**
             * Handel Get request
             */
            if (HttpHelper.CheckRequestType(request, "GET")) {
                try {
                    Long id = Long.parseLong(HttpHelper.getParam(request, "id"));
                    if (id == null) {
                        HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                        HttpHelper.back(request, response);
                        return;
                    }

                    MyJob job = myJobFacade.find(id);

                    if (job == null) {
                        HttpHelper.setSession(request, "Validation_Error", "An I/O error occurred while fetching Job");
                        HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get());
                        return;
                    }
                    request.setAttribute("job", job);
                    HttpHelper.forward(request, response, new Route().add(JspPackage.JOBS.getPath()).add(JspFile.UPDATE.getPath()).get());

                } catch (Exception e) {
                    e.printStackTrace();
                    HttpHelper.setSession(request, "Validation_Error", "Wrong Datatype");
                    HttpHelper.back(request, response);
                }
            }

            /**
             * Handle the Post request
             */
            if (HttpHelper.CheckRequestType(request, "POST")) {
                try {
                    /**
                     * Collect Data
                     */
                    Long id = Long.parseLong(HttpHelper.getParam(request, "id"));
                    String title = HttpHelper.getParam(request, "title");
                    String statusString = HttpHelper.getParam(request, "status");
                    String dueDateString = HttpHelper.getParam(request, "dueDate");
                    String description = HttpHelper.getParam(request, "text");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    /**
                     * Validate Data Updating Data
                     */
                    if (id == null) {
                        HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                        HttpHelper.back(request, response);
                        return;
                    }

                    MyJob job = myJobFacade.find(id);

                    if (job == null) {
                        HttpHelper.setSession(request, "Validation_Error", "An I/O error occurred while fetching Job");
                        HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get());
                        return;
                    }

                    if (title != null && !title.isEmpty()) {
                        job.setTitle(title);
                    }

                    if (dueDateString != null && !dueDateString.isEmpty()) {
                        LocalDate localDate = LocalDate.parse(dueDateString, formatter);
                        Date dueDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        if (dueDate.before(new Date())) {
                            HttpHelper.setSession(request, "Validation_Error", "Due date is in the past");
                            HttpHelper.back(request, response);
                            return;
                        }
                        job.setDueDate(dueDate);
                    }

                    if (statusString != null) {
                        boolean status = statusString.equals("Active");
                        job.setStatus(status);
                    }

                    if (description != null && !description.isEmpty()) {
                        job.setDescription(description);
                    }

                    /**
                     * Store
                     */
                    job.setUpdatedAt(new Date());
                    HttpHelper.setSession(request, "Success_Message", "Job Updated successfully");
                    myJobFacade.edit(job);
                    HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.JOBS.getPath()).add(ServletFile.VIEW.getPath()).get() + "?id=" + job.getId());

                } catch (NumberFormatException e) {
                    Logger.getLogger(controllers.Applications.Create.class.getName()).log(Level.SEVERE, null, e);
                } catch (Exception e) {
                    System.out.println("Error_Message An unexpected error occurred while Updateing the Applicaiton status");
                    HttpHelper.back(request, response);
                }
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
