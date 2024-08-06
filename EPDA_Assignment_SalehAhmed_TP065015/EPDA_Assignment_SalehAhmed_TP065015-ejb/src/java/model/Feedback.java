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
    @NamedQuery(name = "Feedback.totalFeedbacks", query = "SELECT COUNT(f) FROM Feedback f WHERE f.createdAt BETWEEN :startDate AND :endDate")
    ,
    @NamedQuery(name = "Feedback.totalPositiveFeedbacks", query = "SELECT COUNT(f) FROM Feedback f WHERE f.createdAt BETWEEN :startDate AND :endDate AND f.type = 'Positive'")
    ,
    @NamedQuery(name = "Feedback.totalNegativeFeedbacks", query = "SELECT COUNT(f) FROM Feedback f WHERE f.createdAt BETWEEN :startDate AND :endDate AND f.type = 'Negative'")
    ,
    @NamedQuery(name = "Feedback.findByDateRange", query = "SELECT f FROM Feedback f WHERE f.createdAt BETWEEN :startDate AND :endDate")
})
public class Feedback implements Serializable, Model {
    /**
     * PARMETERS
    */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String type;  //Postive , Negative 
    private String content; 
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdAt;
    
    /** 
     * RELATIONSHIPS 
    */
    @ManyToOne
    @JoinColumn(name = "job_seeker_id")
    private MyUser jobSeeker;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private MyUser customer;
    
    /** 
     * CONSTRUCTORS
     */
    public Feedback() {
    }

    public Feedback(String type, String content, Date createdAt, MyUser jobSeeker, MyUser customer) {
        this.type = type;
        this.content = content;
        this.createdAt = createdAt;
        this.jobSeeker = jobSeeker;
        this.customer = customer;
    }
    
    /**
     * GETTERS 
     * SETTERS
     *
     * @return 
    */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
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

    public MyUser getJobseeker() {
        return jobSeeker;
    }

    public void setJobseeker(MyUser jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

    public MyUser getCustomer() {
        return customer;
    }

    public void setCustomer(MyUser customer) {
        this.customer = customer;
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
        if (!(object instanceof Feedback)) {
            return false;
        }
        Feedback other = (Feedback) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Feedback[ id=" + id + " ]";
    }

    @Override
    public List<String> getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
