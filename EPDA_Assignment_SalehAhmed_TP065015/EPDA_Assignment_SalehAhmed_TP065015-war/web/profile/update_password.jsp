<%-- 
    Document   : update_password
    Created on : Jul 5, 2024, 4:49:47 PM
    Author     : saleh
--%>

<%@page import="helpers.NotificationHelper"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Route"%>
<%@page import="middlewares.Guest"%>
<%@page import="helpers.Auth"%>
<%@page import="model.MyUser"%>
<%@page import="helpers.ContentCreator" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Password</title>
        <jsp:include page="../includes/style.jsp"/> 
    </head>
    <body>
        <% Guest.authorise(request, response); %>
        <jsp:include page="../includes/nav.jsp"/> 
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            MyUser user = Auth.user(request); 
            out.println(
                new ContentCreator()
                .start(ContentCreator.Wrapper.DIV, "flex justify-end p-4", "")
                .deleteButtonWithPopup("<i class='fas fa-trash mr-2'></i>Delete Account", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.PROFILES.getPath()).add(ServletFile.DELETE.getPath()).get(), "user", "user", "mr-2 bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded", "")
                .action("<i class='fas fa-edit mr-2'></i>Update", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.PROFILES.getPath()).add(ServletFile.UPDATE.getPath()).get(), "", "", "mr-2 bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded mr-2 ", "")
                .action("<i class='fas fa-eye mr-2'></i>View Profile", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.PROFILE.getPath()).add(JspFile.VIEW.getPath()).get(), "user", "user", "mr-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "")
                .end(ContentCreator.Wrapper.DIV)
                .start(ContentCreator.Wrapper.CONTAINER, "relative bg-white p-6 rounded-lg shadow-lg w-full max-w-md mx-auto mt-10", "")
                .header("Update Password", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
                .form("POST", "/EPDA_Assignment_SalehAhmed_TP065015-war/Profiles/UpdatePassword"+ "", "", "")
                .input("oldPassword", "password", "", "Old Password", true, "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
                .input("newPassword", "password", "", "New Password", true, "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
                .input("confirmPassword", "password", "", "Confirm Password", true, "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
                .space(20)
                .submit("Save Changes", "w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4", "")
                .end(ContentCreator.Wrapper.FORM)
                .end(ContentCreator.Wrapper.CONTAINER)
                .getContent()
            );
        %>
    </body>
</html>