<%-- 
    Document   : profile
    Created on : Jul 2, 2024, 8:18:41 PM
    Author     : saleh
--%>
<%@page import="model.JobseekerInfo"%>
<%@page import="model.CustomerInfo"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Route"%>
<%@page import="middlewares.Gate"%>
<%@page import="middlewares.Guest"%>
<%@page import="model.MyUser"%>
<%@page import="helpers.Auth"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="helpers.HttpHelper"%>  
<%@page import="helpers.ContentCreator" %>
<%@page import="middlewares.RedirectAfterLogin"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profile</title>
        <jsp:include page="../includes/style.jsp"/> 
    </head>
    <body>
        <% Guest.authorise(request, response);%>
        <jsp:include page="../includes/nav.jsp"/> 
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            MyUser user = Auth.user(request);
            CustomerInfo customerInfo = (CustomerInfo) request.getAttribute("customerInfo");
            JobseekerInfo jobseekerInfo = (JobseekerInfo) request.getAttribute("jobseekerInfo");
            out.println(
                    new ContentCreator()
                            .start(ContentCreator.Wrapper.DIV, "flex justify-end p-4", "")
                            .deleteButtonWithPopup("<i class='fas fa-trash mr-2'></i>Delete Account", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.PROFILES.getPath()).add(ServletFile.DELETE.getPath()).get(), "true", !user.getRole().getName().equals("Admin") + "", "mr-2 bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded", "")
                            .action("<i class='fas fa-edit mr-2'></i>Update", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.PROFILES.getPath()).add(ServletFile.UPDATE.getPath()).get(), "", "", "mr-2 bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded mr-2 ", "")
                            .end(ContentCreator.Wrapper.DIV)
                            .start(ContentCreator.Wrapper.CONTAINER, "relative bg-white p-6 rounded-lg shadow-lg w-full max-w-md mx-auto", "")
                            .start(ContentCreator.Wrapper.DIV, "text-center", "")
                            .paragraph("<img src='https://via.placeholder.com/150' class='w-24 h-24 rounded-full mx-auto' alt='Profile Picture'>", "", "")
                            .header(user.getName(), 3, "text-xl font-semibold mt-4", "")
                            .end(ContentCreator.Wrapper.DIV)
                            .space(10)
                            .start(ContentCreator.Wrapper.DIV, "", "")
                            .paragraph("Name: " + user.getName(), "text-left text-lg text-gray-700 mt-4 border-b border-gray-200 pb-2", "")
                            .paragraph("Username: " + user.getUserName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Email: " + user.getEmail(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Phone Number: " + user.getPhonNumber(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Role: " + user.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Address: " + (customerInfo != null ? customerInfo.getAddress() : ""), "Customer", user.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Company Name: " + (customerInfo != null ? customerInfo.getCompanyName() : ""), "Customer", user.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Website: " + (customerInfo != null ? customerInfo.getWebsite() : ""), "Customer", user.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Age: " + (jobseekerInfo != null ? jobseekerInfo.getAge(): ""), "Jobseeker", user.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Address: " + (jobseekerInfo != null ? jobseekerInfo.getAddress() : ""), "Jobseeker", user.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Hobbies: " + (jobseekerInfo != null ? jobseekerInfo.getHobbies() : ""), "Jobseeker", user.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Gender: " + (jobseekerInfo != null ? jobseekerInfo.getGender() : ""), "Jobseeker", user.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Skills: " + (jobseekerInfo != null ? jobseekerInfo.getSkills() : ""), "Jobseeker", user.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .end(ContentCreator.Wrapper.DIV)
                            .end(ContentCreator.Wrapper.CONTAINER)
                            .getContent()
            );
        %>
    </div>

</body>
</html>
