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
    @NamedQuery(name = "Comment.findByJobseekerId", query = "SELECT c FROM Comment c WHERE c.jobSeeker.id = :jobSeekerId"),
    @NamedQuery(name = "Comment.findByCustomerId", query = "SELECT c FROM Comment c WHERE c.customer.id = :customerId"),
    @NamedQuery(name = "Comment.totalComments", query = "SELECT COUNT(c) FROM Comment c WHERE c.createdAt BETWEEN :startDate AND :endDate"),
    @NamedQuery(name = "Comment.findByDateRange", query = "SELECT c FROM Comment c WHERE c.createdAt BETWEEN :startDate AND :endDate")
})
public class Comment implements Serializable,Model {
    /**
     * PARMETERS
    */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String content;  
    private Integer rating = 0; 
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdAt;  
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedAt; 
    
    @JoinColumn(name = "report_id")
    @ManyToOne
    private Report report;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private MyUser customer;
    
    @ManyToOne
    @JoinColumn(name = "job_seeker_id")
    private MyUser jobSeeker;


 

    /** 
     * CONSTRUCTORS
     */ 
    public Comment() {
    }

    public Comment(String content, int rating, Date createdAt, Date updatedAt, MyUser customer, MyUser jobSeeker) {
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.rating = rating;
        this.customer = customer;
        this.jobSeeker = jobSeeker;
    }
    
    /**
     * GETTERS 
     * SETTERS
     *
     * @return 
    */
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getContent() {
        return content;    
    }
    
    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public MyUser getCustomer() {
        return customer;
    }

    public void setCustomer(MyUser customer) {
        this.customer = customer;
    }

    public void setContent(String content) {
        this.content = content;
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
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Comment[ id=" + id + " ]";
    }

    @Override
    public List<String> getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
