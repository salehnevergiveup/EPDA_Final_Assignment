/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Users;

import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.Comment;
import model.CustomerInfo;
import model.EJB.CommentFacade;
import model.EJB.CustomerInfoFacade;
import model.EJB.JobseekerInfoFacade;
import model.EJB.MyUserFacade;
import model.JobseekerInfo;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class View extends HttpServlet {

    @EJB
    private JobseekerInfoFacade jobseekerInfoFacade;

    @EJB
    private CustomerInfoFacade customerInfoFacade;

    @EJB
    private CommentFacade commentFacade;

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
        Gate.authorise(request, response, "Read User");
        try (PrintWriter out = response.getWriter()) {
            /**
             * Handle request
             */
            try {
                Long id = Long.parseLong(HttpHelper.getParam(request, "id"));

                /**
                 * Validate Data
                 */
                if (id == null) {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                    HttpHelper.back(request, response);
                    return;
                }

                MyUser user = myUserFacade.find(id);

                if (user == null) {
                    HttpHelper.setSession(request, "Validation_Error", "An I/O error occurred while fetching User Data");
                    HttpHelper.back(request, response);
                    return;
                }
                /**
                 * View the Data
                 */
                List<Comment> comments = (List<Comment>) commentFacade.findAll();
                
                //fetch the addational data for the jobseeker and the customer
                if(user.getRole().getName().equalsIgnoreCase("customer")) {  
                    CustomerInfo customerInfo = customerInfoFacade.findByUserId(user.getId()); 
                    request.setAttribute("customerInfo", customerInfo);
                }else if(user.getRole().getName().equalsIgnoreCase("jobseeker")){  
                    JobseekerInfo jobseekerInfo = jobseekerInfoFacade.findByUserId(user.getId());
                    request.setAttribute("jobseekerInfo", jobseekerInfo);
                }

                if (comments != null && !comments.isEmpty()) {
                    comments = comments.stream()
                            .filter(comment -> comment.getCustomer().getId().equals(user.getId()))
                            .collect(Collectors.toList());
                }

                request.setAttribute("user", user);
                request.setAttribute("comments", comments);
                HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.VIEW.getPath()).get());

            } catch (NumberFormatException e) {
                Logger.getLogger(controllers.Applications.Create.class.getName()).log(Level.SEVERE, null, e);
            } catch (Exception e) {
                System.out.println("Error_Message An unexpected error occurred while Fetching the User Data");
                HttpHelper.back(request, response);
                return;
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
