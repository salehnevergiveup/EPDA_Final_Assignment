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
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.MyUser;

/**
 *
 * @author saleh
 */
@Stateless
public class MyUserFacade extends AbstractFacade<MyUser> {

    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyUserFacade() {
        super(MyUser.class);
    }

    public List<MyUser> findByRole(String roleName) {
        return em.createNamedQuery("MyUser.findByRole", MyUser.class)
                .setParameter("roleName", roleName)
                .getResultList();
    }

    public MyUser findByEmail(String email) {
        System.out.println("email: " + email);
        List<MyUser> users = em.createNamedQuery("MyUser.findByEmail", MyUser.class)
                .setParameter("email", email)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    public MyUser findByUserName(String userName) {
        List<MyUser> users = em.createNamedQuery("MyUser.findByUsername", MyUser.class)
                .setParameter("userName", userName)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    public List<MyUser> findByRoleAndDateRange(String roleName, Date startDate, Date endDate) {
        return em.createNamedQuery("MyUser.findByRoleAndDateRange", MyUser.class)
                .setParameter("roleName", roleName)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    public Map<String, Integer> countByStatusAndRoleAndDateRange(String roleName, Date startDate, Date endDate) {
        List<Object[]> results = em.createNamedQuery("MyUser.countByStatusAndRoleAndDateRange")
                .setParameter("roleName", roleName)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        Map<String, Integer> countByStatus = new HashMap<>();
        for (Object[] result : results) {
            countByStatus.put((String) result[0], ((Long) result[1]).intValue());
        }
        return countByStatus;
    }

    @Override
    public void truncate() {
        this.findAll().forEach(user -> {
            if (!user.getRole().getName().equalsIgnoreCase("Admin")) {
                this.remove(user);
            }
        });
    }

}
