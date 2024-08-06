<%-- 
    Document   : create
    Created on : Jul 14, 2024, 10:40:50 AM
    Author     : saleh
--%>

<%@page import="middlewares.Gate"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="helpers.Route"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.ReportType"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="java.util.Arrays"%>
<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <jsp:include page="../includes/style.jsp"/> 
    </head>
<body>
    <jsp:include page="../includes/nav.jsp"/> 
    <%Gate.authorise(request, response, "Create Report");%>
    <%
        out.print(NotificationHelper.displayNotifications(request));
    %>
    <%
        out.println(  new ContentCreator()
            .start(ContentCreator.Wrapper.DIV, "flex justify-end p-4", "")
                .action("<i class='fas fa-home mr-2'></i>main", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.REPORTS.getPath()).add(JspFile.INDEX.getPath()).get(), "", "", "bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-2 rounded", "")
                .end(ContentCreator.Wrapper.DIV)
                .start(ContentCreator.Wrapper.DIV, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg", "")
                .header("Create User", 2, "text-2xl font-bold mb-6 text-center", "")
                .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.REPORTS.getPath()).add(ServletFile.CREATE.getPath()).get(), "", "")
                .select("type", Arrays.asList(ReportType.APPLICATIONS.getType(),ReportType.COMMENT_FEEDBACK_ANALYSIS.getType(),ReportType.JOBS.getType(),ReportType.JOBSEEKERS.getType(),ReportType.CUSTOMERS.getType(), ReportType.WARNINGS.getType(), ReportType.GenderAndAge.getType()), "", "", "")
                .start(ContentCreator.Wrapper.DIV, "", "")
                .label("Start Date:","","", "startDate")
                .input("start_date", "date", "", "startDate", true, "", "")
                .end(ContentCreator.Wrapper.DIV)     
                .start(ContentCreator.Wrapper.DIV, "", "")
                .label("End Date:","","", "endDate")
                .input("end_date", "date", "", "endDate", true, "", "")
                .end(ContentCreator.Wrapper.DIV)
                .space(20)
                .submit("Create", "w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4", "")
                .end(ContentCreator.Wrapper.FORM)
                .end(ContentCreator.Wrapper.DIV)
                .space(20)
                .getContent()
        );
    %>
</body>
</html>
