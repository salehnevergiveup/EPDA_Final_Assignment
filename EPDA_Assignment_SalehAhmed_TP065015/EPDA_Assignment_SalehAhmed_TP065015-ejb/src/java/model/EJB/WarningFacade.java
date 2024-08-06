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
import javax.persistence.TypedQuery;
import model.Warning;

/**
 *
 * @author saleh
 */
@Stateless
public class WarningFacade extends AbstractFacade<Warning> {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WarningFacade() {
        super(Warning.class);
    }
// Method to get warnings by date range
    public List<Warning> findByDateRange(Date startDate, Date endDate) {
        TypedQuery<Warning> query = em.createNamedQuery("Warning.findByDateRange", Warning.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    // Method to count warnings by manager
    public Map<String, Integer> countByManager() {
        List<Object[]> results = em.createNamedQuery("Warning.countByManager").getResultList();
        Map<String, Integer> resultMap = new HashMap<>();
        for (Object[] result : results) {
            resultMap.put((String) result[0], ((Number) result[1]).intValue());
        }
        return resultMap;
    }

    // Method to count unique jobseekers by manager
    public Map<String, Integer> countJobseekersByManager() {
        List<Object[]> results = em.createNamedQuery("Warning.countJobseekersByManager").getResultList();
        Map<String, Integer> resultMap = new HashMap<>();
        for (Object[] result : results) {
            resultMap.put((String) result[0], ((Number) result[1]).intValue());
        }
        return resultMap;
    }

    // Method to get the total count of warnings
    public int getTotalWarnings(Date startDate, Date endDate) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(w) FROM Warning w WHERE w.createdAt BETWEEN :startDate AND :endDate", Long.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getSingleResult().intValue();
    }

    // Method to get the total count of jobseekers with warnings
    public int getTotalJobseekersWithWarnings(Date startDate, Date endDate) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(DISTINCT w.jobSeeker.id) FROM Warning w WHERE w.createdAt BETWEEN :startDate AND :endDate", Long.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getSingleResult().intValue();
    }

    // Method to count distinct jobseekers
    public int countDistinctJobseekers() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(DISTINCT w.jobSeeker.id) FROM Warning w", Long.class);
        return query.getSingleResult().intValue();
    }

}
