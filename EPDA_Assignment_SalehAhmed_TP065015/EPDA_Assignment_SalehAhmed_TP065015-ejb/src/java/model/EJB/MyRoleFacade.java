/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.EJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import model.MyRole;

/**
 *
 * @author saleh
 */
@Stateless
public class MyRoleFacade extends AbstractFacade<MyRole> {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyRoleFacade() {
        super(MyRole.class);
    }
    
    public MyRole findByName(String name) {
        try {
            return em.createNamedQuery("MyRole.findByName", MyRole.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; 
        }
    }
}
