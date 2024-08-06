/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Reports;

import controllers.enums.ReportType;
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import model.EJB.ReportFacade;
import model.Report;

/**
 *
 * @author saleh
 */
public class Create extends HttpServlet {

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
         * Check the Auth user based on the permission
         */
        Gate.authorise(request, response, "Create report");
        
        /**
         * Handle the Get request
         */
        if (HttpHelper.CheckRequestType(request, "POST")) {
           try {
                 /**
                  * Data collection
                 */
                 String name = HttpHelper.getParam(request, "type");  
                 String startDateString = HttpHelper.getParam(request, "start_date");  
                 String endDateString = HttpHelper.getParam(request,"end_date");  
                 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                 /**
                  * Data Validation
                  */
                if(name.isEmpty()) {  
                    HttpHelper.setSession(request, "Validation_Error", "An I/O error occurred while fetching users data");
                     HttpHelper.back(request, response);
                     return;
                }
                 
                if(startDateString.isEmpty()){  
                    HttpHelper.setSession(request, "Validation_Error", "The start date field is required.");
                     HttpHelper.back(request, response);
                     return;
                }

                if(endDateString.isEmpty()) { 
                     HttpHelper.setSession(request, "Validation_Error", "The end date field is required.");
                     HttpHelper.back(request, response);
                     return;
                }
                

                if(!(name.equals(ReportType.COMMENT_FEEDBACK_ANALYSIS.getType())
                      || name.equals(ReportType.CUSTOMERS.getType())
                      ||name.equals(ReportType.APPLICATIONS.getType())
                      ||name.equals(ReportType.JOBS.getType())
                      ||name.equals(ReportType.JOBSEEKERS.getType())
                      ||name.equals(ReportType.WARNINGS.getType())
                      ||name.equals(ReportType.GenderAndAge.getType())
                     )){ 
                     HttpHelper.setSession(request, "Validation_Error", "Invalid report type specified.");
                     HttpHelper.back(request, response);
                     return;
                }
                 
                Date startDate =  dateFormat.parse(startDateString);
                Date endDate = dateFormat.parse(endDateString); 
                

                if(startDate.after(endDate)) {  
                     HttpHelper.setSession(request, "Validation_Error", "The start date cannot be after the end date.");
                     HttpHelper.back(request, response);
                     return; 
                }
                 
                if(startDate.after(new Date())) { 
                    HttpHelper.setSession(request, "Validation_Error", "The date cannot be after Today date.");
                    HttpHelper.back(request, response);
                    return;
                }
                /**
                 * Create Report
                */
                Report report = new Report(name, startDate, endDate);
                reportFacade.create(report);
                HttpHelper.setSession(request, "Success_Message", "Report Created successfully");
                HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.REPORTS.getPath()).add(ServletFile.VIEW.getPath()).get()+ "?id="+ report.getId());
                 
                 
             }catch (Exception e) {
                e.printStackTrace();
                HttpHelper.setSession(request, "Error", "An error occurred while fetching customers");
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
