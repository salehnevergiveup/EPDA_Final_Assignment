<%-- 
    Document   : update
    Created on : Jul 3, 2024, 9:11:31 PM
    Author     : saleh
--%>
<%@page import="model.JobseekerInfo"%>
<%@page import="model.CustomerInfo"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.AccountStatus"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Auth"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="middlewares.Gate"%>
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
        <title>Update user</title>
        <jsp:include page="../includes/style.jsp"/> 
    </head>
    <body>
        <% Gate.authorise(request, response, "Update User"); %> 
        <jsp:include page="../includes/nav.jsp"/> 
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            MyUser user = Auth.user(request);
            MyUser user1 = (MyUser) request.getAttribute("user");
            CustomerInfo customerInfo = (CustomerInfo) request.getAttribute("customerInfo");
            JobseekerInfo jobseekerInfo = (JobseekerInfo) request.getAttribute("jobseekerInfo");

            out.println(
                    new ContentCreator()
                            .start(ContentCreator.Wrapper.DIV, "flex justify-end p-4", "")
                            .deleteButtonWithPopup("<i class='fas fa-trash mr-2'></i>Delete User", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.DELETE.getPath()).get() + "?id=" + user1.getId(), "", "", "bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-2 rounded mr-2", "")
                            .action("<i class='fas fa-eye mr-2'></i>View User", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.VIEW.getPath()).get() + "?id=" + user1.getId(), "", "", "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-2 rounded mr-2", "")
                            .action("<i class='fas fa-plus mr-2'></i>Create User", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get(), "", "", "bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-2 rounded", "")
                            .end(ContentCreator.Wrapper.DIV)
                            .start(ContentCreator.Wrapper.DIV, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg", "")
                            .header("Update User", 2, "text-2xl font-bold mb-6 text-center", "")
                            .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?id=" + user1.getId(), "space-y-4", "")
                            .input("name", "text", user1.getName(), "", false, "", "")
                            .space(10)
                            .input("userName", "text", user1.getUserName(), "", false, "", "")
                            .space(10)
                            .input("password", "password", "", "Enter your password", false, "", "")
                            .space(10)
                            .input("confPassword", "password", "", "Enter your confirmation password", false, "", "")
                            .space(10)
                            .input("email", "email", user1.getEmail(), "Enter your email", false, "", "")
                            .condationalinput("customerAddress", "text", customerInfo != null ? customerInfo.getAddress() : "", "Enter your address", true, "Customer", user1.getRole().getName(), "", "")
                            .condationalinput("companyName", "text", customerInfo != null ? customerInfo.getCompanyName() : "", "Enter your company name", true, "Customer", user1.getRole().getName(), "", "")
                            .condationalinput("website", "text", customerInfo != null ? customerInfo.getWebsite() : "", "Enter your website", true, "Customer", user1.getRole().getName(), "", "")
                            .condationalinput("jobseekerAddress", "text", jobseekerInfo != null ? jobseekerInfo.getAddress() : "", "Enter your address", true, "Jobseeker", user1.getRole().getName(), "", "")
                            .condationalinput("hobbies", "text", jobseekerInfo != null ? jobseekerInfo.getHobbies() : "", "Enter your hobbies", true, "Jobseeker", user1.getRole().getName(), "", "")
                            .condationalinput("skills", "text", jobseekerInfo != null ? jobseekerInfo.getSkills() : "", "Enter your skills", true, "Jobseeker", user1.getRole().getName(), "", "")
                            .condationalinput("age", "number", jobseekerInfo != null ? String.valueOf(jobseekerInfo.getAge()): "", "", true, "Jobseeker", user1.getRole().getName(), "", "")
                            .radio("gender", jobseekerInfo != null ? jobseekerInfo.getGender() : "", "Jobseeker", user1.getRole().getName(), "form-radio h-5 w-5 text-blue-600", "")
                            .space(10)
                            .select("status", Arrays.asList(AccountStatus.ACTIVE.getStatus(), AccountStatus.PENDING.getStatus(), AccountStatus.REJECTED.getStatus(), AccountStatus.SUSPENDED.getStatus()), "", "", "")
                            .space(20)
                            .submit("Update", "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded w-full", "")
                            .end(ContentCreator.Wrapper.FORM)
                            .end(ContentCreator.Wrapper.DIV)
                            .space(20)
                            .getContent()
            );
        %>
    </body>
</html>
