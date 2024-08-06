<%-- 
    Document   : update
    Created on : Jul 4, 2024, 1:53:40 PM
    Author     : saleh
--%>

<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="helpers.HttpHelper"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="java.util.List"%>
<%@page import="model.MyUser"%>
<%@page import="helpers.Auth"%>
<%@page import="middlewares.Gate"%>
<%@page import="model.MyJob"%>
<%@page import="java.util.Arrays"%>
<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Job</title>
    <jsp:include page="../includes/style.jsp"/>
</head>
<body>
    <% Gate.authorise(request, response, "Update Job"); %> 
    <jsp:include page="../includes/nav.jsp"/> 
    <%
     out.print(NotificationHelper.displayNotifications(request));
    %>
    <%
        MyUser user = Auth.user(request); 
        MyJob job = (MyJob)request.getAttribute("job");

        out.println(
            new ContentCreator()
            .start(ContentCreator.Wrapper.DIV, "flex justify-end mb-4 mr-2", "")
            .deleteButtonWithPopup("<i class='fas fa-trash mr-2'></i>Delete Job", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.DELETE.getPath()) + "?id=" + job.getId(), "Customer", user.getRole().getName(), "bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded mr-2", "")
            .action("<i class='fas fa-eye mr-2'></i>View Job", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.VIEW.getPath()) + "?id=" + job.getId(), "", "", "mr-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-2 rounded mr-2", "")
            .action("<i class='fas fa-plus mr-2'></i>Create Job", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.JOBS.getPath()).add(JspFile.CREATE.getPath()).get(), "Customer", user.getRole().getName(), "mr-2 bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-2  rounded", "")
            .action("<i class='fas fa-arrow-left mr-2'></i>Jobs", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get(), "Jobseeker", user.getRole().getName(), "mr-2 bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-2  rounded", "")
            .end(ContentCreator.Wrapper.DIV)
           .start(ContentCreator.Wrapper.CONTAINER, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg", "")
            .header("Update Job", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
            .form("POST",new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?id=" + job.getId(), "space-y-4", "")
            .input("title", "text", job.getTitle(), "",true, "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
            .select("status", Arrays.asList("Active", "Inactive"), "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "", "")
            .input("dueDate", "date", job.getDueDate().toString(),"",true, "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" , "")
            .textArea( "text",job.getDescription(), "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
            .submit("Update Job", "w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4", "")
            .end(ContentCreator.Wrapper.FORM)
            .end(ContentCreator.Wrapper.CONTAINER)
            .getContent()
        );
    %>
</body>
</html>
