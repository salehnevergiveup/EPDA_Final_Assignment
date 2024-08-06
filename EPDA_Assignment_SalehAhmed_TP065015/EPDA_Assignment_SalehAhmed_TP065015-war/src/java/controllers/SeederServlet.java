package controllers;

import controllers.enums.ServletFile;
import helpers.HttpHelper;
import helpers.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
/**
 * Servlet implementation class SeederServlet
 */
import seeders.AppSeeder;
import seeders.UserSeeder;

@WebServlet(name = "SeederServlet", urlPatterns = {"/SeederServlet"})
public class SeederServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private JobseekerInfoFacade jobseekerInfoFacade;

    @EJB
    private CustomerInfoFacade customerInfoFacade;
    @EJB
    private MyRoleFacade myRoleFacade;

    @EJB
    private MyUserFacade myUserFacade;

    @EJB
    public ApplicationFacade applcationFacade;

    @EJB
    public CommentFacade commentFacade;

    @EJB
    public FeedbackFacade feedbackFacade;
    @EJB
    public MyJobFacade myJobFacade;
    @EJB
    public WarningFacade warningFacade;
    @PersistenceContext(unitName = "EPDA_Assignment_SalehAhmed_TP065015-warPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;
    @EJB
    private ReportFacade reportFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            try {

                String seedType = request.getParameter("seedType");
                
                UserSeeder userSeeder = new UserSeeder(myRoleFacade, myUserFacade, customerInfoFacade, jobseekerInfoFacade);
                AppSeeder appSeeder = new AppSeeder(myRoleFacade, customerInfoFacade, jobseekerInfoFacade, myUserFacade, applcationFacade, commentFacade, feedbackFacade, myJobFacade, warningFacade, reportFacade);
                if ("warnings".equals(seedType)) {
                    appSeeder.seedWarnings();
                } else if ("users".equals(seedType)) {
                    userSeeder.seed();
                } else if ("applications".equals(seedType)) {
                    appSeeder.seedApplications();
                } else if ("jobs".equals(seedType)) {
                    appSeeder.seedJobs();
                } else if ("comments".equals(seedType)) {
                    appSeeder.seedComments();
                } else if ("reports".equals(seedType)) {
                    appSeeder.seedReports();
                }else if ("feedbacks".equals(seedType)) {
                    appSeeder.seedFeedbacks();
                } else if ("all".equals(seedType)) {
                    appSeeder.seed();
                }
                else if ("truncateAll".equals(seedType)) {
                    appSeeder.truncate();
                    userSeeder.truncate();
                }
                else {
                    HttpHelper.setSession(request, "Validation_Error", "Invalid Acction Please Try Again !!!!!");
                    HttpHelper.back(request, response);
                }

                HttpHelper.setSession(request, "Success_Message", seedType + " Data Seeded successfully");

                HttpHelper.redirectTo(request, response, new Route().add(ServletFile.DASHBOARD.getPath()).get());
            } catch (Exception e) {
                HttpHelper.setSession(request, "Validation_Error", "Error While Seeding the Data");
                HttpHelper.redirectTo(request, response, new Route().add(ServletFile.DASHBOARD.getPath()).get());
            }

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
}
