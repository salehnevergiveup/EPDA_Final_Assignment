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
import model.Comment;

/**
 *
 * @author saleh
 */
@Stateless
public class CommentFacade extends AbstractFacade<Comment> {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommentFacade() {
        super(Comment.class);
    }
    
    public List<Comment> findByJobseekerId(Long jobSeekerId) {
        return em.createNamedQuery("Comment.findByJobseekerId", Comment.class)
                .setParameter("jobSeekerId", jobSeekerId)
                .getResultList();
    }
    
    public List<Comment> findByCustomerId(Long customerId) {
        return em.createNamedQuery("Comment.findByCustomerId", Comment.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }
    
    public int getTotalComments(Date startDate, Date endDate) {
        return em.createNamedQuery("Comment.totalComments", Long.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult()
                .intValue();
    }

    public List<Comment> findByDateRange(Date startDate, Date endDate) {
        return em.createNamedQuery("Comment.findByDateRange", Comment.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}
