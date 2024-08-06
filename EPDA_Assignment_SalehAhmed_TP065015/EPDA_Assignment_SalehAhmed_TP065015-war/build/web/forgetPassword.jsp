<%-- 
    Document   : forgetPassword
    Created on : Aug 7, 2024, 1:33:20 AM
    Author     : saleh
--%>

<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="helpers.Route"%>
<%@page import="middlewares.Gate"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="helpers.ContentCreator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
  <jsp:include page="includes/style.jsp"/> 
</head>
<body>
 
     <div class="min-h-screen flex items-center justify-center bg-gray-100">
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            out.println(
                new ContentCreator()
                .start(ContentCreator.Wrapper.CONTAINER, "", "")
                .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.FORGETPASSWORD.getPath()).get(), "bg-white p-6 rounded-lg shadow-lg w-full max-w-md mx-auto", "")
                .header("Forget Password", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
                .input("email", "email", "", "Enter your email", true, "border rounded p-2 w-full", "")
                .space(20)
                .start(ContentCreator.Wrapper.DIV, "text-right mt-2", "")
                .link("Login to your account? Login!", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspFile.LOGIN.getPath()).get(), "text-blue-500 hover:underline", "", false)
                .end(ContentCreator.Wrapper.DIV)
                .space(15)
                .submit("Submit", "w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4", "")
                .end(ContentCreator.Wrapper.FORM)
                .end(ContentCreator.Wrapper.CONTAINER)
                .getContent()
            );
        %>
    </div>
</body>
</html>
