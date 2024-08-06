<%-- 
    Document   : view
    Created on : Jul 4, 2024, 12:37:54 PM
    Author     : saleh
--%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="middlewares.Gate"%>
<%@page import="model.Feedback"%>
<%@page import="model.MyUser"%>
<%@page import="helpers.Auth"%>
<%@page import="helpers.Route"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="helpers.ContentCreator"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Feedback</title>
    <jsp:include page="../includes/style.jsp"/> 
</head>
<body>
    <jsp:include page="../includes/nav.jsp"/> 
    <%Gate.authorise(request, response, "Read Feedback");%>
    <%
        out.print(NotificationHelper.displayNotifications(request));
    %>
    <%
        MyUser user = Auth.user(request);  
        Feedback feedback = (Feedback)request.getAttribute("feedback"); 
  
        out.println(
            new ContentCreator()
            .start(ContentCreator.Wrapper.DIV, "flex justify-end p-4", "")
            .action("<i class='fas fa-eye mr-2'></i>Jobseeker", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.VIEW.getPath()).get()+ "?id="+ feedback.getJobseeker().getId(), "Customer", user.getRole().getName(), "mr-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-2 rounded mr-2", "")
            .deleteButtonWithPopup("<i class='fas fa-eye mr-2'></i>Delete", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.FEEDBACK.getPath()).add(ServletFile.DELETE.getPath()).get() + "?id="+ feedback.getId(), "Customer", user.getRole().getName(), "mr-2 bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-2 rounded", "")
            .action("<i class='fas fa-home mr-2'></i>Main", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.FEEDBACK.getPath()).add(ServletFile.INDEX.getPath()).get(), "", "", "bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-2 rounded", "")
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.CONTAINER, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg", "")
            .header("View Feedback", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
            .start(ContentCreator.Wrapper.DIV, "text-center", "")
            .header(feedback.getCustomer().getName(), 3, "text-xl font-semibold mt-4", "")
            .end(ContentCreator.Wrapper.DIV)
            .space(10)
            .start(ContentCreator.Wrapper.DIV, "", "")
            .paragraph("Creator Name: " + feedback.getCustomer().getName(), "text-left text-lg text-gray-700 mt-4 border-b border-gray-200 pb-2", "")
            .paragraph("Receiver Name: " + feedback.getJobseeker().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
            .paragraph("Content: " + feedback.getContent(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
            .paragraph("Date: " + feedback.getCreatedAt(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
            .end(ContentCreator.Wrapper.DIV)
            .end(ContentCreator.Wrapper.CONTAINER)
            .getContent()
        );
    %>
</body>
</html>
