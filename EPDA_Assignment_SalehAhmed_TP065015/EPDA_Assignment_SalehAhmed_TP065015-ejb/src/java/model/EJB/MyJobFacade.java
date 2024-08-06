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
import model.MyJob;

/**
 *
 * @author saleh
 */
@Stateless
public class MyJobFacade extends AbstractFacade<MyJob> {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyJobFacade() {
        super(MyJob.class);
    }

    public List<MyJob> findByCustomerId(Long customerId) {
        return em.createNamedQuery("MyJob.findByCustomerId", MyJob.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

    public List<MyJob> findByDateRange(Date startDate, Date endDate) {
        return em.createNamedQuery("MyJob.findByDateRange", MyJob.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    public Map<Boolean, Integer> countByStatusAndDateRange(Date startDate, Date endDate) {
        List<Object[]> results = em.createNamedQuery("MyJob.countByStatusAndDateRange")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        Map<Boolean, Integer> countByStatus = new HashMap<>();
        for (Object[] result : results) {
            countByStatus.put((Boolean) result[0], ((Long) result[1]).intValue());
        }
        return countByStatus;
    }

}
