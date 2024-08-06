/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Initializer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import model.EJB.MyRoleFacade;
import model.EJB.MyUserFacade;
import model.EJB.PermissionFacade;

/**
 *
 * @author saleh
 */
@Singleton
@Startup
public class NewSessionBean {
    @Inject
    private PermissionFacade permissionFacade;
    @Inject
    private MyRoleFacade myRoleFacade;
    @Inject
    private MyUserFacade myUserFacade;

    @PostConstruct
    public void init() {
        // Logic to run on startup
        new StartAppSeeder(permissionFacade, myRoleFacade, myUserFacade).seed();
    }
}
