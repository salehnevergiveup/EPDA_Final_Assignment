<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="middlewares.Gate"%>
<%@page import="model.MyUser"%>
<%@page import="helpers.Auth"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="helpers.ContentCreator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <%Gate.authorise(request, response, "Create User");%>
    <jsp:include page="/includes/style.jsp"/> 
    <%
      out.print(NotificationHelper.displayNotifications(request));
    %>
    <style>
        .box {
            background: white;
            padding: 1.5rem;
            border-radius: 0.5rem;
            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
            position: relative;
        }
        .box::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            width: 100%;
            height: 5px;
            background: #3B82F6; /* Tailwind blue-500 */
            animation: slideIn 2s linear;
        }
        @keyframes slideIn {
            from {
                width: 0;
            }
            to {
                width: 100%;
            }
        }
    </style>
</head>
<body>
        <jsp:include page="/includes/nav.jsp"/> 
        <%
            out.println(NotificationHelper.displayNotifications(request));
        %> 
    <%
        MyUser user = Auth.user(request);
        int jobCount = (Integer) request.getAttribute("jobCount");
        int feedbackCount = Integer.parseInt(request.getAttribute("feedbackCount").toString());
        int commentCount =  Integer.parseInt(request.getAttribute("commentCount").toString());
        int warningCount =  Integer.parseInt(request.getAttribute("warningCount").toString());
        int reportCount =  Integer.parseInt(request.getAttribute("reportCount").toString());
        int applicationCount =  Integer.parseInt(request.getAttribute("applicationCount").toString());
        int userCount =  Integer.parseInt(request.getAttribute("userCount").toString());
        int managementCount =  Integer.parseInt(request.getAttribute("managementCount").toString());
        int jobseekerCount =  Integer.parseInt(request.getAttribute("jobseekerCount").toString());
        int customerCount =  Integer.parseInt(request.getAttribute("customerCount").toString());

        ContentCreator contentCreator = new ContentCreator();
        contentCreator.start(ContentCreator.Wrapper.CONTAINER, "px-4 lg:w-3/5 mx-auto", "")
            .start(ContentCreator.Wrapper.DIV, "grid grid-cols-1 md:grid-cols-2 gap-4", "")
            .start(ContentCreator.Wrapper.DIV, "box", "")
                .paragraph("Total Users: " + userCount, "text-lg font-semibold", "")
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.DIV, "box", "")
                .paragraph("Number of Jobs: " + jobCount, "text-lg font-semibold", "")
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.DIV, "box", "")
                .paragraph("Number of Feedback: " + feedbackCount, "text-lg font-semibold", "")
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.DIV, "box", "")
                .paragraph("Number of Comments: " + commentCount, "text-lg font-semibold", "")
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.DIV, "box", "")
                .paragraph("Number of Warnings: " + warningCount, "text-lg font-semibold", "")
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.DIV, "box", "")
                .paragraph("Number of Reports: " + reportCount, "text-lg font-semibold", "")
            .end(ContentCreator.Wrapper.DIV)
            .start(ContentCreator.Wrapper.DIV, "box", "")
                .paragraph("Number of Applications: " + applicationCount, "text-lg font-semibold", "")
            .end(ContentCreator.Wrapper.DIV)
            .end(ContentCreator.Wrapper.DIV)
            .end(ContentCreator.Wrapper.CONTAINER);

        String content = contentCreator.getContent();
    %>

    <div class="flex mt-16 container mx-auto">
        <%= content %>

        <div class="mt-8">
            <h2 class="text-2xl font-bold text-center">User Distribution</h2>
            <div class="flex justify-center">
                <canvas id="userChart" class="mt-4" style="max-width: 400px;"></canvas>
            </div>
        </div>

        <script>
            var ctx = document.getElementById('userChart').getContext('2d');
            new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: ['Management', 'Job Seekers', 'Customers'],
                    datasets: [{
                        data: [<%= managementCount %>, <%= jobseekerCount %>, <%= customerCount %>],
                        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56']
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false
                }
            });
        </script>
    </div>
</body>
</html>