/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Reports;

import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import helpers.Auth;
import helpers.HttpHelper;
import helpers.Route;
import helpers.SearchHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.ReportFacade;
import model.MyUser;
import model.Report;

/**
 *
 * @author saleh
 */
public class Index extends HttpServlet {

    @EJB
    private ReportFacade reportFacade;

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
         * Check the Auth User
         */
        Gate.authorise(request, response, "Read Report");
        try (PrintWriter out = response.getWriter()) {
            /**
             * Handle request
             */
            try {
                MyUser user = Auth.user(request);

                List<Report> reports = (List<Report>) reportFacade.findAll();

                /**
                 * Check Jobs
                 */
                if (reports == null) {
                    HttpHelper.setSession(request, "Error_Message", "An I/O error occurred while fetching Reports");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Extract the Data Needed for The Table
                 */
                List<Map<String, String>> tableReports = new ArrayList<>();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                reports.forEach(report -> {
                    Map<String, String> reportMap = new HashMap<>();
                    reportMap.put("id", report.getId().toString());
                    reportMap.put("Name", report.getName());
                    reportMap.put("Start_Date", dateFormat.format(report.getStartDate()).toString());
                    reportMap.put("End_Date", dateFormat.format(report.getEndDate()).toString());
                    tableReports.add(reportMap);
                });

                /**
                 * Handling searching
                 */
                if (HttpHelper.CheckRequestType(request, "POST")) {
                    String query = HttpHelper.getParam(request, "query");
                    if (query != null) {
                        List<Map<String, String>> filtredReport = tableReports;
                        filtredReport = SearchHelper.search(filtredReport, query);
                        if (filtredReport != null) {
                            request.setAttribute("reports", filtredReport);
                            HttpHelper.forward(request, response, new Route().add(JspPackage.REPORTS.getPath()).add(JspFile.INDEX.getPath()).get());
                        }
                    }
                }

                /**
                 * Display all reports
                 */
                request.setAttribute("reports", tableReports);

                HttpHelper.forward(request, response, new Route().add(JspPackage.REPORTS.getPath()).add(JspFile.INDEX.getPath()).get());

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error_Message An unexpected error occurred while Handling jobs");
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
