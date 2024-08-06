<%-- 
    Document   : index
    Created on : Jul 14, 2024, 12:21:18 AM
    Author     : saleh
--%>

<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Arrays"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.ContentCreator"%>
<%@page import="helpers.Auth"%>
<%@page import="java.util.List"%>
<%@page import="model.MyUser"%>
<%@page import="middlewares.Gate"%>
<%@page import="middlewares.Gate"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports</title>
    <jsp:include page="../includes/style.jsp"/> 
</head>
<body>
    <% Gate.authorise(request, response, "Read Report"); %>
    <jsp:include page="../includes/nav.jsp" /> 
    <%
       out.print(NotificationHelper.displayNotifications(request));
    %>
    <%
        MyUser user = Auth.user(request);
        List<Map<String,String>> reports = (List<Map<String,String>>) request.getAttribute("reports");
        List<List<String>> reportActions = Arrays.asList(
                                                            Arrays.asList("", "<i class='fas fa-eye mr-2'></i>View", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.REPORTS.getPath()).add(ServletFile.VIEW.getPath()).get(), "", "", "true", "text-blue-500", "")
                                                         );
        out.println(
            new ContentCreator()
            .start(ContentCreator.Wrapper.DIV, "flex justify-end mb-4 mr-2", "")
            .action("<i class='fas fa-plus mr-2'></i>Create Report", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.REPORTS.getPath()).add(JspFile.CREATE.getPath()).get(), "Management", user.getRole().getName(), "bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded", "")
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.CONTAINER, "relative bg-white p-4 rounded-lg shadow-lg w-full max-w-4xl mx-auto mb-8", "")
            .header("Report Listings", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
            .start(ContentCreator.Wrapper.DIV, "", "")
            .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.REPORTS.getPath()).add(ServletFile.INDEX.getPath()).get(), "flex w-1/2 mx-auto flex-col", "")
            .input("query", "text", "", "query", true, "", "")
            .start(ContentCreator.Wrapper.DIV, "flex w-full justify-center mt-2", "")
            .action("search", "", "", "", "", "margin-right: 5px")
            .link("Reset", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.REPORTS.getPath()).add(ServletFile.INDEX.getPath()).get(),"bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "", true)
            .end(ContentCreator.Wrapper.DIV)
            .end(ContentCreator.Wrapper.FORM)
            .end(ContentCreator.Wrapper.DIV)
            .table(
                "Reports",
                Arrays.asList("Name", "Start_Date", "End_Date", "Actions"),
                reports,
                reportActions,
                "max-w-full rounded-lg mb-4 overflow-y-auto border-2 border-gray-100 max-h-[700px]",
                ""
            )
            .getContent()
        );
    %>
</body>
