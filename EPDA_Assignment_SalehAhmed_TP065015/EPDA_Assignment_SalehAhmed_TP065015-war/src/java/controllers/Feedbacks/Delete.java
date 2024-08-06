/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Feedbacks;

import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.FeedbackFacade;
import model.Feedback;

/**
 *
 * @author saleh
 */
@WebServlet(name = "Feedbacks.Delete", urlPatterns = {"/Feedbacks/Delete"})
public class Delete extends HttpServlet {

    @EJB
    private FeedbackFacade feedbackFacade;

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
            /**
             * Handle request
             */
            try {
                /**
                 * Collect Data
                 */
                Long feedbackId = Long.parseLong(HttpHelper.getParam(request, "id"));

                /**
                 * Validate Data Delete Date
                 */
                if (feedbackId == null) {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                    HttpHelper.back(request, response);
                    return;
                }

                Feedback feedback = feedbackFacade.find(feedbackId);
                if (feedback == null) {
                    HttpHelper.setSession(request, "Error_Message", "Feedback not found.");
                } else {
                    feedbackFacade.remove(feedback);
                    HttpHelper.setSession(request, "Success_Message", "Feedabck deleted successfully.");
                }

            } catch (NumberFormatException e) {
                HttpHelper.setSession(request, "Error_Message", "Invalid job ID.");
                HttpHelper.back(request, response);
            }

            HttpHelper.forward(request, response, new Route().add(ServletPackage.FEEDBACK.getPath()).add(ServletFile.INDEX.getPath()).get());
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
