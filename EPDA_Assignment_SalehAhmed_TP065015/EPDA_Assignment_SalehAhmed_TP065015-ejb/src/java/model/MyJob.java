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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author saleh
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "MyJob.findByCustomerId", query = "SELECT j FROM MyJob j WHERE j.customer.id = :customerId"),  
    @NamedQuery(name = "MyJob.findByDateRange", query = "SELECT j FROM MyJob j WHERE j.createdAt BETWEEN :startDate AND :endDate"),
    @NamedQuery(name = "MyJob.countByStatusAndDateRange", query = "SELECT j.status, COUNT(j) FROM MyJob j WHERE j.createdAt BETWEEN :startDate AND :endDate GROUP BY j.status")
})
public class MyJob implements Serializable , Model{
    /**
     * PARMETERS
    */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;  
    private String description;  
    private boolean status;  
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdAt;  
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedAt;  
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueDate;  
    
    /** 
     * RELATIONSHIPS 
    */
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private MyUser customer;
    
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Application> applications;
    
    /** 
     * CONSTRUCTORS
     */
    public MyJob() {
    }

    public MyJob(String title, String description, boolean status, Date createdAt, Date dueDate, MyUser customer) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.customer = customer;
    }
    
    /**
     * GETTERS 
     * SETTERS
     *
     * @return 
    */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public MyUser getCustomer() {
        return customer;
    }

    public void setCustomer(MyUser customer) {
        this.customer = customer;
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
        if (!(object instanceof MyJob)) {
            return false;
        }
        MyJob other = (MyJob) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MyJob[ id=" + id + " ]";
    }

    @Override
    public List<String> getData() {
     return Arrays.asList(
            this.title,
            this.description,
            String.valueOf(this.status),
            this.createdAt.toString(),
            this.updatedAt.toString(),
            this.dueDate.toString(),
            this.getCustomer().getName()
        );
    }
 
}
