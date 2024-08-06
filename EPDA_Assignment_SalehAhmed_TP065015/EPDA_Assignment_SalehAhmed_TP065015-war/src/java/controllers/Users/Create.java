/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.Users;

import controllers.enums.AccountStatus;
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
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CustomerInfo;
import model.EJB.CustomerInfoFacade;
import model.EJB.JobseekerInfoFacade;
import model.EJB.MyRoleFacade;
import model.EJB.MyUserFacade;
import model.JobseekerInfo;
import model.MyRole;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class Create extends HttpServlet {

    @EJB
    private CustomerInfoFacade customerInfoFacade;

    @EJB
    private JobseekerInfoFacade jobseekerInfoFacade;

    @EJB
    private MyRoleFacade myRoleFacade;
    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-warPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

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
         * Handle POST Request
         */
        try (PrintWriter out = response.getWriter()) {
            try {
                /**
                 * Collect Data
                 */
                String name = HttpHelper.getParam(request, "name");
                String email = HttpHelper.getParam(request, "email");
                String userName = HttpHelper.getParam(request, "userName");
                String status = HttpHelper.getParam(request, "status");
                String password = HttpHelper.getParam(request, "password");
                String confPassword = HttpHelper.getParam(request, "confPassword");
                String phoneNumber = HttpHelper.getParam(request, "phoneNumber");
                String role = HttpHelper.getParam(request, "role");
                /**
                 * Validate Data
                 */

                if (name.isEmpty()
                        || userName.isEmpty()
                        || email.isEmpty()
                        || password.isEmpty()
                        || status.isEmpty()
                        || role.isEmpty()
                        || phoneNumber.isEmpty()
                        || confPassword.isEmpty()
                        || !Validation.checkEmail(email)
                        || !Validation.checkPassword(password)
                        || !password.equals(confPassword)) {
                    HttpHelper.setSession(request, "Validation_Error", "Please fill all the fields correctly.");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get());
                    return;
                }

                // Additional fields for Customer and Jobseeker
                String companyaddress = null, companyName = null, website = null, jobseekeraddress= null, hobbies = null, skills = null, gender = null;
                int age = 0; 
                // Check the role and fetch relevant additional fields
                if ("Customer".equals(role)) {
                    companyName = HttpHelper.getParam(request, "companyName");
                    website = HttpHelper.getParam(request, "website");
                    companyaddress = HttpHelper.getParam(request, "companyaddress");
                } else if ("Jobseeker".equals(role)) {
                    jobseekeraddress = HttpHelper.getParam(request, "jobseekeraddress");
                    hobbies = HttpHelper.getParam(request, "hobbies");
                    skills = HttpHelper.getParam(request, "skills");
                    gender = HttpHelper.getParam(request, "gender");
                    age =  Integer.parseInt(HttpHelper.getParam(request, "age"));
                }

                if ("Jobseeker".equals(role)) {
                    if (jobseekeraddress.isEmpty()
                            || hobbies.isEmpty()
                            || skills.isEmpty()
                            || gender.isEmpty()) {
                        HttpHelper.setSession(request, "Validation_Error", "Please fill all the fields correctly.");
                        HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get());
                        return;

                    }
                }

                if ("Customer".equals(role)) {
                    if (companyaddress.isEmpty()
                            || companyName.isEmpty()
                            || website.isEmpty()) {
                        HttpHelper.setSession(request, "Validation_Error", "Please fill all the fields correctly.");
                        HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get());
                        return;

                    }
                }

                if (!("Admin".equals(role)
                        || "Management".equals(role)
                        || "Customer".equals(role)
                        || "Jobseeker".equals(role))) {

                    HttpHelper.setSession(request, "Validation_Error", "Invalid role selected.");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get());
                    return;
                }

                if (!(status.equals(AccountStatus.ACTIVE.getStatus())
                        || status.equals(AccountStatus.PENDING.getStatus())
                        || status.equals(AccountStatus.REJECTED.getStatus())
                        || status.equals(AccountStatus.SUSPENDED.getStatus()))) {

                    HttpHelper.setSession(request, "Validation_Error", "Invalid status selected.");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get());
                    return;
                }

                if (myUserFacade.findByEmail(email) != null) {
                    HttpHelper.setSession(request, "Validation_Error", "Email is already registered.");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get());
                    return;
                }

                if (myUserFacade.findByUserName(userName) != null) {
                    HttpHelper.setSession(request, "Validation_Error", "Username is already taken.");
                    HttpHelper.incldue(request, response, new Route().add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get());
                    return;
                }

                /**
                 * Create New User
                 */
                MyRole myRole = myRoleFacade.findByName(role);
                MyUser user = new MyUser(name, userName, password, email, phoneNumber, new Date(), status);
                user.setRole(myRole);
                if (myRole.equals("Jobseeker")) {
                    user.setWarningCounter(0);
                }
                myUserFacade.create(user);
                MyUser newUser = myUserFacade.findByEmail(email);
                
                if("Jobseeker".equals(role)) {  
                   JobseekerInfo jobseekerInfo = new JobseekerInfo(jobseekeraddress,hobbies,gender,skills,age,newUser);
                   jobseekerInfoFacade.create(jobseekerInfo);
                }else if("Customer".equals(role)) {  
                    CustomerInfo customerInfo = new CustomerInfo(companyaddress, companyName, website, newUser); 
                    customerInfoFacade.create(customerInfo);
                }
                HttpHelper.setSession(request, "Success_Message", "User Created successfully.");
                HttpHelper.redirectTo(request, response, new Route().add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get());

            } catch (NumberFormatException e) {
                Logger.getLogger(controllers.Applications.Create.class.getName()).log(Level.SEVERE, null, e);
            } catch (Exception e) {
                System.out.println("Error_Message An unexpected error occurred while Updateing the User Data");
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

    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }

}
