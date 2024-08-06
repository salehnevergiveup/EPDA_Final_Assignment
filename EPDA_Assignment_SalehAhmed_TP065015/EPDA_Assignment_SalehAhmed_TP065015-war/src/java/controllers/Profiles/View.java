/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Profiles;

import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import helpers.Auth;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Guest;
import model.CustomerInfo;
import model.EJB.CustomerInfoFacade;
import model.EJB.JobseekerInfoFacade;
import model.JobseekerInfo;
import model.MyUser;

/**
 *
 * @author saleh
 */
@WebServlet(name = "Profiles.View", urlPatterns = {"/Profiles/View"})
public class View extends HttpServlet {

    @EJB
    private JobseekerInfoFacade jobseekerInfoFacade;

    @EJB
    private CustomerInfoFacade customerInfoFacade;

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
            Guest.authorise(request, response);

            MyUser user = Auth.user(request);
            if (user.getRole().getName().equalsIgnoreCase("Customer")) {
                CustomerInfo customerInfo = customerInfoFacade.findByUserId(user.getId());
                request.setAttribute("customerInfo", customerInfo);
            } else if (user.getRole().getName().equalsIgnoreCase("Jobseeker")) {
                JobseekerInfo jobseekerInfo = jobseekerInfoFacade.findByUserId(user.getId());
                request.setAttribute("jobseekerInfo", jobseekerInfo);
            }

            HttpHelper.forward(request, response, new Route().add(JspPackage.PROFILE.getPath()).add(JspFile.VIEW.getPath()).get());

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
