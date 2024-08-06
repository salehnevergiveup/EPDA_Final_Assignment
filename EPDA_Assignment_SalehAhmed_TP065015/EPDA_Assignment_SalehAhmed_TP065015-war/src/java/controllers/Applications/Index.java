/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Applications;

import controllers.enums.ApplicationStatus;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.Application;
import model.EJB.ApplicationFacade;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class Index extends HttpServlet {

    @EJB
    private ApplicationFacade applicationFacade;

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
        Gate.authorise(request, response, "Read Application");
        try (PrintWriter out = response.getWriter()) {
            try {
                MyUser user = Auth.user(request);

                List<Application> applications = this.applicationFacade.findAll();
                String type = HttpHelper.getParam(request, "type");

                /**
                 * Check Applications
                 */
                if (applications == null) {
                    HttpHelper.setSession(request, "Error_Message", "An I/O error occurred while fetching Applications");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Extract the Data Needed for The Table
                 */
                String role = user.getRole().getName();
                List<Map<String, String>> allApplications = new ArrayList<>();

                if ("Customer".equals(role)) {
                    applications = applicationFacade.findByCustomerId(user.getId());
                } else if ("Jobseeker".equals(role)) {
                    applications = applicationFacade.findByJobSeekerId(user.getId());
                }

                /**
                 * Filter data based on the status
                 */
                if (type != null && !type.isEmpty() && !type.equals("All")) {
                    String type2 = type;
                    applications = applications.stream().filter(app -> app.getStatus().equalsIgnoreCase(type2) && !app.getStatus().equalsIgnoreCase(ApplicationStatus.DELETED.getStatus())).collect(Collectors.toList());
                } else {
                    type = "All";
                    applications = applications.stream().filter(app -> !app.getStatus().equalsIgnoreCase(ApplicationStatus.DELETED.getStatus())).collect(Collectors.toList());
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                List<Map<String, String>> fillApplications = new ArrayList<>();
                applications.forEach(app -> {
                    Map<String, String> applicationMap = new HashMap<>();
                    applicationMap.put("id", app.getId().toString());
                    applicationMap.put("Job_Seeker", app.getJobSeeker().getName());
                    applicationMap.put("Job_Title", app.getJob().getTitle());
                    applicationMap.put("Created_Date", dateFormat.format(app.getCreatedAt()));
                    applicationMap.put("Status", app.getStatus());
                    applicationMap.put("JobSeekeer", app.getJobSeeker().getId().toString());
                    applicationMap.put("Job", app.getJob().getId().toString());
                    fillApplications.add(applicationMap);
                });

                allApplications = fillApplications;

                /**
                 * Handling searching
                 */
                if (HttpHelper.CheckRequestType(request, "POST")) {
                    String query = HttpHelper.getParam(request, "query");
                    if (query != null) {
                        List<Map<String, String>> filteredApplications = allApplications;
                        filteredApplications = SearchHelper.search(filteredApplications, query);
                        if (filteredApplications != null) {
                            allApplications = filteredApplications;
                        }
                    }
                }

                request.setAttribute("applications", allApplications);
                request.setAttribute("type", type);
                HttpHelper.incldue(request, response, new Route().add(JspPackage.APPLICATIONS.getPath()).add(JspFile.INDEX.getPath()).get());

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error_Message An unexpected error occurred while Handling Applications");
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
