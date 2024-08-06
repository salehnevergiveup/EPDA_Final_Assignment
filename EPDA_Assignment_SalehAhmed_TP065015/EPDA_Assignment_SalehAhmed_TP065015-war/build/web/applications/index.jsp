<%-- 
    Document   : index
    Created on : Jul 4, 2024, 2:56:43 PM
    Author     : saleh
--%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="helpers.Auth"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="java.util.Map"%>
<%@page import="helpers.HttpHelper"%>
<%@page import="controllers.enums.ApplicationStatus"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.ContentCreator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="model.Model"%>
<%@page import="model.Application"%>
<%@page import="model.MyJob"%>
<%@page import="model.MyUser"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Applications</title>
        <jsp:include page="../includes/style.jsp"/>
    </head>

    <%
        String status = HttpHelper.getParam(request, "type");
        if (status == null || status.isEmpty()) {
            status = "All";
        }
    %>
<body x-data="{ activeTable: '<%=status%>' }">

        <jsp:include page="../includes/nav.jsp"/> 
        <%
           out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            List<Map<String, String>> applications = (List<Map<String, String>>) request.getAttribute("applications");
            MyUser user = Auth.user(request);
            List<List<String>> actions = Arrays.asList(
                    Arrays.asList("", "<i class='fas fa-eye mr-2'></i>View Application", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.VIEW.getPath()).get(), "", "", "false", "text-blue-500 hover:underline", ""),
                    Arrays.asList("", "<i class='fas fa-check-circle mr-2'></i>Accept", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type=" + ApplicationStatus.APPROVED.getStatus(), "Customer"+(status.equals(ApplicationStatus.APPROVED.getStatus())),  user.getRole().getName()+"false", "false", "text-green-500 hover:underline", ""),
                    Arrays.asList("", "<i class='fas fa-check-circle mr-2'></i>Pendding", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type=" + ApplicationStatus.PENDING.getStatus(), "Customer"+(status.equals(ApplicationStatus.PENDING.getStatus())),  user.getRole().getName()+"false", "false", "text-green-500 hover:underline", ""),
                    Arrays.asList("", "<i class='fas fa-times-circle mr-2'></i>Reject", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type=" + ApplicationStatus.REJECTED.getStatus(), "Customer"+(status.equals(ApplicationStatus.REJECTED.getStatus())),  user.getRole().getName()+"false", "false", "text-red-500 hover:underline", ""),
                    Arrays.asList("", "<i class='fas fa-check-circle mr-2'></i>Accept Offer", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type=" + ApplicationStatus.INTERVIEW.getStatus(), "Jobseeker" + (status.equals(ApplicationStatus.APPROVED.getStatus())), user.getRole().getName() + "true", "false", "text-green-500 hover:underline", ""),
                    Arrays.asList("", "<i class='fas fa-times-circle mr-2'></i>Reject Offer", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?Type="+ApplicationStatus.DELETED.getStatus(), "Jobseeker" + (status.equals(ApplicationStatus.APPROVED.getStatus())), user.getRole().getName() + "true", "false", "text-red-500 hover:underline", "")
            );
            if(ApplicationStatus.INTERVIEW.getStatus().equals(status)) actions = Arrays.asList(
                    Arrays.asList("", "<i class='fas fa-eye mr-2'></i>View Application", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.VIEW.getPath()).get(), "", "", "false", "text-blue-500 hover:underline", "")
            );
        %>
        <div class="relative bg-white p-6 rounded-lg shadow-lg w-full max-w-4xl mx-auto mt-16" x-data="{ activeTable: '
             ' }">
            <h2 class="text-center text-2xl text-gray-600 font-bold mb-6">Applications</h2>
            <div class="flex justify-center space-x-4 mb-6">
                <div class="rounded-lg px-4 bg-gray-100">
                <a href="<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get()%>?type=All" 
                       :class="{'bg-gray-300 text-gray-800': activeTable === 'All', 'text-gray-800': activeTable !== 'All'}"
                       class="px-4 py-2 rounded border-none hover:bg-gray-300 focus:outline-none focus:border-none active:bg-gray-300">
                       All
                </a>
                <a href="<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get()%>?type=<%= ApplicationStatus.PENDING.getStatus()%>"
                   :class="{'bg-gray-300 text-gray-800': activeTable === '<%= ApplicationStatus.PENDING.getStatus()%>', 'text-gray-800': activeTable !== '<%= ApplicationStatus.PENDING.getStatus()%>'}"
                   class="px-4 py-2 rounded border-none hover:bg-gray-300 focus:outline-none focus:border-none active:bg-gray-300">
                   Pending
                </a>
                <a href="<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get()%>?type=<%= ApplicationStatus.APPROVED.getStatus()%>"
                   :class="{'bg-gray-300 text-gray-800': activeTable === '<%= ApplicationStatus.APPROVED.getStatus()%>', 'text-gray-800': activeTable !== '<%= ApplicationStatus.APPROVED.getStatus()%>'}"
                   class="px-4 py-2 rounded border-none hover:bg-gray-300 focus:outline-none focus:border-none active:bg-gray-300">
                        <%if (user.getRole().getName().equals("Customer")) {%>
                        Accepted<%} else { %>
                        Offers <% } %>
                </a>
                <a href="<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get()%>?type=<%= ApplicationStatus.REJECTED.getStatus()%>"
                   :class="{'bg-gray-300 text-gray-800': activeTable === '<%= ApplicationStatus.REJECTED.getStatus()%>', 'text-gray-800': activeTable !== '<%= ApplicationStatus.REJECTED.getStatus()%>'}"
                   class="px-4 py-2 rounded border-none hover:bg-gray-300 focus:outline-none focus:border-none active:bg-gray-300">
                   Rejected
                </a>
                <a href="<%= new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get()%>?type=<%= ApplicationStatus.INTERVIEW.getStatus()%>"
                      :class="{'bg-gray-300 text-gray-800': activeTable === '<%= ApplicationStatus.INTERVIEW.getStatus()%>', 'text-gray-800': activeTable !== '<%= ApplicationStatus.INTERVIEW.getStatus()%>'}"
                      class="px-4 py-2 rounded border-none hover:bg-gray-300 focus:outline-none focus:border-none active:bg-gray-300">
                      Interview
                </a>
            </div> 
       </div>
<%  out.println(
        new ContentCreator()
            .start(ContentCreator.Wrapper.DIV, "", "")
            .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get() + "?type=" + status, "flex w-1/2 mx-auto flex-col", "")
            .input("query", "text", "", "query", true, "", "")
            .start(ContentCreator.Wrapper.DIV, "flex w-full justify-center mt-2", "")
            .action("search", "", "", "", "", "margin-right: 5px")
            .link("Reset", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.APPLICATIONS.getPath()).add(ServletFile.INDEX.getPath()).get() + "?type=" + status, "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "", true)
            .end(ContentCreator.Wrapper.DIV)
            .end(ContentCreator.Wrapper.FORM)
            .end(ContentCreator.Wrapper.DIV)
            .table(
                    "Applications",
                    Arrays.asList("Job_Seeker", "Job_Title", "Created_Date", "Status", "Actions"),
                    applications,
                    actions,
                    "min-w-full leading-normal",
                    ""
            )
            .space(20)
            .getContent()
    );
%>
</div>
</div>
</body>
</html>