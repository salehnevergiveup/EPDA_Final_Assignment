<%-- 
    Document   : index
    Created on : Jul 4, 2024, 4:08:50 PM
    Author     : saleh
--%>

<%@page import="helpers.NotificationHelper"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="middlewares.Gate"%>
<%@page import="java.util.Map"%>
<%@page import="helpers.Auth"%>
<%@page import="model.Model"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="model.MyJob"%>
<%@page import="model.MyUser"%>
<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Comments Index</title>
        <jsp:include page="../includes/style.jsp"/>
    </head>
    <body>
        <%Gate.authorise(request, response, "Read Comment");%>
        <jsp:include page="../includes/nav.jsp"/> 
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.COMMENTS.getPath()).add(ServletFile.VIEW.getPath()).get();
            MyUser user = Auth.user(request);
            List<Map<String,String>> comments = (List<Map<String,String>>) request.getAttribute("comments");
            System.out.println(comments);
            List<List<String>> commentActions = Arrays.asList(
                Arrays.asList("","<i class='fas fa-eye mr-2'></i>View Comment", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.COMMENTS.getPath()).add(ServletFile.VIEW.getPath()).get(), "admin", "admin", "false", "py-2 text-blue-500 hover:underline", ""),
                Arrays.asList("customer","<i class='fas fa-eye mr-2'></i>View Company", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.VIEW.getPath()).get(), "Jobseeker", user.getRole().getName(), "false", "py-2 text-blue-500 hover:underline", "")
            );
            out.println(
                new ContentCreator()
                    .start(ContentCreator.Wrapper.CONTAINER, "relative bg-white p-4 rounded-lg shadow-lg w-full max-w-4xl mx-auto mb-8", "")
                    .header("Comments", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
                    .start(ContentCreator.Wrapper.DIV, "", "")
                    .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.COMMENTS.getPath()).add(ServletFile.INDEX.getPath()).get(), "flex w-1/2 mx-auto flex-col", "")
                    .input("query", "text", "", "query", true, "", "")
                    .start(ContentCreator.Wrapper.DIV, "flex w-full justify-center mt-2", "")
                    .action("search", "", "", "", "", "margin-right: 5px")
                    .link("Rest", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.COMMENTS.getPath()).add(ServletFile.VIEW.getPath()).get(),"bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "", true)
                    .end(ContentCreator.Wrapper.DIV)
                    .end(ContentCreator.Wrapper.FORM)
                    .end(ContentCreator.Wrapper.DIV)
                    .table(
                        "Comments",
                        Arrays.asList("Jobseeker", "Company_Name", "Created_Date", "Updated_Date","Content", "Actions"),
                        comments,
                        commentActions,
                        "max-w-full rounded-lg mb-4 overflow-y-auto border-2 border-gray-100 max-h-[700px]",
                        ""
                    )
                    .getContent()
            );
        %>
    </body>
</html>
