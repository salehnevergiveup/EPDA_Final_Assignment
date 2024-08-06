<%-- 
    Document   : index
    Created on : Jul 4, 2024, 4:30:44 PM
    Author     : saleh
--%>

<%@page import="middlewares.Gate"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="helpers.Auth"%>
<%@page import="java.util.Map"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Route"%>
<%@page import="model.Model"%>
<%@page import="model.Warning"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="model.MyUser"%>
<%@page import="model.Warning"%>
<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Warnings Index</title>
        <jsp:include page="../includes/style.jsp"/>
    </head>
    <body>
        <% Gate.authorise(request, response, "Read Warning");%>
        <jsp:include page="../includes/nav.jsp"/> 
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            List<Map<String, String>> warnings = (List<Map<String, String>>) request.getAttribute("warnings");
            MyUser user = Auth.user(request);

            List<List<String>> warningActions = Arrays.asList(
                Arrays.asList("jobseeker_id","<i class='fas fa-eye mr-2'></i>View User", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.VIEW.getPath()).get(), "Management", user.getRole().getName(), "false", "text-blue-500", "")
            );

            out.println(
                new ContentCreator()
                    .start(ContentCreator.Wrapper.CONTAINER, "relative bg-white p-6 rounded-lg shadow-lg w-full max-w-4xl mx-auto mt-16", "")
                    .header("Warnings", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
                .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.WARNINGS.getPath()).add(ServletFile.INDEX.getPath()).get(), "flex w-1/2 mx-auto flex-col", "")
                .input("query", "text", "", "query", true, "", "")
                .start(ContentCreator.Wrapper.DIV, "flex w-full justify-center mt-2", "")
                .action("search", "", "", "", "", "margin-right: 5px")
                .link("Reset", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.WARNINGS.getPath()).add(ServletFile.INDEX.getPath()).get(), "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "", true)
                .end(ContentCreator.Wrapper.DIV)
                .end(ContentCreator.Wrapper.FORM)

                    .start(ContentCreator.Wrapper.DIV, "overflow-x-auto", "")
                    .table(
                        "Warnings",
                        Arrays.asList("Management", "Jobseeker_Name", "Created_Date","Actions"),
                        warnings,
                        warningActions,
                        "min-w-full leading-normal",
                        ""
                    )
                    .end(ContentCreator.Wrapper.DIV)
                    .end(ContentCreator.Wrapper.DIV)
                    .end(ContentCreator.Wrapper.CONTAINER)
                    .getContent()
            );
        %>
    </body>
</html>

