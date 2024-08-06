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
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
import helpers.HttpHelper;
import helpers.Route;
import helpers.Validation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import middlewares.Gate;
import model.Application;
import model.CustomerInfo;
import model.EJB.CustomerInfoFacade;
import model.EJB.JobseekerInfoFacade;
import model.EJB.MyUserFacade;
import model.JobseekerInfo;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class Update extends HttpServlet {

    @EJB
    private JobseekerInfoFacade jobseekerInfoFacade;

    @EJB
    private CustomerInfoFacade customerInfoFacade;

    @EJB
    private MyUserFacade myUserFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error- occurs
     * @throws IOException if an I/O error- occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        /**
         * Check Auth user
         */
        Gate.authorise(request, response, "Update User");
        try (PrintWriter out = response.getWriter()) {

            /**
             * Handle Get Request
             */
            if (HttpHelper.CheckRequestType(request, "GET")) {
                try {
                    /**
                     * Collect Data
                     */
                    Long id = Long.parseLong(HttpHelper.getParam(request, "id"));
                    String status = HttpHelper.getParam(request, "Type");

                    /**
                     * Validate Data Updating Data
                     */
                    if (id == null) {
                        HttpHelper.setSession(request, "Validation_Error", "Invalid Data Found");
                        HttpHelper.back(request, response);
                        return;
                    }

                    MyUser user = myUserFacade.find(id);

                    /**
                     * Handle the request to update.jsp
                     */
                    if (status.isEmpty()) {
                        if (user.getRole().getName().equalsIgnoreCase("Customer")) {
                            CustomerInfo customerInfo = customerInfoFacade.findByUserId(user.getId());
                            request.setAttribute("customerInfo", customerInfo);
                        } else if (user.getRole().getName().equalsIgnoreCase("jobseeker")) {
                            JobseekerInfo jobseekerInfo = jobseekerInfoFacade.findByUserId(user.getId());
                            request.setAttribute("jobseekerInfo", jobseekerInfo);
                        }
                        request.setAttribute("user", user);
                        HttpHelper.forward(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.UPDATE.getPath()).get() + "?id=" + user.getId());
                        return;
                    }
                    if (AccountStatus.SUSPENDED.getStatus().equals(status)
                            || AccountStatus.ACTIVE.getStatus().equals(status)
                            || AccountStatus.PENDING.getStatus().equals(status)
                            || AccountStatus.REJECTED.getStatus().equals(status)) {
                        if (user.getStatus().equals(AccountStatus.SUSPENDED.getStatus()) && !AccountStatus.SUSPENDED.getStatus().equals(status)) {
                            user.setWarningCounter(0);
                        }
                        user.setStatus(status);
                        user.setUpdatedAt(new Date());
                        myUserFacade.edit(user);
                    } else {
                        HttpHelper.setSession(request, "Validation_Error", "Status Not Found");
                        HttpHelper.back(request, response);
                        return;
                    }

                    /**
                     * Updating the Data
                     */
                    HttpHelper.setSession(request, "Success_Message", "User Account Status Updated");
                    HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get());

                } catch (NumberFormatException e) {
                    Logger.getLogger(controllers.Applications.Create.class.getName()).log(Level.SEVERE, null, e);
                } catch (Exception e) {
                    HttpHelper.back(request, response);
                    return;
                }
            }

            /**
             * Handle POST Request
             */
            if (HttpHelper.CheckRequestType(request, "POST")) {
                try {
                    /**
                     * Collect Data
                     */
                    Long id = Long.parseLong(HttpHelper.getParam(request, "id"));
                    String name = HttpHelper.getParam(request, "name");
                    String email = HttpHelper.getParam(request, "email");
                    String userName = HttpHelper.getParam(request, "userName");
                    String status = HttpHelper.getParam(request, "status");
                    String password = HttpHelper.getParam(request, "password");
                    String confPassword = HttpHelper.getParam(request, "confPassword");

                    // Additional fields for Customer
                    String customerAddress = HttpHelper.getParam(request, "customerAddress");
                    String companyName = HttpHelper.getParam(request, "companyName");
                    String website = HttpHelper.getParam(request, "website");

                    // Additional fields for Jobseeker
                    String jobseekerAddress = HttpHelper.getParam(request, "jobseekerAddress");
                    String hobbies = HttpHelper.getParam(request, "hobbies");
                    String gender = HttpHelper.getParam(request, "gender");
                    String skills = HttpHelper.getParam(request, "skills");
                    int age = Integer.parseInt(HttpHelper.getParam(request, "age")); 

                    /**
                     * Validate Data Updating Data
                     */
                    if (id == null) {
                        HttpHelper.setSession(request, "Validation_Error", "User ID is required.");
                        HttpHelper.back(request, response);
                        return;
                    }

                    MyUser user = myUserFacade.find(id);

                    if (user == null) {
                        HttpHelper.setSession(request, "Validation_Error", "User not found.");
                        HttpHelper.back(request, response);
                        return;
                    }

                    if (!name.isEmpty() && !name.equals(user.getName())) {
                        user.setName(name);
                    }
                    if (!status.isEmpty()) {
                        if (status.equals(AccountStatus.ACTIVE.getStatus())
                                || status.equals(AccountStatus.PENDING.getStatus())
                                || status.equals(AccountStatus.REJECTED.getStatus())
                                || status.equals(AccountStatus.SUSPENDED.getStatus())) {
                            user.setStatus(status);
                        } else {
                            HttpHelper.setSession(request, "Validation_Error", "Invalid status.");
                            request.setAttribute("user", user);
                            HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.UPDATE.getPath()).get());
                            return;
                        }
                    }

                    if (!email.isEmpty() && !email.equals(user.getEmail())) {
                        if (Validation.checkEmail(email)) {
                            if (myUserFacade.findByEmail(email) == null) {
                                user.setEmail(email);

                            } else {
                                HttpHelper.setSession(request, "Validation_Error", "Email is already registered.");
                                request.setAttribute("user", user);
                                HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.UPDATE.getPath()).get());
                                return;
                            }
                        } else {
                            HttpHelper.setSession(request, "Validation_Error", "Invalid email format.");
                            request.setAttribute("user", user);
                            HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.UPDATE.getPath()).get());
                            return;
                        }
                    }

                    if (!userName.isEmpty() && !userName.equals(user.getUserName())) {
                        if (myUserFacade.findByUserName(userName) == null) {
                            user.setuserName(userName);
                        } else {
                            HttpHelper.setSession(request, "Validation_Error", "Username is already taken.");
                            request.setAttribute("user", user);
                            HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.UPDATE.getPath()).get());
                            return;
                        }
                    }

                    if (!password.isEmpty()) {
                        if (!confPassword.isEmpty()) {
                            if (password.equals(confPassword)) {
                                if (Validation.checkPassword(password)) {
                                    user.setPassword(password);
                                } else {
                                    HttpHelper.setSession(request, "Validation_Error", "Invalid password format.");
                                    request.setAttribute("user", user);
                                    HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.UPDATE.getPath()).get());
                                    return;
                                }
                            } else {
                                HttpHelper.setSession(request, "Validation_Error", "Conformation Password and The Password Is Not Match.");
                                request.setAttribute("user", user);
                                HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.UPDATE.getPath()).get());
                                return;
                            }
                        } else {
                            HttpHelper.setSession(request, "Validation_Error", "Conformation Password Is Empty.");
                            request.setAttribute("user", user);
                            HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.UPDATE.getPath()).get());
                            return;
                        }
                    }

                    if (user.getRole().getName().equalsIgnoreCase("Customer")) {
                        CustomerInfo customerInfo = customerInfoFacade.findByUserId(user.getId());
                        if (customerInfo == null) {
                            customerInfo = new CustomerInfo();
                            customerInfo.setUser(user);
                        }
                        if (!customerAddress.isEmpty() && !customerAddress.equals(customerInfo.getAddress())) {
                            customerInfo.setAddress(customerAddress);
                        }
                        if (!companyName.isEmpty() && !companyName.equals(customerInfo.getCompanyName())) {
                            customerInfo.setCompanyName(companyName);
                        }
                        if (!website.isEmpty() && !website.equals(customerInfo.getWebsite())) {
                            customerInfo.setWebsite(website);
                        }
                        customerInfoFacade.edit(customerInfo);
                    } else if (user.getRole().getName().equalsIgnoreCase("Jobseeker")) {
                        JobseekerInfo jobseekerInfo = jobseekerInfoFacade.findByUserId(user.getId());
                        if (jobseekerInfo == null) {
                            jobseekerInfo = new JobseekerInfo();
                            jobseekerInfo.setUser(user);
                        }
                        if (!jobseekerAddress.isEmpty() && !jobseekerAddress.equals(jobseekerInfo.getAddress())) {
                            jobseekerInfo.setAddress(jobseekerAddress);
                        }
                        if ( age != 0 && age != jobseekerInfo.getAge()) {
                            jobseekerInfo.setAge(age);
                        }
                        if (!hobbies.isEmpty() && !hobbies.equals(jobseekerInfo.getHobbies())) {
                            jobseekerInfo.setHobbies(hobbies);
                        }
                        if (!gender.isEmpty() && !gender.equals(jobseekerInfo.getGender())) {
                            jobseekerInfo.setGender(gender);
                        }
                        if (!skills.isEmpty() && !skills.equals(jobseekerInfo.getSkills())) {
                            jobseekerInfo.setSkills(skills);
                        }
                        jobseekerInfoFacade.edit(jobseekerInfo);
                    }

                    /**
                     * Update User
                     */
                    user.setUpdatedAt(new Date());
                    myUserFacade.edit(user);
                    HttpHelper.setSession(request, "Success_Message", "User updated successfully.");
                    HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get());

                } catch (NumberFormatException e) {
                    Logger.getLogger(controllers.Applications.Create.class.getName()).log(Level.SEVERE, null, e);
                } catch (Exception e) {
                    System.out.println("Error_Message An unexpected error- occurred while Updateing the User Data");
                    HttpHelper.back(request, response);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error- occurs
     * @throws IOException if an I/O error- occurs
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
     * @throws ServletException if a servlet-specific error- occurs
     * @throws IOException if an I/O error- occurs
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
