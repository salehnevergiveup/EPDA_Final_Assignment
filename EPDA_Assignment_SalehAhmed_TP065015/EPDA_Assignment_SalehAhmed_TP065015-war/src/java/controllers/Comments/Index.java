/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Comments;

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
import java.util.Arrays;
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
import model.Comment;
import model.EJB.CommentFacade;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class Index extends HttpServlet {

    @EJB
    private CommentFacade commentFacade;

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
        Gate.authorise(request, response, "Read Comment");
        try (PrintWriter out = response.getWriter()) { 
            /** 
             * Handle request
             */
            try {
                MyUser user = Auth.user(request);

                List<Comment> comments = commentFacade.findAll();

                /**
                 * Check Comments
                 */
                if (comments == null) {
                    HttpHelper.setSession(request, "Error_Message", "An I/O error occurred while fetching Comments");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Extract the Data Needed for The Table
                 */
                String role = user.getRole().getName();
                List<Map<String, String>> commentsString = new ArrayList<>();

                if ("Jobseeker".equals(role)) {
                    comments = commentFacade.findByJobseekerId(user.getId());
                } else if ("Customer".equals(role)) {
                    comments = comments.stream()
                            .filter(comment -> comment.getCustomer().getId() == user.getId())
                            .collect(Collectors.toList());
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                comments.forEach(comment -> {
                    Map<String, String> commentMap = new HashMap<>();
                    commentMap.put("id", comment.getId().toString());
                    commentMap.put("Jobseeker", comment.getJobSeeker().getName());
                    commentMap.put("Company_Name", comment.getCustomer().getName());
                    commentMap.put("Created_Date", dateFormat.format(comment.getCreatedAt()).toString());
                    commentMap.put("Updated_Date", comment.getUpdatedAt() == null ? "None" : dateFormat.format(comment.getUpdatedAt()).toString());
                    commentMap.put("Content", comment.getContent());
                    commentMap.put("customer", comment.getCustomer().getId().toString());
                    commentsString.add(commentMap);
                });

                /**
                 * Handling searching
                 */
                if (HttpHelper.CheckRequestType(request, "POST")) {
                    String query = HttpHelper.getParam(request, "query");
                    if (query != null) {
                        List<Map<String, String>> filtredComments = commentsString;
                        filtredComments = SearchHelper.search(filtredComments, query);
                        if (filtredComments != null) {
                            request.setAttribute("comments", filtredComments);
                            HttpHelper.forward(request, response, new Route().add(JspPackage.COMMENTS.getPath()).add(JspFile.INDEX.getPath()).get());
                        }
                    }
                }

                request.setAttribute("comments", commentsString);

                HttpHelper.forward(request, response, new Route().add(JspPackage.COMMENTS.getPath()).add(JspFile.INDEX.getPath()).get());

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error_Message An unexpected error occurred while Handling Comments");
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
