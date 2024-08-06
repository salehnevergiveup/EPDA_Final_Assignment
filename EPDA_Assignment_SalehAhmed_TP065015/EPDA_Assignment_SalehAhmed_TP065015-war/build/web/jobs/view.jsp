<%-- 
    Document   : view
    Created on : Jul 4, 2024, 1:53:50 PM
    Author     : saleh
--%>

<%@page import="helpers.NotificationHelper"%>
<%@page import="helpers.HttpHelper"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="model.Comment"%>
<%@page import="java.util.List"%>
<%@page import="model.MyUser"%>
<%@page import="helpers.Auth"%>
<%@page import="model.MyJob"%>
<%@page import="middlewares.Gate"%>
<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>View Job</title>
        <jsp:include page="../includes/nav.jsp"/> 
    </head>
    <body>
        <%  Gate.authorise(request, response, "Read Job"); %>
        <jsp:include page="../includes/style.jsp"/>
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            MyUser user = Auth.user(request);
            MyJob job = (MyJob) request.getAttribute("job");
            String comapnyName =  (String) request.getAttribute("companyName");
            out.println(
                    new ContentCreator()
                            .start(ContentCreator.Wrapper.DIV, "", "")
                            .deleteButtonWithPopup("<i class='fas fa-trash mr-2'></i>Delete Job",new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.DELETE.getPath()).get()+ "?id="  + job.getId(), "Customer", user.getRole().getName(), "bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-2 rounded mr-2", "")
                            .action("<i class='fas fa-edit mr-2'></i>Update Job", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?id=" + job.getId(), "Customer", user.getRole().getName(), "mr-2 bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-2  rounded mr-2", "")
                            .action("<i class='fas fa-plus mr-2'></i>Create Job", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.JOBS.getPath()).add(JspFile.CREATE.getPath()).get(), "Customer", user.getRole().getName(), "mr-2 bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-2  rounded", "")
                            .action("<i class='fas fa-plus mr-2'></i>Apply Now", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.APPLICATIONS.getPath()).add(JspFile.CREATE.getPath()).get() + "?id=" + job.getId(), "Jobseeker"  , user.getRole().getName(), "mr-2 bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-2  rounded", "")
                            .end(ContentCreator.Wrapper.DIV)
                            .end(ContentCreator.Wrapper.DIV)
                            .space(20)
                            .start(ContentCreator.Wrapper.CONTAINER, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg", "")
                            .header("Job Details", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
                            .start(ContentCreator.Wrapper.DIV, "text-center", "")
                            .paragraph("Title: " + job.getTitle(), "text-left text-lg text-gray-700 mt-4 border-b border-gray-200 pb-2", "")
                            .paragraph("Description: " + job.getDescription(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Status: Active " + job.getStatus(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Created At: " + job.getCreatedAt(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Updated At: " + job.getUpdatedAt(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Due Date: " + job.getDueDate(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Company Name: " + comapnyName, "text-left text-lg text-gray-700 mt-2", "")
                            .end(ContentCreator.Wrapper.DIV)
                            .end(ContentCreator.Wrapper.CONTAINER)
                            .space(60)
                            .getContent()
            );
        %>
       
</body>
</html>
