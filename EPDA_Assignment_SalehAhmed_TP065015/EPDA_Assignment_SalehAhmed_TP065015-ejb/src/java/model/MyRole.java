/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author saleh
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "MyRole.findByName", query = "SELECT r FROM MyRole r WHERE r.name = :name")
})
public class MyRole implements Serializable {
    /**
     * PARMETERS
    */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String name;  
   
    /** 
     * RELATIONSHIPS 
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Permission> permissions;
    
    @OneToOne(mappedBy = "role")
    private MyUser user;
  
    /** 
     * CONSTRUCTORS
    */ 
    public MyRole(String name) {
        this.name = name;
    }
    
    public MyRole() {
        
    }
    
    /**
     * GETTERS 
     * SETTERS
     *
     * @return 
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

  
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * METHODS
    */ 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MyRole)) {
            return false;
        }
        MyRole other = (MyRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    public boolean havePermission(String permission) {  
       return this.permissions.stream().anyMatch((per) -> per.getName().equalsIgnoreCase(permission)); 
    }

    @Override
    public String toString() {
        return "model.MyRole[ id=" + id + " ]";
    }

    public void setId(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
