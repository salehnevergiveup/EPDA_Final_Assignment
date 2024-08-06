<%-- 
    Document   : apply
    Created on : Jul 7, 2024, 8:44:40 PM
    Author     : saleh
--%>SSSW

<%@page import="helpers.NotificationHelper"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Route"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.HttpHelper"%>
<%@page import="middlewares.Gate"%>
<%@page import="java.util.Arrays"%>
<%@page import="helpers.Auth"%>
<%@page import="model.MyUser"%>
<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Apply for Job</title>
    <jsp:include page="../includes/style.jsp"/>
</head>
<body>

    <%Gate.authorise(request, response, "Create Application");%>
    <jsp:include page="../includes/nav.jsp"/> 
    <%
     out.print(NotificationHelper.displayNotifications(request));
    %>
    <% 
        MyUser user = Auth.user(request);
        out.println(
            new ContentCreator()
            .start(ContentCreator.Wrapper.DIV, "flex justify-end p-4", "")
            .action("<i class='fas fa-arrow-left mr-2'></i>Jobs", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get(), "Jobseeker", user.getRole().getName(), "bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded", "")
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.CONTAINER, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg overflow-hidden mb-8", "")
            .header("Apply for Job", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
            .form("post", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.CREATE.getPath()).get(), "space-y-4", "id='applyForm'")
            .input("id", "hidden", HttpHelper.getParam(request, "id"), "", false, "", "")
            .input("application", "hidden", HttpHelper.getParam(request, "id"), "", false, "", "")
            .input("name", "text", "", "Enter your name", true, "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
            .textArea("selfDescription", "Enter a self-description", "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
            .submit("Submit", "w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4", "background-color: #3b82f6;")
            .end(ContentCreator.Wrapper.FORM)
            .end(ContentCreator.Wrapper.CONTAINER)
            .getContent()
        );
        
    %>
</body>
</html>