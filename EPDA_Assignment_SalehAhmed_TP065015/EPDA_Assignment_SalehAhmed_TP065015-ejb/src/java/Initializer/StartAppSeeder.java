/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Initializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import model.EJB.MyRoleFacade;
import model.EJB.MyUserFacade;
import model.EJB.PermissionFacade;
import model.MyRole;
import model.MyUser;
import model.Permission;

/**
 *
 * @author saleh
 */
public class StartAppSeeder {

    private final PermissionFacade permissionFacade;
    private final MyRoleFacade myRoleFacade;
    private final MyUserFacade myUserFacade;

    public StartAppSeeder(PermissionFacade permissionFacade, MyRoleFacade myRoleFacade, MyUserFacade myUserFacade) {
        this.permissionFacade = permissionFacade;
        this.myRoleFacade = myRoleFacade;
        this.myUserFacade = myUserFacade;
    }

    public void seed() {
        if (this.permissionFacade.count() == 0) {
            this.seedPermissions().seedRoles().assginPermissions().seedAdmin().setAdminRole();
        }
    }
    

    public StartAppSeeder seedAdmin() {
        MyUser admin = new MyUser("Admin", "AdminEE", "password", "admin@gmail.com", "1111", new Date(), "Active");
        myUserFacade.create(admin);
        return this;
    }

    public StartAppSeeder seedRoles() {

        ArrayList<MyRole> roles = new ArrayList();
        roles.add(new MyRole("Admin"));
        roles.add(new MyRole("Management"));
        roles.add(new MyRole("Customer"));
        roles.add(new MyRole("Jobseeker"));

        roles.forEach(this.myRoleFacade::create);

        return this;
    }

    public StartAppSeeder seedPermissions() {

        ArrayList<Permission> permissions = new ArrayList();
        List<String> model = Arrays.asList("User", "Profile", "Feedback", "Warning", "Comment", "Application", "Job", "Report");
        List<String> verbs = Arrays.asList("Create", "Read", "Update", "Delete");

        model.forEach((m) -> {
            verbs.forEach((v) -> {
                permissions.add(new Permission(v + " " + m));
            });
        });

        permissions.add(new Permission("Start App"));

        permissions.sort((Permission p1, Permission p2) -> {
            return p1.getName().compareTo(p2.getName());
        });

        permissions.forEach(this.permissionFacade::create);
        return this;
    }

    public List<Permission> permissionsAndRoles(MyRole role) {
        List<Permission> permissions = this.permissionFacade.findAll();
        switch (role.getName()) {
            case "Admin":
                return permissions.stream().filter((per) -> {
                    String name = per.getName();
                    return name.endsWith("Profile")
                            || name.endsWith("User")
                            || name.equals("Start App")
                            || name.endsWith("Report");
                }).collect(Collectors.toList());
            case "Management":
                return permissions.stream().filter((per) -> {
                    String name = per.getName();
                    return name.endsWith("Profile")
                            || name.endsWith("User")
                            || name.endsWith("Warning")
                            || name.endsWith("Report")
                            || name.equalsIgnoreCase("Read Feedback");
                }).collect(Collectors.toList());

            case "Customer":
                return permissions.stream().filter((per) -> {
                    String name = per.getName();
                    return name.endsWith("Profile")
                            || name.equalsIgnoreCase("Read User")
                            || name.endsWith("Feedback")
                            || name.endsWith("Job")
                            || name.endsWith("Application")
                            || name.equalsIgnoreCase("Read Comment");

                }).collect(Collectors.toList());

            case "Jobseeker":
                return permissions.stream().filter((per) -> {
                    String name = per.getName();
                    return name.endsWith("Profile")
                            || name.equalsIgnoreCase("Read User")
                            || name.equalsIgnoreCase("Read Warning")
                            || name.equalsIgnoreCase("Read Job")
                            || name.equalsIgnoreCase("Read Application")
                            || name.equalsIgnoreCase("Create Application")
                            || name.equalsIgnoreCase("Update Application")
                            || name.equalsIgnoreCase("Read Feedback")
                            || name.endsWith("Comment");

                }).collect(Collectors.toList());

            default:
                return null;
        }

    }

    public StartAppSeeder assginPermissions() {
        List<MyRole> roles = this.myRoleFacade.findAll();
        roles.forEach((role) -> {
            List<Permission> permissions = this.permissionsAndRoles(role);
            role.setPermissions(permissions);
            this.myRoleFacade.edit(role);
        });
        return this;
    }

    public StartAppSeeder setAdminRole() {
        MyRole role = this.myRoleFacade.findByName("Admin");
        MyUser admin = this.myUserFacade.findByEmail("admin@gmail.com");
        admin.setRole(role);
        this.myUserFacade.edit(admin);
        return this;
    }

}
