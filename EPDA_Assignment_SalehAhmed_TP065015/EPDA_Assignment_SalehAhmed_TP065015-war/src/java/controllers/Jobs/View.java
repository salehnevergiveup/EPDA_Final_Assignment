/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Jobs;

import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import helpers.Auth;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.Comment;
import model.EJB.ApplicationFacade;
import model.EJB.CommentFacade;
import model.EJB.CustomerInfoFacade;
import model.EJB.MyJobFacade;
import model.MyJob;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class View extends HttpServlet {

    @EJB
    private ApplicationFacade applicationFacade;
    
    @EJB
    private CustomerInfoFacade customerInfoFacade;

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
        Gate.authorise(request, response, "Read Job");
        try (PrintWriter out = response.getWriter()) {
            /**
             * Handle POST request
             */

            /**
             * Collect Data
             */
            MyUser user = Auth.user(request);
            Long id = Long.parseLong(HttpHelper.getParam(request, "id"));
            try {
                /**
                 * Validate Data
                 */
                if (id == null) {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                    HttpHelper.back(request, response);
                    return;
                }

                MyJob job = myJobFacade.find(id);

                if (job == null) {
                    HttpHelper.setSession(request, "Validation_Error", "Job Not Found");
                    HttpHelper.forward(request, response, new Route().add(JspPackage.JOBS.getPath()).add(JspFile.INDEX.getPath()).get());
                    return;
                }

                /**
                 * View the Data
                 */
                
                request.setAttribute("job", job);
                request.setAttribute("companyName", customerInfoFacade.findByUserId(job.getCustomer().getId()).getCompanyName());
                HttpHelper.forward(request, response, new Route().add(JspPackage.JOBS.getPath()).add(JspFile.VIEW.getPath()).get());

            } catch (NumberFormatException e) {
                e.printStackTrace();
                HttpHelper.setSession(request, "Validation_Error", "Invalid job ID");
                HttpHelper.back(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                HttpHelper.setSession(request, "Validation_Error", "An unexpected error occurred");
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
