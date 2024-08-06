/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Jobs;

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
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.ApplicationFacade;
import model.EJB.CustomerInfoFacade;
import model.EJB.MyJobFacade;
import model.MyJob;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class Index extends HttpServlet {

    @EJB
    private CustomerInfoFacade customerInfoFacade;

    @EJB
    private ApplicationFacade applicationFacade;

    @EJB
    private MyJobFacade myJobFacade;

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
        Gate.authorise(request, response, "Read Job");
        try (PrintWriter out = response.getWriter()) {
            try {
                MyUser user = Auth.user(request);

                List<MyJob> myJobs = (List<MyJob>) myJobFacade.findAll();

                /**
                 * Check Jobs
                 */
                if (myJobs == null) {
                    HttpHelper.setSession(request, "Error_Message", "An I/O error occurred while fetching jobs");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Extract the Data Needed for The Table
                 */
                String role = user.getRole().getName();
                List<Map<String, String>> jobs = new ArrayList<>();

                if ("Customer".equals(role)) {
                    myJobs = myJobFacade.findByCustomerId(user.getId());
                } else if ("Jobseeker".equals(role)) {
                    myJobs = myJobs.stream()
                            .filter(job -> job.getStatus() == true && !applicationFacade.hasJobseekerApplied(user.getId(), job.getId()))
                            .collect(Collectors.toList());
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                myJobs.forEach(job -> {
                    Map<String, String> jobMap = new HashMap<>();
                    jobMap.put("id", job.getId().toString());
                    jobMap.put("Title", job.getTitle());
                    jobMap.put("Status", job.getStatus() ? "Active" : "Inactive");
                    jobMap.put("Created_Date", dateFormat.format(job.getCreatedAt()).toString());
                    jobMap.put("Updated_Date", job.getUpdatedAt() == null ? "None" : dateFormat.format(job.getUpdatedAt()).toString());
                    jobMap.put("Due_Date", dateFormat.format(job.getDueDate()).toString());
                    jobMap.put("Company_Name", customerInfoFacade.findByUserId(job.getCustomer().getId()).getCompanyName());
                    jobs.add(jobMap);
                });

                /**
                 * Handling searching
                 */ //  this is may come form update page or creat page 
                if (HttpHelper.CheckRequestType(request, "POST")) {
                    String query = HttpHelper.getParam(request, "query");
                    if (query != null) {
                        List<Map<String, String>> filtredJob = jobs;
                        filtredJob = SearchHelper.search(filtredJob, query);
                        if (filtredJob != null) {
                            request.setAttribute("jobs", filtredJob);
                            HttpHelper.forward(request, response, new Route().add(JspPackage.JOBS.getPath()).add(JspFile.INDEX.getPath()).get());
                        }
                    }
                }

                request.setAttribute("jobs", jobs);

                HttpHelper.forward(request, response, new Route().add(JspPackage.JOBS.getPath()).add(JspFile.INDEX.getPath()).get());

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
