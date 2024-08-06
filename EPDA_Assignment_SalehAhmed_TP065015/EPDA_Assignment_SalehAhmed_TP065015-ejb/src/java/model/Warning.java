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
    @NamedQuery(name = "Warning.findByDateRange", query = "SELECT w FROM Warning w WHERE w.createdAt BETWEEN :startDate AND :endDate"),
    @NamedQuery(name = "Warning.countByManager", query = "SELECT w.management.name, COUNT(w) FROM Warning w GROUP BY w.management.name"),
    @NamedQuery(name = "Warning.countJobseekersByManager", query = "SELECT w.management.name, COUNT(DISTINCT w.jobSeeker.id) FROM Warning w GROUP BY w.management.name")
})
public class Warning implements Serializable,Model {
    /**
     * PARMETERS
    */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdAt;
    
    /** 
     * RELATIONSHIPS 
    */
    @ManyToOne
    @JoinColumn(name = "management_id")
    private MyUser management;

    @ManyToOne
    @JoinColumn(name = "job_seeker_id")
    private MyUser jobSeeker;
    
    
    /** 
     * CONSTRUCTORS
     */
    public Warning() {
        
    }

    public Warning(Date createdAt, MyUser management, MyUser jobSeeker) {
        this.createdAt = createdAt;
        this.management = management;
        this.jobSeeker = jobSeeker;
    }
    
    /**
     * GETTERS 
     * SETTERS
     *
     * @return 
    */
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public MyUser getManagement() {
        return management;
    }

    public void setManagement(MyUser management) {
        this.management = management;
    }

    public MyUser getJobseeker() {
        return jobSeeker;
    }

    public void setJobseeker(MyUser jobSeeker) {
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
        if (!(object instanceof Warning)) {
            return false;
        }
        Warning other = (Warning) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Warning[ id=" + id + " ]";
    }

    @Override
    public List<String> getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
