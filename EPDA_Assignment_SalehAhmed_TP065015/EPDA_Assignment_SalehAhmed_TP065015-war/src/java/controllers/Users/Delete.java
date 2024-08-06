/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Users;

import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.CustomerInfo;
import model.EJB.CustomerInfoFacade;
import model.EJB.JobseekerInfoFacade;
import model.EJB.MyUserFacade;
import model.JobseekerInfo;
import model.MyJob;
import model.MyUser;

/**
 *
 * @author saleh
 */
@WebServlet(name = "Users.Delete", urlPatterns = {"/Users/Delete"})
public class Delete extends HttpServlet {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-warPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    @EJB
    private CustomerInfoFacade customerInfoFacade;

    @EJB
    private JobseekerInfoFacade jobseekerInfoFacade;
    
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
         * Check the Auth user
         */
        Gate.authorise(request, response, "Delete User");
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

                MyUser user = myUserFacade.find(id);

                if (user == null) {
                    HttpHelper.setSession(request, "Error_Message", "User not Found.");
                } else {
                    if (user.getRole().getName().equalsIgnoreCase("customer")) {
                        CustomerInfo customerInfo = customerInfoFacade.findByUserId(user.getId());
                        customerInfoFacade.remove(customerInfo);
                    } else if (user.getRole().getName().equalsIgnoreCase("jobseeker")) {
                        JobseekerInfo jobseekerInfo = jobseekerInfoFacade.findByUserId(user.getId());
                        jobseekerInfoFacade.remove(jobseekerInfo);
                    }
                    myUserFacade.remove(user);
                    HttpHelper.setSession(request, "Success_Message", "User deleted successfully.");
                }

            } catch (NumberFormatException e) {
                HttpHelper.setSession(request, "Error_Message", "Invalid User ID.");
                HttpHelper.back(request, response);
            }
            HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get());
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

    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }

}
