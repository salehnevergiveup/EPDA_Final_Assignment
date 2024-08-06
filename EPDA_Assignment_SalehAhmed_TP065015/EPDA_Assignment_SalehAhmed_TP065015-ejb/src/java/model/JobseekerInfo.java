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
    @NamedQuery(name = "JobseekerInfo.findByUserId", query = "SELECT j FROM JobseekerInfo j WHERE j.user.id = :userId")
    ,
    @NamedQuery(name = "JobseekerInfo.findAll", query = "SELECT j FROM JobseekerInfo j")
})
public class JobseekerInfo implements Serializable {

    /**
     * PARMETERS
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;
    private String hobbies;
    private String gender;
    private String skills;
    private int age; 
    
    /**
     * RELATIONSHIPS
     */
    @OneToOne
    private MyUser user;  
    
    /**
     * CONSTRUCTORS
     */
    public JobseekerInfo() {
    }

    public JobseekerInfo(String address, String hobbies, String gender, String skills, int age, MyUser user) {
        this.hobbies = hobbies;
        this.gender = gender;
        this.skills = skills;
        this.address = address;
        this.user = user;
        this.age = age; 
    }

    /**
     * GETTERS SETTERS
     *
     * @return
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSkills() {
        return skills;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
    

    public void setSkills(String skills) {
        this.skills = skills;
    }

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

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
        if (!(object instanceof JobseekerInfo)) {
            return false;
        }
        JobseekerInfo other = (JobseekerInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.JobseekerInfo[ id=" + id + " ]";
    }

}
