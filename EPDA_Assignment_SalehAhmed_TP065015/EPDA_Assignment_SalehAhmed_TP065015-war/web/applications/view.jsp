<%-- 
    Document   : view
    Created on : Jul 9, 2024, 7:07:57 AM
    Author     : saleh
--%>

<%@page import="helpers.NotificationHelper"%>
<%@page import="helpers.HttpHelper"%>
<%@page import="controllers.enums.ApplicationStatus"%>
<%@page import="middlewares.Gate"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="helpers.ContentCreator"%>
<%@page import="helpers.Route"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="helpers.Auth"%>
<%@page import="model.MyUser"%>
<%@page import="model.Application"%>
<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page import="javax.servlet.http.HttpServletResponse"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Application</title>
        <jsp:include page="../includes/style.jsp"/>
    </head>
    <body>
        <%
            Gate.authorise(request, response, "Read Application");
        %>
        <jsp:include page="../includes/nav.jsp"/> 
        <%
            MyUser user = Auth.user(request);
            Application myApplication = (Application) request.getAttribute("application");

            boolean comment = HttpHelper.getSession(request, "Comment") == null ? false : true;

            if (comment) {
                out.println(
                        new ContentCreator()
                            .popupComment(myApplication.getJob().getCustomer().getId().toString(), myApplication.getId().toString())
                            .getContent()
                );
                HttpHelper.removeSession(request, "Comment");
            } else {
                out.print(NotificationHelper.displayNotifications(request));
            }
        %>
        
        <%
            if (application != null) {
                out.println(
                        new ContentCreator()
                                .start(ContentCreator.Wrapper.DIV, "flex justify-end p-4", "")
                                .action("<i class='fas fa-eye mr-2'></i>View Job", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.VIEW.getPath()).get() + "?id=" + myApplication.getJob().getId(), "", "", "mr-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-2 rounded", "")
                                .action("<i class='fas fa-user mr-2'></i>View Job Seeker", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.VIEW.getPath()).get() + "?id=" + myApplication.getJobSeeker().getId(), "Customer", user.getRole().getName(), "mr-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-2 rounded", "")
                                .action("<i class='fas fa-user mr-2'></i>Accept Offer", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type=" + ApplicationStatus.INTERVIEW.getStatus() + "&id=" + myApplication.getId(), "Jobseeker" + (myApplication.getStatus().equals(ApplicationStatus.APPROVED.getStatus())), user.getRole().getName() + "true", "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-2 rounded mr-2", "")
                                .action("<i class='fas fa-user mr-2'></i>Reject Offer", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type=" + ApplicationStatus.DELETED.getStatus() + "&id=" + myApplication.getId(), "Jobseeker" + (myApplication.getStatus().equals(ApplicationStatus.APPROVED.getStatus())), user.getRole().getName() + "true", "mr-2 bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-2 rounded", "")
                                .deleteButtonWithPopup("<i class='fas fa-trash mr-2'></i>Delete Application", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.DELETE.getPath()).get() + "?id=" + myApplication.getId(), "Customer", user.getRole().getName(), "mr-2 bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-2 rounded", "")
                                .end(ContentCreator.Wrapper.DIV)
                                .space(20)
                                .start(ContentCreator.Wrapper.CONTAINER, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg", "")
                                .header("Application Details", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
                                .start(ContentCreator.Wrapper.DIV, "text-center", "")
                                .paragraph("Application ID: " + myApplication.getId(), "text-left text-lg text-gray-700 mt-4 border-b border-gray-200 pb-2", "")
                                .paragraph("Job Seeker Name: " + myApplication.getJobSeeker().getUserName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                                .paragraph("Job Title: " + myApplication.getJob().getTitle(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                                .paragraph("Created At: " + myApplication.getCreatedAt(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                                .paragraph("Status: " + myApplication.getStatus(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                                .paragraph("Self Description: " + myApplication.getSelfDescription(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                                .end(ContentCreator.Wrapper.DIV)
                                .end(ContentCreator.Wrapper.CONTAINER)
                                .getContent()
                );
            } else {
                out.println("<p>Application not found.</p>");
            }
        %>
    </body>

</html>
