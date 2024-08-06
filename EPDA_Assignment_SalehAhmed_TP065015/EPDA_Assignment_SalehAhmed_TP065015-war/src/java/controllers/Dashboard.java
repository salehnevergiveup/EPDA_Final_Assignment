/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.EJB.ApplicationFacade;
import model.EJB.CommentFacade;
import model.EJB.FeedbackFacade;
import model.EJB.MyJobFacade;
import model.EJB.MyUserFacade;
import model.EJB.ReportFacade;
import model.EJB.WarningFacade;

/**
 *
 * @author saleh
 */
public class Dashboard extends HttpServlet {

    @EJB
    private ReportFacade reportFacade;

    @EJB
    private MyUserFacade myUserFacade;

    @EJB
    public ApplicationFacade applcationFacade;

    @EJB
    public CommentFacade commentFacade;

    @EJB
    public FeedbackFacade feedbackFacade;

    @EJB
    public MyJobFacade myJobFacade;
    @EJB
    public WarningFacade warningFacade;

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
            
            // Fetch the required data using the facades
            int jobCount = myJobFacade.count();
            int feedbackCount = feedbackFacade.count();
            int commentCount = commentFacade.count();
            int warningCount = warningFacade.count();
            int reportCount = reportFacade.count();
            int applicationCount = applcationFacade.count();
            int userCount = myUserFacade.count();
            int managementCount = myUserFacade.findByRole("Management").size();
            int jobseekerCount = myUserFacade.findByRole("Jobseeker").size();
            int customerCount = myUserFacade.findByRole("Customer").size();

            // Set the data as request attributes
            request.setAttribute("jobCount", jobCount);
            request.setAttribute("feedbackCount", feedbackCount);
            request.setAttribute("commentCount", commentCount);
            request.setAttribute("warningCount", warningCount);
            request.setAttribute("reportCount", reportCount);
            request.setAttribute("applicationCount", applicationCount);
            request.setAttribute("userCount", userCount);
            request.setAttribute("managementCount", managementCount);
            request.setAttribute("jobseekerCount", jobseekerCount);
            request.setAttribute("customerCount", customerCount);
       
            HttpHelper.forward(request, response,  new Route().add(JspFile.DASHBOARD.getPath()).get());

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
