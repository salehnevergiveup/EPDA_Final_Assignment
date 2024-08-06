/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Warnings;

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
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.WarningFacade;
import model.MyUser;
import model.Warning;

/**
 *
 * @author saleh
 */
public class Index extends HttpServlet {

    @EJB
    private WarningFacade warningFacade;

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
        Gate.authorise(request, response, "Read Warning");
        try (PrintWriter out = response.getWriter()) {
            try {
                MyUser user = Auth.user(request);

                List<Warning> warnings = warningFacade.findAll();

                /**
                 * Check Jobs
                 */
                if (warnings == null || warnings.isEmpty()) {
                    HttpHelper.setSession(request, "Error_Message", "An I/O error occurred while fetching warnings");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Extract the Data Needed for The Table
                 */
                List<Map<String, String>> tableWarnings = new ArrayList<>();

                warnings = warnings.stream().filter(warning
                        -> Objects.equals(warning.getManagement().getId(), user.getId())
                        || Objects.equals(warning.getJobseeker().getId(), user.getId()))
                        .collect(Collectors.toList());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                warnings.forEach(warning -> {
                    Map<String, String> warningsMap = new HashMap<>();
                    warningsMap.put("id", warning.getId().toString());
                    warningsMap.put("Management", warning.getManagement().getName());
                    warningsMap.put("Jobseeker_Name", warning.getJobseeker().getName());
                    warningsMap.put("Created_Date", dateFormat.format(warning.getCreatedAt()).toString());
                    warningsMap.put("jobseeker_id", warning.getJobseeker().getId().toString());
                    tableWarnings.add(warningsMap);
                });

                /**
                 * Handling searching
                 */
                if (HttpHelper.CheckRequestType(request, "POST")) {
                    String query = HttpHelper.getParam(request, "query");
                    if (!query.isEmpty()) {
                        List<Map<String, String>> filtredWarning = tableWarnings;
                        filtredWarning = SearchHelper.search(filtredWarning, query);
                        if (filtredWarning != null) {
                            request.setAttribute("warnings", filtredWarning);
                            HttpHelper.forward(request, response, new Route().add(JspPackage.WARNINGS.getPath()).add(JspFile.INDEX.getPath()).get());
                        }
                    }
                    return;
                }
                request.setAttribute("warnings", tableWarnings);
                HttpHelper.forward(request, response, new Route()
                        .add(JspPackage.WARNINGS.getPath())
                        .add(JspFile.INDEX.getPath()).get());

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error_Message An unexpected error occurred while Handling Warnings");
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
