/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
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
public class Auth {

    private MyUserFacade myUserFacade;
    private MyRoleFacade myRoleFacade;
    private CustomerInfoFacade customerInfoFacade;  
    private JobseekerInfoFacade jobseekerInfoFacade;  

    public Auth(MyUserFacade myUserFacade) {
        this.myUserFacade = myUserFacade;
    }

    public Auth(MyUserFacade myUserFacade, MyRoleFacade myRoleFacade,CustomerInfoFacade customerInfoFacade, JobseekerInfoFacade jobseekerInfoFacade ) {
        this.myUserFacade = myUserFacade;
        this.myRoleFacade = myRoleFacade;
        this.jobseekerInfoFacade = jobseekerInfoFacade; 
        this.customerInfoFacade = customerInfoFacade;
    }
    

    public boolean tryToLoging(String emailOrUserName, String password) {
        MyUser user = this.myUserFacade.findByEmail(emailOrUserName) != null
                ? this.myUserFacade.findByEmail(emailOrUserName)
                : this.myUserFacade.findByUserName(emailOrUserName);

        return user != null ? user.getPassword().equals(password) : false;
    }

    public MyUser user(String emailOrUserName) {
        return this.myUserFacade.findByEmail(emailOrUserName) != null
                ? this.myUserFacade.findByEmail(emailOrUserName)
                : this.myUserFacade.findByUserName(emailOrUserName);
    }

    public boolean tryToRegisterUserName(String username) {
        return !(this.myUserFacade.findByUserName(username) == null);
    }

    public boolean tryToRegisterEmial(String email) {
        return !(this.myUserFacade.findByEmail(email) == null);
    }

    public static MyUser user(HttpServletRequest request) {
        try {
            return (MyUser) request.getSession().getAttribute("user");
        } catch (Exception e) {
            return new MyUser();
        }
    }

    public static boolean can(HttpServletRequest request, String name) {
        Object u = request.getSession().getAttribute("user");
        if (u == null) {
            return false;
        }
        try {
            MyUser user = (MyUser) u;
            return user.can(name) || user.is(name);
        } catch (NoClassDefFoundError | ClassCastException e) {
            return false;
        }
    }

    public static boolean canAny(HttpServletRequest req, ArrayList<String> name) {
        Object u = req.getSession().getAttribute("user");
        if (u == null) {
            return false;
        }
        try {
            MyUser user = (MyUser) u;
            return name.stream().anyMatch((string) -> (user.can(string) || user.is(string)));
        } catch (NoClassDefFoundError | ClassCastException e) {
            return false;
        }
    }

    public MyUser register(String name, String userName, String password, String email, String phoneNumber, String role) {
        String status = role.equalsIgnoreCase("customer") ? "Pending" : "Active";
        MyRole myRole = this.myRoleFacade.findByName(role);
        MyUser u = new MyUser(name, userName, password, email, phoneNumber, new Date(), status);
        u.setRole(myRole);
        if (myRole.equals("Jobseeker")) {
            u.setWarningCounter(0);
        }
        this.myUserFacade.create(u);
        return this.user(email);
    }

    public void registerCustomerInfo(MyUser user, String address, String companyName, String website) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setUser(user);
        customerInfo.setAddress(address);
        customerInfo.setCompanyName(companyName);
        customerInfo.setWebsite(website);
        this.customerInfoFacade.create(customerInfo);
    }

    public void registerJobseekerInfo(MyUser user, String address, String hobbies, String gender, String skills, int age) {
        JobseekerInfo jobseekerInfo = new JobseekerInfo();
        jobseekerInfo.setUser(user);
        jobseekerInfo.setAddress(address);
        jobseekerInfo.setHobbies(hobbies);
        jobseekerInfo.setGender(gender);
        jobseekerInfo.setSkills(skills);
        jobseekerInfo.setAge(age);
        this.jobseekerInfoFacade.create(jobseekerInfo);
    }

}
