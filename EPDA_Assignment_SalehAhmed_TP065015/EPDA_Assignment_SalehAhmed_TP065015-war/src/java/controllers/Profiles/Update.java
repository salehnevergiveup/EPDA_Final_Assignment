/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Profiles;

import controllers.enums.JspFile;
import controllers.enums.JspPackage;
import controllers.enums.ServletFile;
import controllers.enums.ServletPackage;
import helpers.Auth;
import helpers.HttpHelper;
import helpers.Route;
import helpers.Validation;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middlewares.Guest;
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
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Guest.authorise(request, response);

            if (HttpHelper.CheckRequestType(request, "GET")) {
                MyUser user = Auth.user(request);
                if (user.getRole().getName().equalsIgnoreCase("Customer")) {
                    CustomerInfo customerInfo = customerInfoFacade.findByUserId(user.getId());
                    request.setAttribute("customerInfo", customerInfo);
                } else if (user.getRole().getName().equalsIgnoreCase("Jobseeker")) {
                    JobseekerInfo jobseekerInfo = jobseekerInfoFacade.findByUserId(user.getId());
                    System.out.println("somehting is here");
                    request.setAttribute("jobseekerInfo", jobseekerInfo);
                }

                HttpHelper.forward(request, response, new Route().add(JspPackage.PROFILE.getPath()).add(JspFile.UPDATE.getPath()).get());
            }

            if (HttpHelper.CheckRequestType(request, "POST")) {
                MyUser user = Auth.user(request);
                String name = HttpHelper.getParam(request, "name");
                String userName = HttpHelper.getParam(request, "username");
                String email = HttpHelper.getParam(request, "email");
                String phoneNumber = HttpHelper.getParam(request, "phoneNumber");

                if (name.isEmpty()) {
                    name = user.getName();
                }
                if (userName.isEmpty()) {
                    userName = user.getUserName();
                }
                if (email.isEmpty()) {
                    email = user.getEmail();
                }
                if (phoneNumber.isEmpty()) {
                    phoneNumber = user.getPhonNumber();
                }

                if (!Validation.checkEmail(email)) {
                    HttpHelper.setSession(request, "Validation_Error", "Email is not allowed");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.PROFILE.getPath()).add(JspFile.UPDATE.getPath()).get());
                    return;
                }
                if (new Auth(myUserFacade).tryToRegisterEmial(email) && !user.getEmail().equalsIgnoreCase(email)) {
                    HttpHelper.setSession(request, "Error_Message", "The Email is  being used!");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.PROFILE.getPath()).add(JspFile.UPDATE.getPath()).get());
                    return;
                }

                if (new Auth(myUserFacade).tryToRegisterUserName(userName) && !user.getUserName().equalsIgnoreCase(userName)) {
                    HttpHelper.setSession(request, "Error_Message", "The UserName is being used!");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.PROFILE.getPath()).add(JspFile.UPDATE.getPath()).get());
                    return;
                }

                if (user.getRole().getName().equalsIgnoreCase("Customer")) {
                    CustomerInfo customerInfo = customerInfoFacade.findByUserId(user.getId());
                    String customerAddress = HttpHelper.getParam(request, "customerAddress");
                    String companyName = HttpHelper.getParam(request, "companyName");
                    String website = HttpHelper.getParam(request, "website");

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
                    String jobseekerAddress = HttpHelper.getParam(request, "jobseekerAddress");
                    String hobbies = HttpHelper.getParam(request, "hobbies");
                    String gender = HttpHelper.getParam(request, "gender");
                    String skills = HttpHelper.getParam(request, "skills");
                    int age = Integer.parseInt(HttpHelper.getParam(request, "age"));

                    if (jobseekerInfo == null) {
                        jobseekerInfo = new JobseekerInfo();
                        jobseekerInfo.setUser(user);
                    }
                    if (!jobseekerAddress.isEmpty() && !jobseekerAddress.equals(jobseekerInfo.getAddress())) {
                        jobseekerInfo.setAddress(jobseekerAddress);
                    }
                    if (age != 0 && age != jobseekerInfo.getAge()) {
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
                user.setName(name);
                user.setuserName(userName);
                user.setEmail(email);
                user.setPhonNumber(phoneNumber);
                myUserFacade.edit(user);
                HttpHelper.setSession(request, "Success_Message", "Profile Updated Successfully");
                HttpHelper.redirectTo(request, response,  new Route().add(ServletPackage.PROFILES.getPath()).add(ServletFile.VIEW.getPath()).get());
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
