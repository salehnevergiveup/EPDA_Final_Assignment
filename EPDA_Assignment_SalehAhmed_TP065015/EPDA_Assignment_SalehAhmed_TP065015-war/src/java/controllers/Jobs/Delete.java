package controllers.Jobs;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
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
import model.EJB.MyJobFacade;
import model.MyJob;

/**
 *
 * @author saleh
 */
public class Delete extends HttpServlet {

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
        Gate.authorise(request, response, "Delete Job");

        try (PrintWriter out = response.getWriter()) {
            /**
             * Handle request
             */
            try {
                /**
                 * Collect Data
                 */
                Long id = Long.parseLong(HttpHelper.getParam(request, "id"));

                /**
                 * Validate Data Delete Data
                 */
                if (id == null) {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                    HttpHelper.back(request, response);
                }

                MyJob job = myJobFacade.find(id);

                if (job == null) {
                    HttpHelper.setSession(request, "Error_Message", "Job not found.");
                } else {
                    myJobFacade.remove(job);
                    HttpHelper.setSession(request, "Success_Message", "Job deleted successfully.");
                }

            } catch (NumberFormatException e) {
                HttpHelper.setSession(request, "Error_Message", "Invalid job ID.");
                HttpHelper.back(request, response);
            }
            HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get());
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
