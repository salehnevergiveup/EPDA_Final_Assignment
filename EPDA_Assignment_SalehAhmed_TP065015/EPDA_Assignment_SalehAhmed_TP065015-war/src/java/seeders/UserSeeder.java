/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seeders;

import controllers.enums.AccountStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import model.CustomerInfo;
import model.EJB.CustomerInfoFacade;
import model.EJB.JobseekerInfoFacade;
import model.EJB.MyRoleFacade;
import model.EJB.MyUserFacade;
import model.JobseekerInfo;
import model.MyRole;
import model.MyUser;

/**
 *
 * @author saleh
 */
public class UserSeeder {

    private final MyRoleFacade myRoleFacade;
    private final MyUserFacade myUserFacade;
    private final CustomerInfoFacade customerInfoFacade;
    private final JobseekerInfoFacade jobseekerInfoFacade;

    public UserSeeder(MyRoleFacade myRoleFacade, MyUserFacade myUserFacade, CustomerInfoFacade customerInfoFacade, JobseekerInfoFacade jobseekerInfoFacade) {
        this.myRoleFacade = myRoleFacade;
        this.myUserFacade = myUserFacade;
        this.customerInfoFacade = customerInfoFacade;
        this.jobseekerInfoFacade = jobseekerInfoFacade;
    }

    public void seed() {
        int count =  this.myUserFacade.count();
        if (count == 1) {
            this.truncate().seedUsers().assginRoles().createCusromerInfo().createJobSeekerInfo();
        }
    }

    public UserSeeder truncate() {
        this.jobseekerInfoFacade.truncate();
        this.customerInfoFacade.truncate();
        this.myUserFacade.truncate();
        return this;
    }

    public UserSeeder seedUsers() {
        ArrayList<MyUser> users = new ArrayList();
        users.add(new MyUser("Management", "ManagementEE", "password", "management@gmail.com", "2222", new Date(), AccountStatus.ACTIVE.getStatus()));
        users.add(new MyUser("Customer", "CustomerEE", "password", "Customer@gmail.com", "3333", new Date(), AccountStatus.ACTIVE.getStatus()));
        users.add(new MyUser("Jobseeker", "JobSeekerEE", "password", "jobseeker@gmail.com", "4444", new Date(), AccountStatus.ACTIVE.getStatus(), 0));
        String[] accountStatus = {AccountStatus.PENDING.getStatus(), AccountStatus.ACTIVE.getStatus(), AccountStatus.SUSPENDED.getStatus()};
        Random random = new Random();
        for (int i = 1; i <= 5; i++) {
            users.add(new MyUser("Management" + i, "ManagementEE" + i, "password", "management" + i + "@gmail.com", "2222" + i, new Date(), AccountStatus.ACTIVE.getStatus()));
        }
        for (int i = 1; i <= 10; i++) {
            int randomIndex = random.nextInt(2);
            users.add(new MyUser("Customer" + i, "CustomerEE" + i, "password", "Customert" + i + "@gmail.com", "3333" + i, new Date(), accountStatus[randomIndex]));
        }
        for (int i = 0; i <= 30; i++) {
            int randomIndex = random.nextInt(2) + 1;
            users.add(new MyUser("Jobseeker" + i, "JobseekerEE" + i, "password", "Jobseeke" + i + "@gmail.com", "4444" + i, new Date(), accountStatus[randomIndex], 0));
        }
        users.forEach(myUserFacade::create);
        return this;
    }

    public UserSeeder assginRoles() {

        List<MyRole> roles = this.myRoleFacade.findAll();

        this.myUserFacade.findAll().forEach((user) -> {
            String name = user.getName();
            if (name.contains("Management")) {
                user.setRole(roles.stream().filter((role) -> role.getName().equalsIgnoreCase("Management")).findFirst().get());
            } else if (name.contains("Customer")) {
                user.setRole(roles.stream().filter((role) -> role.getName().equalsIgnoreCase("Customer")).findFirst().get());
            } else if (name.contains("Jobseeker")) {
                user.setRole(roles.stream().filter((role) -> role.getName().equalsIgnoreCase("JobSeeker")).findFirst().get());
            }
            this.myUserFacade.edit(user);
        });
        return this;
    }

    public UserSeeder createCusromerInfo() {
        List<MyUser> customers = this.myUserFacade.findByRole("Customer");
        int counter = 1;
        for (MyUser customer : customers) {
            CustomerInfo customerinfo = new CustomerInfo("address" + counter, "company" + counter, "www.company" + counter + ".com", customer);
            this.customerInfoFacade.create(customerinfo);
            counter++;
        }
        return this;
    }

    public UserSeeder createJobSeekerInfo() {
        List<MyUser> jobseekers = this.myUserFacade.findByRole("Jobseeker");
        int counter = 1;
        for (MyUser jobseeker : jobseekers) {
            String gender = counter % 2 == 0 ? "Male" : "Female";
            JobseekerInfo jobseekerInfo = new JobseekerInfo("address" + counter, "hobbies" + counter, gender, "skill" + counter, counter * 10, jobseeker);
            this.jobseekerInfoFacade.create(jobseekerInfo);
            counter++;
        }
        return this;
    }

}
