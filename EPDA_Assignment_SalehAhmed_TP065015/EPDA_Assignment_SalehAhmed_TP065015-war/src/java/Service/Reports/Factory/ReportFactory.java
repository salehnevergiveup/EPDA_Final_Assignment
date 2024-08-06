/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service.Reports.Factory;

import Service.Reports.DTO.ReportsDTO;
import controllers.enums.ReportType;
import java.util.Date;
import javax.ejb.EJB;
import model.EJB.ApplicationFacade;
import model.EJB.CommentFacade;
import model.EJB.FeedbackFacade;
import model.EJB.JobseekerInfoFacade;
import model.EJB.MyJobFacade;
import model.EJB.MyRoleFacade;
import model.EJB.MyUserFacade;
import model.EJB.PermissionFacade;
import model.EJB.WarningFacade;

/**
 *
 * @author saleh
 */
public class ReportFactory {

    Reports report;
    public final ApplicationFacade applcationFacade;
    public final CommentFacade commentFacade;
    public final FeedbackFacade feedbackFacade;
    public final MyJobFacade myJobFacade;
    public final WarningFacade warningFacade;
    public final MyUserFacade myUserFacade;
    public final JobseekerInfoFacade jobseekerInfoFacade; 

    public ReportFactory(MyUserFacade myUserFacade, ApplicationFacade applcationFacade, CommentFacade commentFacade, FeedbackFacade feedbackFacade, MyJobFacade myJobFacade, WarningFacade warningFacade,JobseekerInfoFacade jobseekerInfoFacade) {
        this.applcationFacade = applcationFacade;
        this.commentFacade = commentFacade;
        this.feedbackFacade = feedbackFacade;
        this.myJobFacade = myJobFacade;
        this.warningFacade = warningFacade;
        this.myUserFacade = myUserFacade;
        this.jobseekerInfoFacade =jobseekerInfoFacade;
    }

    public ReportsDTO generateReport(Date start, Date end, String type) {
        System.out.println("test factory 1");
        if (ReportType.COMMENT_FEEDBACK_ANALYSIS.getType().equals(type)) {
            report = new AnalysisReport(type, start, end, feedbackFacade, commentFacade);
        } else if (ReportType.APPLICATIONS.getType().equals(type)) {

            report = new ApplicationReport(type, start, end, applcationFacade);

        } else if (ReportType.CUSTOMERS.getType().equals(type)) {

            report = new CustomerReport(type, start, end, myUserFacade);

        } else if (ReportType.JOBSEEKERS.getType().equals(type)) {

            report = new JobseekersReport(type, start, end, myUserFacade);

        } else if (ReportType.WARNINGS.getType().equals(type)) {

            report = new WarningsReport(type, start, end, warningFacade);

        } else if (ReportType.JOBS.getType().equals(type)) {

            report = new JobsReport(type, start, end, myJobFacade);

        } else if (ReportType.GenderAndAge.getType().equals(type)) {
            
            report = new GenderAndAge(type, start, end, myUserFacade, jobseekerInfoFacade);
        }
        System.out.println("report: " + report.generateReport().getDetails());
        return report == null ? null : report.generateReport();
    }
}
