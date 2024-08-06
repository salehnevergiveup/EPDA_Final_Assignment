/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.EJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Report;

/**
 *
 * @author saleh
 */
@Stateless
public class ReportFacade extends AbstractFacade<Report> {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReportFacade() {
        super(Report.class);
    }
    
}
