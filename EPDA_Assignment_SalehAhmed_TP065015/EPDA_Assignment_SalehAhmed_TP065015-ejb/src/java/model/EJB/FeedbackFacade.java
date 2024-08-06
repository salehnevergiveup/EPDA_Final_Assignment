/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.EJB;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Feedback;

/**
 *
 * @author saleh
 */
@Stateless
public class FeedbackFacade extends AbstractFacade<Feedback> {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FeedbackFacade() {
        super(Feedback.class);
    }
    
    public int getTotalFeedbacks(Date startDate, Date endDate) {
        return em.createNamedQuery("Feedback.totalFeedbacks", Long.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult()
                .intValue();
    }

    public int getTotalPositiveFeedbacks(Date startDate, Date endDate) {
        return em.createNamedQuery("Feedback.totalPositiveFeedbacks", Long.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult()
                .intValue();
    }

    public int getTotalNegativeFeedbacks(Date startDate, Date endDate) {
        return em.createNamedQuery("Feedback.totalNegativeFeedbacks", Long.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult()
                .intValue();
    }

    public List<Feedback> findByDateRange(Date startDate, Date endDate) {
        return em.createNamedQuery("Feedback.findByDateRange", Feedback.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
    
}
