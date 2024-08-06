<%-- 
    Document   : create
    Created on : Jul 4, 2024, 1:53:28 PM
    Author     : saleh
--%>

<%@page import="helpers.NotificationHelper"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Route"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.reflect.Array"%>
<%@page import="java.util.List"%>
<%@page import="model.EJB.MyUserFacade"%>
<%@page import="javax.ejb.EJB"%>
<%@page import="model.EJB.MyJobFacade"%>
<%@page import="model.MyUser"%>
<%@page import="helpers.Auth"%>
<%@page import="middlewares.Gate"%>
<%@page import="middlewares.RedirectAfterLogin"%>
<%@page import="java.util.Arrays"%>
<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Job</title>
    <jsp:include page="../includes/style.jsp"/>
</head>
<body>
    
    <%  Gate.authorise(request, response,"Create Job"); %>
    <jsp:include page="../includes/nav.jsp"/> 
    <%
       out.print(NotificationHelper.displayNotifications(request));
    %>
 
    <%
        MyUser user = Auth.user(request);
        List<List<String>> customers =  (List<List<String>>)request.getAttribute("customers");

        out.println(
            new ContentCreator()
            .start(ContentCreator.Wrapper.DIV, "flex justify-end p-4", "")
            .action("<i class='fas fa-home mr-2'></i>Main", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.INDEX.getPath()).get(), "Customer", user.getRole().getName(), "mr-2 bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded", "")
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.CONTAINER, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg overflow-hidden mb-8", "")
            .header("Create Job", 2, "text-center text-2xl text-gray-600 font-bold mb-6","")
            .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.JOBS.getPath()).add(ServletFile.CREATE.getPath()).get(), "space-y-4  max-h-80", "")
            .input("title", "text", "","Enter job title", true, "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
            .select("status", Arrays.asList("Active", "Inactive"), "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "", "")
            .selectList("customer",customers , "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "", "Admin", user.getRole().getName())
            .input("dueDate", "date", "","", true, "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
            .textArea("text", "description","shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
            .submit("Create Job", "w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4", "")
            .end(ContentCreator.Wrapper.FORM)
            .end(ContentCreator.Wrapper.CONTAINER)
            .getContent()
        );
    %>
</body>
</html>
