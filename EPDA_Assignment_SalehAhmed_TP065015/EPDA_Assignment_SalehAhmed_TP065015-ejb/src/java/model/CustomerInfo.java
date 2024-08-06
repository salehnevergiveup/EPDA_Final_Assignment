/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author saleh
 */
@Entity

@NamedQueries({
    @NamedQuery(name = "CustomerInfo.findByUserId", query = "SELECT c FROM CustomerInfo c WHERE c.user.id = :userId")
    ,
    @NamedQuery(name = "CustomerInfo.findAll", query = "SELECT c FROM CustomerInfo c")
})
public class CustomerInfo implements Serializable {

    /**
     * PARMETERS
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;  
    private String companyName;  
    private String website;  
    
    /**
     * RELATIONSHIPS
     */
    @OneToOne
    private MyUser user;  
    
    /**
     * CONSTRUCTORS
     */
    public CustomerInfo() { }  
    
    public  CustomerInfo(String address,  String companyName, String webiste, MyUser user) { 
        this.address = address;  
        this.companyName = companyName;  
        this.website= webiste;  
        this.user = user; 
    }
    
    /**
     * GETTERS SETTERS
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerInfo)) {
            return false;
        }
        CustomerInfo other = (CustomerInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CustomerInfo[ id=" + id + " ]";
    }
    
}
