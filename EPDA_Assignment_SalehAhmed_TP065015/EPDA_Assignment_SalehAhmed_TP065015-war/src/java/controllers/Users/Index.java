/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Users;

import controllers.enums.AccountStatus;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Gate;
import model.EJB.MyUserFacade;
import model.MyUser;

/**
 *
 * @author saleh
 */
@WebServlet(name = "Users.Index", urlPatterns = {"/Users/Index"})
public class Index extends HttpServlet {

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
        /**
         * Check the Auth user
         */

        Gate.authorise(request, response, "Read User");
        
        try (PrintWriter out = response.getWriter()) {
            try {
                MyUser user = Auth.user(request);
                List<MyUser> users = this.myUserFacade.findAll();
                String type = HttpHelper.getParam(request, "type");

                /**
                 * Check Users
                 */
                if (users == null) {
                    HttpHelper.setSession(request, "Error_Message", "An I/O error occurred while fetching users");
                    HttpHelper.back(request, response);
                    return;
                }

                /**
                 * Extract the Data Needed for The Table
                 */
                String role = user.getRole().getName();
                List<Map<String, String>> allUsers = new ArrayList<>();
                System.out.println("1");
                if ("Customer".equals(role)) {
                    users = users.stream().filter(user1 -> "Jobseeker".equals(user1.getRole().getName())
                            && AccountStatus.ACTIVE.getStatus().equals(user1.getStatus()))
                            .collect(Collectors.toList());
                } else if ("Jobseeker".equals(role)) {
                    users = users.stream().filter(user1 -> "Customer".equals(user1.getRole().getName())
                            && AccountStatus.ACTIVE.getStatus().equals(user1.getStatus()))
                            .collect(Collectors.toList());
                } else if ("Management".equals(role)) {
                    users = users.stream().filter(user1 -> "Customer".equals(user1.getRole().getName())
                            || "Jobseeker".equals(user1.getRole().getName()))
                            .collect(Collectors.toList());
                } else if ("Admin".equals(role)) {
                    System.out.println("here wallah here");
                    users = users.stream().filter(user1 -> "Management".equals(user1.getRole().getName()))
                            .collect(Collectors.toList());
                }
                System.out.println("2");

                /**
                 * Filter data based on the status
                 */
                if (type != null && !type.isEmpty() && !type.equals("All")) {
                    String type2 = type;
                    users = users.stream().filter(u -> u.getStatus().equalsIgnoreCase(type2)).collect(Collectors.toList());
                } else {
                    type = "All";
                }
                System.out.println("3");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                List<Map<String, String>> fillUsers = new ArrayList<>();
                users.forEach(u -> {
                    Map<String, String> usersMap = new HashMap<>();
                    usersMap.put("id", u.getId().toString());
                    usersMap.put("User", u.getName());
                    usersMap.put("User_Name", u.getUserName());
                    usersMap.put("Email", u.getEmail());
                    usersMap.put("Created_Date", dateFormat.format(u.getCreatedAt()));
                    usersMap.put("Updated_Date", user.getUpdatedAt() == null ? "None" : dateFormat.format(u.getUpdatedAt()));
                    usersMap.put("Status", u.getStatus());
                    fillUsers.add(usersMap);
                });
                System.out.println("4");

                allUsers = fillUsers;
                /**
                 * Handling searching
                 */
                if (HttpHelper.CheckRequestType(request, "POST")) {
                    String query = HttpHelper.getParam(request, "query");
                    if (!query.isEmpty()) {
                        List<Map<String, String>> filteredUsers = allUsers;
                        filteredUsers = SearchHelper.search(filteredUsers, query);
                        if (!filteredUsers.isEmpty()) {
                            allUsers = filteredUsers;
                        }
                    }
                }
                System.out.println("5");

                request.setAttribute("users", allUsers);
                request.setAttribute("type", type);
                HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.INDEX.getPath()).get());

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error_Message An unexpected error occurred while Handling Users");
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
