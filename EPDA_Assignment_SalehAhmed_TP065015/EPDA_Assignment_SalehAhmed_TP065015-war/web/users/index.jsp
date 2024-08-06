<%-- 
    Document   : index
    Created on : Jul 3, 2024, 9:11:05 PM
    Author     : saleh
--%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="middlewares.Gate"%>
<%@page import="controllers.enums.AccountStatus"%>
<%@page import="controllers.enums.ApplicationStatus"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Auth"%>
<%@page import="java.util.Map"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.EJB.MyUserFacade"%>
<%@page import="model.Model"%>
<%@page import="model.MyUser"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="helpers.HttpHelper"%>  
<%@page import="helpers.ContentCreator" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Users</title>
        <jsp:include page="../includes/style.jsp"/> 
    </head>
    <%
        String status = HttpHelper.getParam(request, "type");
        if (status == null || status.isEmpty()) {
            status = "All";
        }
    %>
    <body x-data="{ activeTable: '<%=status%>' }">
        <% Gate.authorise(request, response, "Read User");%>
        <jsp:include page="../includes/nav.jsp"/> 
        <%
         out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            List<Map<String, String>> users = (List<Map<String, String>>) request.getAttribute("users");
            MyUser user = Auth.user(request);
            List<List<String>> actions = Arrays.asList(
                    Arrays.asList("", "<i class='fas fa-eye mr-2'></i>View User", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.VIEW.getPath()).get(), "", "", "false", "text-blue-500 ", ""),
                    Arrays.asList("", "<i class='fas fa-edit mr-2'></i>Update", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.UPDATE.getPath()).get(), (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) +"", "true", "true", "text-yellow-500", ""),
                    Arrays.asList("", "<i class='fas fa-check-circle mr-2'></i>Accept", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type=" + AccountStatus.ACTIVE.getStatus(),(user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) + ""+ (status.equals(AccountStatus.ACTIVE.getStatus())), "true" + "false", "false", "text-green-500 ", ""),
                    Arrays.asList("", "<i class='fas fa-hourglass-half mr-2'></i>Awaiting", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type=" + AccountStatus.PENDING.getStatus(), (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) + ""+ (status.equals(AccountStatus.PENDING.getStatus())), "true" + "false", "false", "text-orange-500 ", ""),
                    Arrays.asList("", "<i class='fas fa-times-circle mr-2'></i>Reject", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type=" + AccountStatus.REJECTED.getStatus(), (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) + ""+ (status.equals(AccountStatus.REJECTED.getStatus())), "true" + "false", "false", "text-red-500 ", ""),
                    Arrays.asList("", "<i class='fas fa-pause-circle mr-2'></i>Suspend", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type=" + AccountStatus.SUSPENDED.getStatus(), (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) + ""+ (status.equals(AccountStatus.SUSPENDED.getStatus())), "true" + "false", "false", "text-red-500 ", "")
            );
            List<String> tableHeader = Arrays.asList("User", "User_Name", "Email", "Status", "Actions");
            if (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) {
               tableHeader = Arrays.asList("User", "User_Name", "Email", "Created_Date", "Updated_Date","Status", "Actions");
            }

        %>
        <%if (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) {
                out.println(
                        new ContentCreator()
                                .start(ContentCreator.Wrapper.DIV, "flex justify-end mb-4 mr-2", "")
                                .action("<i class='fas fa-plus mr-2'></i>Create User", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get(), (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) + "", "true", "bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-2 rounded", "")
                                .end(ContentCreator.Wrapper.DIV)
                                .getContent()
                );
        %>
        <div class="relative bg-white p-6 rounded-lg shadow-lg w-full max-w-4xl mx-auto mt-16" x-data="{ activeTable: '
             ' }">

            <h2 class="text-center text-2xl text-gray-600 font-bold mb-6">Users</h2>
            <div class="flex justify-center space-x-4 mb-6">
                <div class="rounded-lg px-4 bg-gray-100">
                <a href="<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get()%>?type=All" 
                       :class="{'bg-gray-300 text-gray-800': activeTable === 'All', 'text-gray-800': activeTable !== 'All'}"
                       class="px-4 py-2 rounded border-none hover:bg-gray-300 focus:outline-none focus:border-none active:bg-gray-300">
                       All
                </a>
                <a href="<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get()%>?type=<%= ApplicationStatus.PENDING.getStatus()%>"
                   :class="{'bg-gray-300 text-gray-800': activeTable === '<%= AccountStatus.PENDING.getStatus()%>', 'text-gray-800': activeTable !== '<%= AccountStatus.PENDING.getStatus()%>'}"
                   class="px-4 py-2 rounded border-none hover:bg-gray-300 focus:outline-none focus:border-none active:bg-gray-300">
                   Pending
                </a>
                <a href="<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get()%>?type=<%= AccountStatus.ACTIVE.getStatus()%>"
                   :class="{'bg-gray-300 text-gray-800': activeTable === '<%= AccountStatus.ACTIVE.getStatus()%>', 'text-gray-800': activeTable !== '<%= AccountStatus.ACTIVE.getStatus()%>'}"
                   class="px-4 py-2 rounded border-none hover:bg-gray-300 focus:outline-none focus:border-none active:bg-gray-300">
                        Accepted
                </a>
                <a href="<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get()%>?type=<%= AccountStatus.REJECTED.getStatus()%>"
                   :class="{'bg-gray-300 text-gray-800': activeTable === '<%= AccountStatus.REJECTED.getStatus()%>', 'text-gray-800': activeTable !== '<%= AccountStatus.REJECTED.getStatus()%>'}"
                   class="px-4 py-2 rounded border-none hover:bg-gray-300 focus:outline-none focus:border-none active:bg-gray-300">
                   Rejected
                </a>
                <a href="<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get()%>?type=<%= AccountStatus.SUSPENDED.getStatus()%>"
                      :class="{'bg-gray-300 text-gray-800': activeTable === '<%=  AccountStatus.SUSPENDED.getStatus()%>', 'text-gray-800': activeTable !== '<%=  AccountStatus.SUSPENDED.getStatus()%>'}"
                      class="px-4 py-2 rounded border-none hover:bg-gray-300 focus:outline-none focus:border-none active:bg-gray-300">
                      Suspended
                </a>
            </div> 
       </div>
     <%}%>
<%  out.println(
        new ContentCreator()
            .start(ContentCreator.Wrapper.DIV, "", "")
            .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get() + "?type=" + status, "flex w-1/2 mx-auto flex-col", "")
            .input("query", "text", "", "query", true, "", "")
            .start(ContentCreator.Wrapper.DIV, "flex w-full justify-center mt-2", "")
            .action("search", "", "", "", "", "margin-right: 5px")
            .link("Reset", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.INDEX.getPath()).get() + "?type=" + status, "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "", true)
            .end(ContentCreator.Wrapper.DIV)
            .end(ContentCreator.Wrapper.FORM)
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.DIV, "", "")
            .table(
                    "Users",
                    tableHeader,
                    users,
                    actions,
                    "min-w-full leading-normal",
                    ""
            )
            .space(20)
            .end(ContentCreator.Wrapper.DIV)
            .getContent()
    );
%>
</div>
</div>
    </body>
</html>
