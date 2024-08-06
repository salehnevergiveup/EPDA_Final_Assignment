/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.EJB;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Application;

/**
 *
 * @author saleh
 */
@Stateless
public class ApplicationFacade extends AbstractFacade<Application> {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ApplicationFacade() {
        super(Application.class);
    }
    
    public List<Application> findByCustomerId(long customerId) {
        return em.createNamedQuery("Application.findByCustomerId", Application.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

    public List<Application> findByJobSeekerId(long jobSeekerId) {
        return em.createNamedQuery("Application.findByJobSeekerId", Application.class)
                .setParameter("jobSeekerId", jobSeekerId)
                .getResultList();
    }
    
    public boolean hasJobseekerApplied(Long jobseekerId, Long jobId) {
        Long count = em.createNamedQuery("Application.findByJobseekerAndJob", Long.class)
                        .setParameter("jobseekerId", jobseekerId)
                        .setParameter("jobId", jobId)
                        .getSingleResult();
        return count > 0;
    }
    
    public List<Application> getApplicationsByDateRange(Date startDate, Date endDate) {
        return em.createNamedQuery("Application.findByDateRange", Application.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
    
    public int getTotalApplications(Date startDate, Date endDate) {
        return em.createNamedQuery("Application.countByDateRange", Long.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult().intValue();
    }
    
    public Map<String, Integer> getApplicationsByStatus(Date startDate, Date endDate) {
        List<Object[]> results = em.createNamedQuery("Application.countByStatusAndDateRange", Object[].class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        Map<String, Integer> applicationsByStatus = new HashMap<>();
        for (Object[] result : results) {
            applicationsByStatus.put((String) result[0], ((Long) result[1]).intValue());
        }
        return applicationsByStatus;
    }
}
