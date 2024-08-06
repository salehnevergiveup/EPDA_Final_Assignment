<%-- 
    Document   : index
    Created on : Jul 4, 2024, 1:53:10 PM
    Author     : saleh
--%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="helpers.HttpHelper"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="middlewares.Gate"%>
<%@page import="helpers.Auth"%>
<%@page import="model.MyUser"%>
<%@page import="java.util.Date"%>
<%@page import="model.MyJob"%>
<%@page import="model.Model"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Jobs</title>
        <jsp:include page="../includes/style.jsp"/> 
    </head>
    <body>
        <% Gate.authorise(request, response, "Read Job");%>
        <jsp:include page="../includes/nav.jsp"/> 
        <%
           out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            MyUser user = Auth.user(request);
            List<Map<String,String>> jobs = (List<Map<String,String>>) request.getAttribute("jobs");
            List<List<String>> jobActions = Arrays.asList(
                                                            Arrays.asList("", "<i class='fas fa-eye mr-2'></i>View", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.VIEW.getPath()).get(), "", "", "true", "text-blue-500", ""),
                                                            Arrays.asList("", "<i class='fas fa-edit mr-2'></i>Update", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.UPDATE.getPath()).get(), "Customer", user.getRole().getName(), "true", "text-yellow-500", ""),
                                                            Arrays.asList("", "<i class='fas fa-check mr-2'></i>Apply",new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.APPLICATIONS.getPath()).add(JspFile.CREATE.getPath()).get(), "Jobseeker", user.getRole().getName(), "true", "text-green-500", "")
                                                         );
            out.println(
                    new ContentCreator()
                    .start(ContentCreator.Wrapper.DIV, "flex justify-end mb-4 mr-2", "")
                    .action("<i class='fas fa-plus mr-2'></i>Create Job", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.JOBS.getPath()).add(JspFile.CREATE.getPath()).get(), "Customer", user.getRole().getName(), "bg-green-500 hover:bg-green-700 text-white font-bold px-2 py-2 rounded", "")
                    .end(ContentCreator.Wrapper.DIV)
                    .start(ContentCreator.Wrapper.CONTAINER, "relative bg-white p-4 rounded-lg shadow-lg w-full max-w-4xl mx-auto mb-8", "")
                    .header("Job Listings", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
                    .start(ContentCreator.Wrapper.DIV, "", "")
                    .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get(), "flex w-1/2 mx-auto flex-col", "")
                    .input("query", "text", "", "query", true, "", "")
                    .start(ContentCreator.Wrapper.DIV, "flex w-full justify-center mt-2", "")
                    .action("search", "", "", "", "", "margin-right: 5px")
                    .link("Reset", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get(),"bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "", true)
                    .end(ContentCreator.Wrapper.DIV)
                    .end(ContentCreator.Wrapper.FORM)
                    .end(ContentCreator.Wrapper.DIV)
                    .table(
                            "Jobs",
                            Arrays.asList("Title", "Status", "Created_Date", "Updated_Date", "Due_Date", "Company_Name", "Actions"),
                            jobs,
                            jobActions,
                            "max-w-full rounded-lg mb-4 overflow-y-auto border-2 border-gray-100 max-h-[700px]",
                            ""
                    )
                    .getContent()
            );
        %>
    </body>
</html>