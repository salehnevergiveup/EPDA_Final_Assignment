/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

/**
 *
 * @author saleh
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Application.findByCustomerId", query = "SELECT a FROM Application a WHERE a.job.customer.id = :customerId"),
    @NamedQuery(name = "Application.findByJobSeekerId", query = "SELECT a FROM Application a WHERE a.jobSeeker.id = :jobSeekerId"), 
    @NamedQuery(name = "Application.findByJobseekerAndJob", query = "SELECT COUNT(a) FROM Application a WHERE a.jobSeeker.id = :jobseekerId AND a.job.id = :jobId"),
    @NamedQuery(name = "Application.findByDateRange", query = "SELECT a FROM Application a WHERE a.createdAt BETWEEN :startDate AND :endDate"),
    @NamedQuery(name = "Application.countByDateRange", query = "SELECT COUNT(a) FROM Application a WHERE a.createdAt BETWEEN :startDate AND :endDate"),
    @NamedQuery(name = "Application.countByStatusAndDateRange", query = "SELECT a.status, COUNT(a) FROM Application a WHERE a.createdAt BETWEEN :startDate AND :endDate GROUP BY a.status")
})
public class Application implements Serializable,Model {

    /**
     * PARMETERS
    */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String status; 
    private String selfDescription; 
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdAt;  
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedAt;
    
    
    /** 
     * RELATIONSHIPS 
    */
    @ManyToOne
    @JoinColumn(name = "job_id")
    private MyJob job;
    @ManyToOne
    @JoinColumn(name = "job_seeker_id")
    private MyUser jobSeeker;

    public String getSelfDescription() {
        return selfDescription;
    }

    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }
    
    /** 
     * CONSTRUCTORS
    */
    public Application() {
    }
    
    public Application(String status,String selfDescription, Date createdAt, MyJob job, MyUser jobSeeker) {
        this.status = status;
        this.createdAt = createdAt;
        this.selfDescription = selfDescription;
        this.job = job;
        this.jobSeeker = jobSeeker;
    }
    
    /**
     * GETTERS 
     * SETTERS
     *
     * @return 
    */
    public String getStatus() {
        return status;    
    }

    public void setStatus(String status) {
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

    public MyJob getJob() {
        return job;
    }

    public void setJob(MyJob job) {
        this.job = job;
    }

    public MyUser getJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(MyUser jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

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
        if (!(object instanceof Application)) {
            return false;
        }
        Application other = (Application) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Application[ id=" + id + " ]";
    }

    @Override
    public List<String> getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
