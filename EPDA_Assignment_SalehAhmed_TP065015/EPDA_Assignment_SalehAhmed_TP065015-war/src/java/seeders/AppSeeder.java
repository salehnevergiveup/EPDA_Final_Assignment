/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seeders;

import controllers.enums.ApplicationStatus;
import controllers.enums.FeedbackType;
import controllers.enums.ReportType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import model.Application;
import model.Comment;
import model.EJB.ApplicationFacade;
import model.EJB.CommentFacade;
import model.EJB.CustomerInfoFacade;
import model.EJB.FeedbackFacade;
import model.EJB.JobseekerInfoFacade;
import model.EJB.MyJobFacade;
import model.EJB.MyRoleFacade;
import model.EJB.MyUserFacade;
import model.EJB.ReportFacade;
import model.EJB.WarningFacade;
import model.Feedback;
import model.MyJob;
import model.MyUser;
import model.Report;
import model.Warning;

/**
 *
 * @author saleh
 */
public class AppSeeder {

    public final ApplicationFacade applcationFacade;
    public final MyRoleFacade myRoleFacade;
    public final CommentFacade commentFacade;
    public final FeedbackFacade feedbackFacade;
    public final MyJobFacade myJobFacade;
    public final WarningFacade warningFacade;
    public final MyUserFacade myUserFacade;
    public final ReportFacade reportFacade;
    private final CustomerInfoFacade customerInfoFacade;
    private final JobseekerInfoFacade jobseekerInfoFacade;
//

    public AppSeeder(MyRoleFacade myRoleFacade, CustomerInfoFacade customerInfoFacade, JobseekerInfoFacade jobseekerInfoFacade, MyUserFacade myUserFacade, ApplicationFacade applcationFacade, CommentFacade commentFacade, FeedbackFacade feedbackFacade, MyJobFacade myJobFacade, WarningFacade warningFacade, ReportFacade reportFacade) {
        this.applcationFacade = applcationFacade;
        this.commentFacade = commentFacade;
        this.feedbackFacade = feedbackFacade;
        this.myJobFacade = myJobFacade;
        this.warningFacade = warningFacade;
        this.myUserFacade = myUserFacade;
        this.reportFacade = reportFacade;
        this.myRoleFacade = myRoleFacade;
        this.jobseekerInfoFacade = jobseekerInfoFacade;
        this.customerInfoFacade = customerInfoFacade;

        int customerCount = this.myUserFacade.findByRole("Customer").size();

        int jobseekerCount = this.myUserFacade.findByRole("Jobseeker").size();

        int managementCount = this.myUserFacade.findByRole("Management").size();

        if (customerCount < 1 && jobseekerCount < 1 && managementCount < 1) {
            new UserSeeder(this.myRoleFacade, this.myUserFacade, this.customerInfoFacade, this.jobseekerInfoFacade).seed();
        }
    }

    public void seed() {
        this.truncate().seedJobs().seedApplications().seedComments().seedReports().seedFeedbacks().seedWarnings();
    }

    public AppSeeder truncate() {
        this.commentFacade.truncate();
        this.feedbackFacade.truncate();
        this.warningFacade.truncate();
        this.applcationFacade.truncate();
        this.myJobFacade.truncate();
        this.reportFacade.truncate();
        return this;
    }

    public AppSeeder seedJobs() {
        List<MyUser> customers = this.myUserFacade.findByRole("Customer");
        ArrayList<MyJob> jobs = new ArrayList<MyJob>();
        Random random = new Random();
        int number = 20 + random.nextInt(90);
        for (int i = 1; i <= number; i++) {
            MyUser customer = customers.get(random.nextInt(customers.size()));
            Date dueDate = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
            jobs.add(new MyJob("title" + i, "description" + i, random.nextInt(2) == 1, new Date(), dueDate, customer));
        }
        jobs.forEach(this.myJobFacade::create);
        return this;
    }

    public AppSeeder seedApplications() {
        List<MyUser> jobseekers = this.myUserFacade.findByRole("Jobseeker");
        List<MyJob> jobs = this.myJobFacade.findAll();
        List<String> statusList = new ArrayList<>(Arrays.asList(ApplicationStatus.APPROVED.getStatus(), ApplicationStatus.PENDING.getStatus(), ApplicationStatus.REJECTED.getStatus(), ApplicationStatus.INTERVIEW.getStatus()));
        if (jobs.size() < 1) {
            this.seedJobs();
            jobs = this.myJobFacade.findAll();
        }
        ArrayList<Application> applications = new ArrayList();
        Random random = new Random();
        int number = 20 + random.nextInt(90);
        for (int i = 1; i <= number; i++) {
            MyJob job = jobs.get(random.nextInt(jobs.size()));
            MyUser jobseeker = jobseekers.get(random.nextInt(jobseekers.size()));
            String status = statusList.get(random.nextInt(statusList.size()));
            applications.add(new Application(status, "description" + i, new Date(), job, jobseeker));
        }
        applications.forEach(this.applcationFacade::create);
        return this;
    }

    public AppSeeder seedComments() {
        List<MyUser> jobseekers = this.myUserFacade.findByRole("Jobseeker");
        List<MyUser> cusomers = this.myUserFacade.findByRole("Customer");

        ArrayList<Comment> comments = new ArrayList();
        Random random = new Random();
        int number = 20 + random.nextInt(90);
        for (int i = 1; i <= number; i++) {
            MyUser customer = cusomers.get(random.nextInt(cusomers.size()));
            MyUser jobseeker = jobseekers.get(random.nextInt(jobseekers.size()));
            comments.add(new Comment("Comment number" + i, 3, new Date(), new Date(), customer, jobseeker));
        }
        comments.forEach(this.commentFacade::create);
        return this;
    }

    public AppSeeder seedFeedbacks() {
        List<MyUser> jobseekers = this.myUserFacade.findByRole("Jobseeker");
        List<MyUser> customers = this.myUserFacade.findByRole("Customer");
        List<String> types = new ArrayList<>(Arrays.asList(FeedbackType.POSITIVE.getType(), FeedbackType.NEGATIVE.getType()));

        Random random = new Random();
        ArrayList<Feedback> feedbacks = new ArrayList();
        int number = 20 + random.nextInt(90);
        for (int i = 1; i < number; i++) {
            MyUser jobseeker = jobseekers.get(random.nextInt(jobseekers.size()));
            MyUser customer = customers.get(random.nextInt(customers.size()));
            String type = types.get(random.nextInt(types.size()));
            feedbacks.add(new Feedback(type, "feadback number" + i, new Date(), jobseeker, customer));
        }
        feedbacks.forEach(this.feedbackFacade::create);
        return this;
    }

    public AppSeeder seedWarnings() {

        List<MyUser> jobseekers = this.myUserFacade.findByRole("Jobseeker");
        List<MyUser> managements = this.myUserFacade.findByRole("Management");

        Random random = new Random();
        ArrayList<Warning> warnings = new ArrayList();
        int number = 20 + random.nextInt(90);
        for (int i = 1; i <= number; i++) {
            MyUser jobseeker = jobseekers.get(random.nextInt(jobseekers.size()));
            MyUser management = managements.get(random.nextInt(managements.size()));
            warnings.add(new Warning(new Date(), management, jobseeker));
        }
        warnings.forEach(this.warningFacade::create);
        return this;
    }

    public AppSeeder seedReports() {

        List<String> reports = new ArrayList<>(Arrays.asList(ReportType.APPLICATIONS.getType(), ReportType.COMMENT_FEEDBACK_ANALYSIS.getType(), ReportType.CUSTOMERS.getType(), ReportType.JOBS.getType(), ReportType.JOBSEEKERS.getType(), ReportType.WARNINGS.getType()));

        reports.forEach(report -> {
            reportFacade.create(new Report(report, new Date(), new Date()));
        });

        return this;
    }

}
