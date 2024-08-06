<%-- 
    Document   : edit
    Created on : Jul 3, 2024, 7:38:58 PM
    Author     : saleh
--%>
<%@page import="model.JobseekerInfo"%>
<%@page import="model.CustomerInfo"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="middlewares.Guest"%>
<%@page import="helpers.Auth"%>
<%@page import="model.MyUser"%>
<%@page import="model.Model"%>
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
        <title>Update Profile</title>
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
                            .action("<i class='fa fa-key mr-2' aria-hidden='true'></i>Change Password", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.PROFILE.getPath()).add(JspFile.UPDATE_PASSWORD.getPath()).get(), "", "", "mr-2 bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded mr-2 ", "")
                            .action("<i class='fas fa-eye mr-2'></i>View Profile", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.PROFILES.getPath()).add(ServletFile.VIEW.getPath()).get(), "user", "user", "mr-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "")
                            .deleteButtonWithPopup("<i class='fas fa-trash mr-2'></i>Delete Account", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.PROFILES.getPath()).add(ServletFile.DELETE.getPath()).get(), "user", "user", "mr-2 bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded", "")
                            .end(ContentCreator.Wrapper.DIV)
                            .start(ContentCreator.Wrapper.CONTAINER, "relative bg-white p-6 rounded-lg shadow-lg w-full max-w-md mx-auto", "")
                            .header("Update Profile", 2, "", "")
                            .form("POST", "/EPDA_Assignment_SalehAhmed_TP065015-war/Profiles/Update", "", "")
                            .input("name", "text", user.getName(), "", true, "", "")
                            .input("username", "text", user.getUserName(), "", true, "", "")
                            .input("email", "email", user.getEmail(), "", true, "", "")
                            .input("phoneNumber", "text", user.getPhonNumber(), "", true, "", "")
                            .condationalinput("customerAddress", "text", customerInfo != null ? customerInfo.getAddress() : "", "Enter your address", true, "Customer", user.getRole().getName(), "", "")
                            .condationalinput("companyName", "text", customerInfo != null ? customerInfo.getCompanyName() : "", "Enter your company name", true, "Customer", user.getRole().getName(), "", "")
                            .condationalinput("website", "text", customerInfo != null ? customerInfo.getWebsite() : "", "Enter your website", true, "Customer", user.getRole().getName(), "", "")
                            .condationalinput("jobseekerAddress", "text", jobseekerInfo != null ? jobseekerInfo.getAddress() : "", "Enter your address", true, "Jobseeker", user.getRole().getName(), "", "")
                            .condationalinput("hobbies", "text", jobseekerInfo != null ? jobseekerInfo.getHobbies() : "", "Enter your hobbies", true, "Jobseeker", user.getRole().getName(), "", "")
                            .condationalinput("skills", "text", jobseekerInfo != null ? jobseekerInfo.getSkills() : "", "Enter your skills", true, "Jobseeker", user.getRole().getName(), "", "")
                            .condationalinput("age", "number", jobseekerInfo != null ? String.valueOf(jobseekerInfo.getAge()) : "", "Enter your age", true, "Jobseeker", user.getRole().getName(), "", "")
                            .radio("gender", jobseekerInfo != null ? jobseekerInfo.getGender() : "", "Jobseeker", user.getRole().getName(), "form-radio h-5 w-5 text-blue-600", "")
                            .space(20)
                            .submit("Save Changes", "w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4", "")
                            .end(ContentCreator.Wrapper.FORM)
                            .end(ContentCreator.Wrapper.CONTAINER)
                            .space(20)
                            .getContent()
            );
        %>
    </body>
</html>
