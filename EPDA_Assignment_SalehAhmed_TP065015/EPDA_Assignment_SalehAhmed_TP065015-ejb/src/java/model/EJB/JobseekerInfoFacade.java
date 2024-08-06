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
import model.JobseekerInfo;

/**
 *
 * @author saleh
 */
@Stateless
public class JobseekerInfoFacade extends AbstractFacade<JobseekerInfo> {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public JobseekerInfoFacade() {
        super(JobseekerInfo.class);
    }
    
    public JobseekerInfo findByUserId(Long userId) {
        TypedQuery<JobseekerInfo> query = em.createNamedQuery("JobseekerInfo.findByUserId", JobseekerInfo.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public List<JobseekerInfo> findAll() {
        TypedQuery<JobseekerInfo> query = em.createNamedQuery("JobseekerInfo.findAll", JobseekerInfo.class);
        return query.getResultList();
    }
    
}
