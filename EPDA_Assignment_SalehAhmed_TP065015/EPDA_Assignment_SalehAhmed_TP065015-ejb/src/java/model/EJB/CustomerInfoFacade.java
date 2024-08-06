/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.EJB;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import model.CustomerInfo;

/**
 *
 * @author saleh
 */
@Stateless
public class CustomerInfoFacade extends AbstractFacade<CustomerInfo> {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerInfoFacade() {
        super(CustomerInfo.class);
    }

    public CustomerInfo findByUserId(Long userId) {
        TypedQuery<CustomerInfo> query = em.createNamedQuery("CustomerInfo.findByUserId", CustomerInfo.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public List<CustomerInfo> findAll() {
        TypedQuery<CustomerInfo> query = em.createNamedQuery("CustomerInfo.findAll", CustomerInfo.class);
        return query.getResultList();
    }

}
