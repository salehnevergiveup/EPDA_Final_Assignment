<%-- 
    Document   : login
    Created on : Jul 2, 2024, 8:18:11 PM
    Author     : saleh
--%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="helpers.HttpHelper"%>  
<%@page import="helpers.ContentCreator" %>
<%@page import="middlewares.RedirectAfterLogin"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <jsp:include page="includes/style.jsp"/> 
    </head>
    <body>
        <% RedirectAfterLogin.handle(request, response);%>
        <div class="min-h-screen flex items-center justify-center bg-gray-100">
            <%
                out.print(NotificationHelper.displayNotifications(request));
            %>
            <%
                out.println(
                        new ContentCreator()
                                .start(ContentCreator.Wrapper.CONTAINER, "", "")
                                .form("POST","Login" ,  "bg-white p-6 rounded-lg shadow-lg w-full max-w-md mx-auto", "")
                                .header("Login", 2, "", "")
                                .input("username", "text", "","Username or Email", true, "", "")
                                .space(20)
                                .input("password", "password", "","Password", true, "", "")
                                .space(20)
                                .start(ContentCreator.Wrapper.DIV, "text-right mt-2", "")
                                .link("Forgot Password?", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspFile.FORGETPASSWORD.getPath()).get() , "", "", false)
                                .end(ContentCreator.Wrapper.DIV)
                                .space(15)
                                .submit("Login", "w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4", "")
                                .space(20)
                                .start(ContentCreator.Wrapper.DIV, "text-center mt-4", "")
                                .link("Need an account? Sign up now!", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspFile.REGISTER.getPath()).get(), "", "", false)
                                .end(ContentCreator.Wrapper.DIV)
                                .end(ContentCreator.Wrapper.FORM)
                                .end(ContentCreator.Wrapper.CONTAINER)
                                .getContent()
                );
            %>
        </div> 
    </body>
</html>
