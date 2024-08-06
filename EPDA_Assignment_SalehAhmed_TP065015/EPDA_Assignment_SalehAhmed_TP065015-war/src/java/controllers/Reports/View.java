/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Reports;

import Service.Reports.DTO.ReportsDTO;
import Service.Reports.Factory.ReportFactory;
import Service.Reports.Factory.Reports;
import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.ApplicationFacade;
import model.EJB.CommentFacade;
import model.EJB.FeedbackFacade;
import model.EJB.JobseekerInfoFacade;
import model.EJB.MyJobFacade;
import model.EJB.MyUserFacade;
import model.EJB.ReportFacade;
import model.EJB.WarningFacade;
import model.Report;

/**
 *
 * @author saleh
 */
public class View extends HttpServlet {

    @EJB
    private JobseekerInfoFacade jobseekerInfoFacade;

    @EJB
    private ReportFacade reportFacade;

    @EJB
    private WarningFacade warningFacade;

    @EJB
    private MyJobFacade myJobFacade;

    @EJB
    private FeedbackFacade feedbackFacade;

    @EJB
    private CommentFacade commentFacade;

    @EJB
    private ApplicationFacade applicationFacade;

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
        Gate.authorise(request, response, "Read Report");
        try (PrintWriter out = response.getWriter()) {
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

                Report report = reportFacade.find(id);
                if (report == null) {
                    HttpHelper.setSession(request, "Validation_Error", "Repoprt Not Found");
                    HttpHelper.back(request, response);
                    return;
                }
                /**
                 * Extract the data
                 */

                ReportsDTO data = new ReportFactory(myUserFacade,
                        applicationFacade,
                        commentFacade,
                        feedbackFacade,
                        myJobFacade,
                        warningFacade,
                        jobseekerInfoFacade)
                        .generateReport(
                                report.getStartDate(),
                                report.getEndDate(),
                                report.getName());

                /**
                 * View the extracted data //
                 */
                System.out.println("ChartLabels: " + data.getChartLabels());
                System.out.println("ChartData: " + data.getChartData());
                System.out.println("SecondaryChartLabels: " + data.getChartLabels());
                System.out.println("SecondaryChartData: " + data.getChartData());
                System.out.println("TableCategory: " + data.getCountByCategory());
                System.out.println("TableDetails: " + data.getDetails());
                System.out.println("reportType: " + report.getName());

//            request.setAttribute("ChartLabels", data.getChartLabels());
//            request.setAttribute("ChartData", data.getChartData());
//            request.setAttribute("SecondaryChartLabels", data.getChartLabels());
//            request.setAttribute("SecondaryChartData", data.getSecondaryChartData());
//            request.setAttribute("TableCategory", data.getCountByCategory());
//            request.setAttribute("TableDetails", data.getDetails());
//            request.setAttribute("reportType", report.getName());
                System.out.println("test 3");
                request.setAttribute("reportData", data);
                request.setAttribute("reportType", report.getName());
                HttpHelper.forward(request, response, new Route().add(JspPackage.REPORTS.getPath()).add(JspFile.VIEW.getPath()).get());

            } catch (NumberFormatException e) {
                Logger.getLogger(controllers.Applications.Create.class.getName()).log(Level.SEVERE, null, e);
            } catch (Exception e) {
                System.out.println("Error_Message An unexpected error occurred while Fetching the User Data");
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
