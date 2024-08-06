/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Feedbacks;

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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.FeedbackFacade;
import model.EJB.MyUserFacade;
import model.Feedback;
import model.MyUser;

/**
 *
 * @author saleh
 */
@WebServlet(name = "Feedbacks.Create", urlPatterns = {"/Feedbacks/Create"})
public class Create extends HttpServlet {

    @EJB
    private MyUserFacade myUserFacade;

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

        /**
         * Check the Auth user
         */
        Gate.authorise(request, response, "Create Feedback");

        try (PrintWriter out = response.getWriter()) {
            /**
             * Handle request
             */
            try {
                /**
                 * Collect Data
                 */
                MyUser user = Auth.user(request);
                Long jobSeekerId = Long.parseLong(HttpHelper.getParam(request, "id"));
                String content = HttpHelper.getParam(request, "feedbackContent");
                String type = HttpHelper.getParam(request, "type");

                /**
                 * Validate Data
                 */
                if (jobSeekerId == null || content.isEmpty() || type.isEmpty()) {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                    HttpHelper.back(request, response);                   
                    return;
                }

                MyUser jobSeeker = myUserFacade.find(jobSeekerId);
                if (jobSeeker == null) {
                    HttpHelper.setSession(request, "Validation_Error", "An I/O error occurred while fetching jobSeekerUser");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Store Data
                 */
                HttpHelper.setSession(request, "Success_Message", "Feedback Submited successfully");
                feedbackFacade.create(new Feedback(type, content, new Date(), jobSeeker, user));
                HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.FEEDBACK.getPath()).add(ServletFile.INDEX.getPath()).get());
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
