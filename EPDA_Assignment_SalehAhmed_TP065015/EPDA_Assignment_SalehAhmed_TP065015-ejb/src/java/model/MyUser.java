/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author saleh
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "MyUser.findByRole", query = "SELECT u FROM MyUser u WHERE u.role.name = :roleName")
    ,  
    @NamedQuery(name = "MyUser.findByEmail", query = "SELECT u FROM MyUser u WHERE u.email = :email")
    ,
    @NamedQuery(name = "MyUser.findByUsername", query = "SELECT u FROM MyUser u WHERE u.userName = :userName")
    ,
    @NamedQuery(name = "MyUser.findByRoleAndDateRange", query = "SELECT u FROM MyUser u WHERE u.role.name = :roleName AND u.createdAt BETWEEN :startDate AND :endDate")
    ,
    @NamedQuery(name = "MyUser.countByStatusAndRoleAndDateRange", query = "SELECT u.status, COUNT(u) FROM MyUser u WHERE u.role.name = :roleName AND u.createdAt BETWEEN :startDate AND :endDate GROUP BY u.status"),
    
     @NamedQuery(name = "MyUser.countByStatusAndRole", query = "SELECT u.status, COUNT(u) FROM MyUser u WHERE u.role.name = :roleName GROUP BY u.status")
    ,
})
public class MyUser implements Serializable, Model {

    /**
     * PARMETERS
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String userName;
    private String password;
    private String email;
    private String phonNumber;
    private String status;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdAt;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedAt;
    private int warningCounter;

    /**
     * RELATIONSHIPS
     */
    @OneToOne
    private MyRole role;

    public int getWarningCounter() {
        return warningCounter;
    }

    public void setWarningCounter(int warningCounter) {
        this.warningCounter = warningCounter;
    }

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyJob> jobs;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> jobseekersComments;

    public List<Comment> getJobseekersComments() {
        return jobseekersComments;
    }

    public void setJobseekersComments(List<Comment> jobseekersComments) {
        this.jobseekersComments = jobseekersComments;
    }

    public List<Comment> getCustomersComments() {
        return customersComments;
    }

    public void setCustomersComments(List<Comment> customersComments) {
        this.customersComments = customersComments;
    }

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> customersComments;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> customerFeedbacks;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> jobSeekerFeedbacks;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Warning> jobSeekerWarnings;

    @OneToMany(mappedBy = "management", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Warning> managementWarnings;

    /**
     * CONSTRUCTORS
     */
    public MyUser() {
    }

    public MyUser(String name, String userName, String password, String email, String phonNumber, String status, MyRole role, List<MyJob> jobs, List<Application> applications, List<Comment> jobseekersComments, List<Comment> customersComments, List<Feedback> customerFeedbacks, List<Feedback> jobSeekerFeedbacks, List<Warning> jobSeekerWarnings, List<Warning> managementWarnings) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phonNumber = phonNumber;
        this.status = status;
        this.role = role;
        this.jobs = jobs;
        this.applications = applications;
        this.jobseekersComments = jobseekersComments;
        this.customersComments = customersComments;
        this.customerFeedbacks = customerFeedbacks;
        this.jobSeekerFeedbacks = jobSeekerFeedbacks;
        this.jobSeekerWarnings = jobSeekerWarnings;
        this.managementWarnings = managementWarnings;
    }

    public MyUser(String name, String userName, String password, String email, Date createdAt, String status) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.status = status;//Pending, Active, Suspended 
        this.createdAt = createdAt;
    }

    public MyUser(String name, String userName, String password, String email, String phoneNumber, Date createdAt, String status) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phonNumber = phoneNumber;
        this.status = status;//Pending, Active, Suspended 
        this.createdAt = createdAt;
    }

    public MyUser(String name, String userName, String password, String email, String phoneNumber, Date createdAt, String status, int counter) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phonNumber = phoneNumber;
        this.status = status;//Pending, Active, Suspended 
        this.createdAt = createdAt;
        this.warningCounter = counter;
    }

    /**
     * GETTERS SETTERS
     *
     * @return
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhonNumber() {
        return phonNumber;
    }

    public void setPhonNumber(String phonNumber) {
        this.phonNumber = phonNumber;
    }

    public List<MyJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<MyJob> jobs) {
        this.jobs = jobs;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<Feedback> getCustomerFeedbacks() {
        return customerFeedbacks;
    }

    public void setCustomerFeedbacks(List<Feedback> customerFeedbacks) {
        this.customerFeedbacks = customerFeedbacks;
    }

    public List<Feedback> getJobSeekerFeedbacks() {
        return jobSeekerFeedbacks;
    }

    public void setJobSeekerFeedbacks(List<Feedback> jobSeekerFeedbacks) {
        this.jobSeekerFeedbacks = jobSeekerFeedbacks;
    }

    public List<Warning> getJobSeekerWarnings() {
        return jobSeekerWarnings;
    }

    public void setJobSeekerWarnings(List<Warning> jobSeekerWarnings) {
        this.jobSeekerWarnings = jobSeekerWarnings;
    }

    public List<Warning> getManagementWarnings() {
        return managementWarnings;
    }

    public void setManagementWarnings(List<Warning> managementWarnings) {
        this.managementWarnings = managementWarnings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phonNumber;
    }

    public void setPhone_number(String phone_number) {
        this.phonNumber = phone_number;
    }

    public MyRole getRole() {
        return role;
    }

    public void setRole(MyRole role) {
        this.role = role;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * METHODS
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MyUser)) {
            return false;
        }
        MyUser other = (MyUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public boolean can(String permission) {
        return this.role.havePermission(permission);
    }

    public boolean is(String role) {
        return this.getRole().getName().equals(role);
    }

    @Override
    public String toString() {
        return "model.MyUser[ id=" + id + " ]";
    }

    @Override
    public List<String> getData() {
        return Arrays.asList(
                name,
                userName,
                email,
                phonNumber,
                status
        );
    }

}
