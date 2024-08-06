/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Feedbacks;

import controllers.enums.BaseRoute;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.FeedbackFacade;
import model.EJB.MyUserFacade;
import model.Feedback;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class Index extends HttpServlet {

    @EJB
    private FeedbackFacade feedbackFacade;

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
        Gate.authorise(request, response, "Read Feedback");
        
        try (PrintWriter out = response.getWriter()) {
            try {

                MyUser user = Auth.user(request);

                List<Feedback> feedbacks = feedbackFacade.findAll();

                /**
                 * Check Jobs
                 */
                if (feedbacks == null || feedbacks.isEmpty()) {
                    HttpHelper.setSession(request, "Error_Message", "An I/O error occurred while fetching Feedbacks");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Extract the Data Needed for The Table
                 */
                List<Map<String, String>> tableFeedbacks = new ArrayList<>();

                feedbacks = feedbacks.stream().filter(feedback
                        -> Objects.equals(feedback.getCustomer().getId(), user.getId())
                        || Objects.equals(feedback.getJobseeker().getId(), user.getId()))
                        .collect(Collectors.toList());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                feedbacks.forEach(feedback -> {
                    Map<String, String> feedbackMap = new HashMap<>();
                    feedbackMap.put("id", feedback.getId().toString());
                    feedbackMap.put("Company_Name", feedback.getCustomer().getName());
                    feedbackMap.put("Jobseeker_Name", feedback.getJobseeker().getName());
                    feedbackMap.put("Content", feedback.getContent());
                    feedbackMap.put("Created_Date", dateFormat.format(feedback.getCreatedAt()).toString());
                    feedbackMap.put("customer_id", feedback.getCustomer().getId().toString());
                    feedbackMap.put("jobseeker_id", feedback.getJobseeker().getId().toString());
                    tableFeedbacks.add(feedbackMap);
                });

                /**
                 * Handling searching
                 */
                if (HttpHelper.CheckRequestType(request, "POST")) {
                    String query = HttpHelper.getParam(request, "query");
                    if (!query.isEmpty()) {
                        List<Map<String, String>> filtredFeedbacks = tableFeedbacks;
                        filtredFeedbacks = SearchHelper.search(filtredFeedbacks, query);
                        if (!filtredFeedbacks.isEmpty()) {
                            request.setAttribute("feedbacks", filtredFeedbacks);
                            HttpHelper.incldue(request, response, new Route().add(JspPackage.FEEDBACKS.getPath()).add(JspFile.INDEX.getPath()).get());
                            return;
                        }
                    }
                    return;
                }

                request.setAttribute("feedbacks", tableFeedbacks);

                HttpHelper.incldue(request, response, new Route()
                        .add(JspPackage.FEEDBACKS.getPath())
                        .add(JspFile.INDEX.getPath()).get());

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error_Message An unexpected error occurred while Handling Feedbacks");
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
