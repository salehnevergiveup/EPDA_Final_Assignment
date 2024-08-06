/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Comments;

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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import middlewares.Gate;
import model.Application;
import model.Comment;
import model.EJB.ApplicationFacade;
import model.EJB.CommentFacade;
import model.EJB.MyUserFacade;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class Create extends HttpServlet {

    @EJB
    private ApplicationFacade applicationFacade;

    @EJB
    private MyUserFacade myUserFacade;

    @EJB
    private CommentFacade commentFacade;

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
        Gate.authorise(request, response, "Create Comment");

        try (PrintWriter out = response.getWriter()) {
            /**
             * Handle request
             */
            try {
                /**
                 * Collect Data
                 */
                MyUser user = Auth.user(request);
                String content = HttpHelper.getParam(request, "commentContent");
                int rating = Integer.parseInt(HttpHelper.getParam(request, "rating"));
                Long customerId = Long.parseLong(HttpHelper.getParam(request, "id"));
                String appString = HttpHelper.getParam(request, "application");
                MyUser customer = myUserFacade.find(customerId);

                /**
                 * Validate Data
                 */
                if (customer == null) {
                    HttpHelper.setSession(request, "Validation_Error", "An I/O error occurred while fetching Customer Data");
                    HttpHelper.back(request, response);
                    return; 
                }

                if (content == null || content.isEmpty()) {
                    HttpHelper.setSession(request, "Validation_Error", "Comment Conent is Required");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Store Data
                 */
                HttpHelper.setSession(request, "Success_Message", "Commnet compeleted successfully");
                Comment comment = new Comment(content, rating, new Date(), null, customer, user);
                commentFacade.create(comment);

                /**
                 * Handle Comment creation from the Notifications
                 */
                if (appString != "") {
                    Long app = Long.parseLong(appString);
                    Application application = applicationFacade.find(app);
                    if (application.getJobSeeker().getId().toString().equals(user.getId().toString())) {
                        HttpHelper.setSession(request, "Success_Message", "Thanks For your Commnets");
                        HttpHelper.redirectTo(request, response, new Route()
                                .add(ServletPackage.APPLICATIONS.getPath())
                                .add(ServletFile.VIEW.getPath()).get() + "?id=" + app);
                        return;
                    } else {
                        HttpHelper.incldue(request, response, new Route()
                                .add(JspPackage.INCLUDES.getPath())
                                .add(JspFile.ERROR_403.getPath()).get());
                        return;
                    }
                }

                HttpHelper.back(request, response);

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
