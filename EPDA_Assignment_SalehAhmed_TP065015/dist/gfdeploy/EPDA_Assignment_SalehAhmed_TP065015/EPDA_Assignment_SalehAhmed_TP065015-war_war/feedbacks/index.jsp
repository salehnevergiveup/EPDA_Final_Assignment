<%-- 
    Document   : index
    Created on : Jul 4, 2024, 12:21:56 PM
    Author     : saleh
--%>
<%@page import="model.MyUser"%>
<%@page import="helpers.Auth"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="middlewares.Gate"%>
<%@page import="helpers.HttpHelper"%>
<%@page import="java.util.Map"%>
<%@page import="model.Model"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Feedback</title>
    <jsp:include page="../includes/style.jsp"/> 
</head>
<body>
    <jsp:include page="../includes/nav.jsp"/> 
    <%Gate.authorise(request, response, "Read Feedback");%>
    <%
        out.print(NotificationHelper.displayNotifications(request));
    %>
    <%  
        MyUser user  = Auth.user(request);
        List<Map<String,String>> feedbackList =   (List<Map<String,String>>)request.getAttribute("feedbacks"); 
        List<List<String>> feedbackActions = Arrays.asList(
            Arrays.asList("","<i class='fas fa-eye mr-2'></i>View", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.FEEDBACK.getPath()).add(ServletFile.VIEW.getPath()).get(), "", "", "false", "text-blue-500 hover:underline", ""),
            Arrays.asList("jobseeker_id","<i class='fas fa-eye mr-2'></i>View Jobseeker", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.VIEW.getPath()).get(), "Customer", user.getRole().getName(), "false", "text-blue-500 hover:underline", "")
        );
        out.println(
            new ContentCreator()
            .start(ContentCreator.Wrapper.CONTAINER, "relative bg-white p-6 rounded-lg shadow-lg w-full max-w-4xl mx-auto mt-16", "")
            .header("Feedback List", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
            .start(ContentCreator.Wrapper.DIV, "", "")
            .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.FEEDBACK.getPath()).add(ServletFile.INDEX.getPath()).get(), "flex w-1/2 mx-auto flex-col", "")
            .input("query", "text", "", "query", true, "", "")
            .start(ContentCreator.Wrapper.DIV, "flex w-full justify-center mt-2", "")
            .action("search", "", "", "", "", "margin-right: 5px")
            .link("Reset", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.FEEDBACK.getPath()).add(ServletFile.INDEX.getPath()).get(), "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "", true)
            .end(ContentCreator.Wrapper.DIV)
            .end(ContentCreator.Wrapper.FORM)
            .end(ContentCreator.Wrapper.DIV)
            .table(
                "Feedback",
                Arrays.asList("Company_Name", "Jobseeker_Name", "Content", "Created_Date", "Action"),
                feedbackList,
                feedbackActions,
                "min-w-full leading-normal",
                ""
            )
            .getContent()
        );
    %>
</body>
</html>