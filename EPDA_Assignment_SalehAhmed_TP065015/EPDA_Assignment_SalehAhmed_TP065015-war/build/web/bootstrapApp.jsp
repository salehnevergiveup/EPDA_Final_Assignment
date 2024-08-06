<%-- 
    Document   : bootstrapApp
    Created on : Jul 2, 2024, 8:22:52 PM
    Author     : saleh
--%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="model.MyUser"%>
<%@page import="helpers.Auth"%>
<%@ page import="helpers.ContentCreator"%>
<%@ page import="helpers.Route"%>
<%@ page import="controllers.enums.BaseRoute"%>
<%@ page import="controllers.enums.ServletPackage"%>
<%@ page import="controllers.enums.ServletFile"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Seeder</title>
        <jsp:include page="/includes/style.jsp"/>
    </head>
    <body>
        <jsp:include page="/includes/nav.jsp"/>
        <%
            out.println(NotificationHelper.displayNotifications(request));
        %>
        <div class="container mx-auto mt-8">
            <h1 class="text-2xl font-bold mb-4">Data Seeder</h1>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <%
                    ContentCreator contentCreator = new ContentCreator();
                    MyUser user = Auth.user(request);

                    // Action for seeding Warnings
                    contentCreator.start(ContentCreator.Wrapper.DIV, "", "")
                            .action("<i class='fas fa-exclamation-triangle mr-2'></i>Seed Warnings",
                                    new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.BOOTSTRAP_APP.getPath()).get() + "?seedType=warnings",
                                    "Admin", user.getRole().getName(),
                                    "bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded-full flex items-center justify-center space-x-2", "")
                            .end(ContentCreator.Wrapper.DIV);

                    // Action for seeding Applications
                    contentCreator.start(ContentCreator.Wrapper.DIV, "", "")
                            .action("<i class='fas fa-clipboard-list mr-2'></i>Seed Applications",
                                    new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.BOOTSTRAP_APP.getPath()).get() + "?seedType=applications",
                                    "Admin", user.getRole().getName(),
                                    "bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded-full flex items-center justify-center space-x-2", "")
                            .end(ContentCreator.Wrapper.DIV);

                    // Action for seeding Jobs
                    contentCreator.start(ContentCreator.Wrapper.DIV, "", "")
                            .action("<i class='fas fa-briefcase mr-2'></i>Seed Jobs",
                                    new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.BOOTSTRAP_APP.getPath()).get() + "?seedType=jobs",
                                    "Admin", user.getRole().getName(),
                                    "bg-indigo-500 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded-full flex items-center justify-center space-x-2", "")
                            .end(ContentCreator.Wrapper.DIV);

                    // Action for seeding Comments
                    contentCreator.start(ContentCreator.Wrapper.DIV, "", "")
                            .action("<i class='fas fa-comments mr-2'></i>Seed Comments",
                                    new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.BOOTSTRAP_APP.getPath()).get() + "?seedType=comments",
                                    "Admin", user.getRole().getName(),
                                    "bg-pink-500 hover:bg-pink-700 text-white font-bold py-2 px-4 rounded-full flex items-center justify-center space-x-2", "")
                            .end(ContentCreator.Wrapper.DIV);
                    
                    // Action for seeding Feedbacks
                    contentCreator.start(ContentCreator.Wrapper.DIV, "", "")
                            .action("<i class='fas fa-comments mr-2'></i>Seed feedbacks",
                                    new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.BOOTSTRAP_APP.getPath()).get() + "?seedType=feedbacks",
                                    "Admin", user.getRole().getName(),
                                    "bg-pink-500 hover:bg-pink-700 text-white font-bold py-2 px-4 rounded-full flex items-center justify-center space-x-2", "")
                            .end(ContentCreator.Wrapper.DIV);

                    // Action for seeding Reports
                    contentCreator.start(ContentCreator.Wrapper.DIV, "", "")
                            .action("<i class='fas fa-chart-pie mr-2'></i>Seed Reports",
                                    new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.BOOTSTRAP_APP.getPath()).get() + "?seedType=reports",
                                    "Admin", user.getRole().getName(),
                                    "bg-purple-500 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded-full flex items-center justify-center space-x-2", "")
                            .end(ContentCreator.Wrapper.DIV);
                    
                    // Action for seeding Users
                    contentCreator.start(ContentCreator.Wrapper.DIV, "", "")
                            .action("<i class='fas fa-chart-pie mr-2'></i>Seed Users",
                                    new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.BOOTSTRAP_APP.getPath()).get() + "?seedType=users",
                                    "Admin", user.getRole().getName(),
                                    "bg-purple-500 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded-full flex items-center justify-center space-x-2", "")
                            .end(ContentCreator.Wrapper.DIV);
                    
                    // Action for seeding All
                    contentCreator.start(ContentCreator.Wrapper.DIV, "", "")
                            .action("<i class='fas fa-seedling mr-2'></i>Seed All",
                                    new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.BOOTSTRAP_APP.getPath()).get() + "?seedType=all",
                                    "Admin", user.getRole().getName(),
                                    "bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded-full flex items-center justify-center space-x-2", "")
                            .end(ContentCreator.Wrapper.DIV);
                    //Truncate all  
                    contentCreator.start(ContentCreator.Wrapper.DIV, "", "")
                            .action("<i class='fas fa-trash-alt mr-2'></i>Truncate All",
                                    new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletFile.BOOTSTRAP_APP.getPath()).get() + "?seedType=truncateAll",
                                    "Admin", user.getRole().getName(),
                                    "bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded-full flex items-center justify-center space-x-2", "")
                            .end(ContentCreator.Wrapper.DIV);
                    String content = contentCreator.getContent();
                    out.print(content);
                %>
            </div>
        </div>
    </body>
</html>
