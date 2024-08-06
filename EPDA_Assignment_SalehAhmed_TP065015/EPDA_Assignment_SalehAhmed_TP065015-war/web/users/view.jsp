<%@page import="model.JobseekerInfo"%>
<%@page import="model.CustomerInfo"%>
<%@page import="middlewares.Gate"%>
<%@page import="helpers.NotificationHelper"%>
<%@page import="controllers.enums.JspPackage"%>
<%@page import="controllers.enums.JspFile"%>
<%@page import="controllers.enums.FeedbackType"%>
d<%-- 
    Document   : view
    Created on : Jul 3, 2024, 9:11:13 PM
    Author     : saleh
--%>
<%@page import="controllers.enums.ServletFile"%>
<%@page import="controllers.enums.ServletPackage"%>
<%@page import="controllers.enums.BaseRoute"%>
<%@page import="helpers.Route"%>
<%@page import="helpers.Route"%>
<%@page import="model.Comment"%>
<%@page import="helpers.Auth"%>
<%@page import="model.MyUser"%>
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
        <title>view user</title>
        <jsp:include page="../includes/style.jsp"/> 
    </head>
    <body x-data="{ activeTable: 'applications' }">
        <% Gate.authorise(request, response, "Read User");%>
        <jsp:include page="../includes/nav.jsp"/> 
        <%
            out.print(NotificationHelper.displayNotifications(request));
        %>
        <%
            MyUser user = Auth.user(request);
            MyUser user1 = (MyUser) request.getAttribute("user");
            List<Comment> comments = (List<Comment>) request.getAttribute("comments");
            CustomerInfo customerInfo = (CustomerInfo) request.getAttribute("customerInfo");
            JobseekerInfo jobseekerInfo = (JobseekerInfo) request.getAttribute("jobseekerInfo");

            out.println(
                    new ContentCreator()
                            .start(ContentCreator.Wrapper.DIV, "flex justify-end p-4", "")
                            .deleteButtonWithPopup("<i class='fas fa-trash mr-2'></i>Delete User", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.DELETE.getPath()).get() + "?id=" + user1.getId(), (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) + "", "true", "bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-1 rounded mr-2", "")
                            .action("<i class='fas fa-edit mr-2'></i>Update User", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.USERS.getPath()).add(ServletFile.UPDATE.getPath()).get() + "?id=" + user1.getId(), (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) + "", "true", "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-1 rounded mr-2", "")
                            .action("<i class='fas fa-plus'></i>Create User", new Route().add(BaseRoute.CONTEXT.getPath()).add(JspPackage.USERS.getPath()).add(JspFile.CREATE.getPath()).get(), (user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) + "", "true", "bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-1 rounded", "")
                            .warningButtonWithPopup(" <i class='fas fa-exclamation-triangle mr-2'></i>Send Warnings", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.WARNINGS.getPath()).add(ServletFile.CREATE.getPath()).get() + "?id=" + user1.getId(), ((user.getRole().getName().equals("Management") || user.getRole().getName().equals("Admin")) && user1.getRole().getName().equals("Jobseeker")) + "", "true", "bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-1 rounded ml-2", "")
                            .end(ContentCreator.Wrapper.DIV)
                            .space(20)
                            .start(ContentCreator.Wrapper.DIV, "max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg", "")
                            .start(ContentCreator.Wrapper.DIV, "text-center", "")
                            .paragraph("<img src='https://via.placeholder.com/150' class='w-24 h-24 rounded-full mx-auto' alt='Profile Picture'>", "", "")
                            .header(user1.getName(), 3, "text-xl font-semibold mt-4", "")
                            .end(ContentCreator.Wrapper.DIV)
                            .space(10)
                            .start(ContentCreator.Wrapper.DIV, "", "")
                            .paragraph("Name: " + user1.getName(), "text-left text-lg text-gray-700 mt-4 border-b border-gray-200 pb-2", "")
                            .paragraph("Username: " + user1.getUserName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Email: " + user1.getEmail(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Phone Number: " + user1.getPhonNumber(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Role: " + user1.getRole().getName(), "text-left text-lg text-gray-700 mt-2", "")
                            .condationalparagraph("Company Name: " + (customerInfo != null ? customerInfo.getCompanyName() : ""), "Customer", user1.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Address: " + (customerInfo != null ? customerInfo.getAddress() : ""), "Customer", user1.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Website: " + (customerInfo != null ? customerInfo.getWebsite() : ""), "Customer", user1.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Address: " + (jobseekerInfo != null ? jobseekerInfo.getAddress() : ""), "Jobseeker", user1.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Age: " + (jobseekerInfo != null ? jobseekerInfo.getAge() : ""), "Jobseeker", user1.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Hobbies: " + (jobseekerInfo != null ? jobseekerInfo.getHobbies() : ""), "Jobseeker", user1.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Gender: " + (jobseekerInfo != null ? jobseekerInfo.getGender() : ""), "Jobseeker", user1.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .condationalparagraph("Skills: " + (jobseekerInfo != null ? jobseekerInfo.getSkills() : ""), "Jobseeker", user1.getRole().getName(), "text-left text-lg text-gray-700 mt-2 border-b border-gray-200 pb-2", "")
                            .paragraph("Account Status: " + user1.getStatus(), "text-left text-lg text-gray-700 mt-2", "")
                            .end(ContentCreator.Wrapper.DIV)
                            .end(ContentCreator.Wrapper.CONTAINER)
                            .getContent()
            );
        %>

        <%//Feedback Form %>  
        <% 
            if (user1.getRole().getName().equals("Jobseeker") && user.getRole().getName().equals("Customer")) {
                out.println(
                        new ContentCreator()
                                .space(50)
                                .start(ContentCreator.Wrapper.DIV, "relative max-w-lg mx-auto bg-white p-4 rounded-lg shadow-lg", "")
                                .form("POST", new Route().add(BaseRoute.CONTEXT.getPath()).add(ServletPackage.FEEDBACK.getPath()).add(ServletFile.CREATE.getPath()).get() + "?id=" + user1.getId(), "", "")
                                .header("Submit Feedback", 2, "text-center text-2xl text-gray-600 font-bold mb-6", "")
                                .select("type", Arrays.asList(FeedbackType.NEGATIVE.getType(), FeedbackType.POSITIVE.getType()), "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "", "")
                                .textArea("feedbackContent", "Enter your feedback...", "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline", "")
                                .submit("Submit", "w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "background-color: #3b82f6")
                                .end(ContentCreator.Wrapper.FORM)
                                .end(ContentCreator.Wrapper.DIV)
                                .space(50)
                                .getContent()
                );
            }
        %>

        <% //Comments %>
        <%if(user1.getRole().getName().equals("Customer")) {%>
        <div x-data="{ showNewCommentForm: false, rating: 0 }">
            <% if (user.getRole().getName().equals("Jobseeker") && user1.getRole().getName().equals("Customer")) { %>
            <div class="text-center">
                <button @click="showNewCommentForm = !showNewCommentForm" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
                    <i class='fas fa-plus mr-2'></i>Add Comment
                </button>
            </div>
            <% }%>
            <div class="max-w-lg mx-auto bg-white p-8 rounded-lg shadow-lg my-4 overflow-y-auto" style="max-height: 400px">
                <div x-show="showNewCommentForm">
                    <h2 class="text-center text-2xl text-gray-600 font-bold mb-6">New Comment</h2>
                    <%System.out.println("customer id for view page is: "+ user1.getId() ); %>
                    <form method="POST" action="/EPDA_Assignment_SalehAhmed_TP065015-war/Comments/Create?id=<%=user1.getId()%>" class="space-y-6">
                        <textarea name="commentContent" required placeholder="Comment" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"></textarea>
                        <div class="flex mb-4 justify-center">
                            <template x-for="star in [1, 2, 3, 4, 5]" :key="star">
                                <i @click="rating = star" :class="{'text-yellow-500': rating >= star, 'text-gray-400': rating < star }" class="fas fa-star text-2xl cursor-pointer mx-1"></i>
                            </template>
                        </div>
                        <input type="hidden" name="rating" :value="rating">
                        <div class="text-center">
                            <button style="background-color: blue" type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-1 rounded">
                                <i class='fas fa-paper-plane mr-2'></i>Submit
                            </button>
                        </div>
                    </form>
                </div>

                <% for (Comment comment : comments) {%>
                <div class="space-y-4 mt-10">
                    <div class="border p-4 rounded-lg" x-data="{ editing: false, commentContent: '<%= comment.getContent()%>', showModal: false }">
                        <div class="flex">
                            <div class="text-sm text-gray-600 mr-2">
                                <a href="/EPDA_Assignment_SalehAhmed_TP065015-war/Comments/View?id=<%= comment.getJobSeeker().getId()%>">
                                    <div>Name: <%= comment.getJobSeeker().getName()%></div>
                                </a>
                            </div>
                            <div class="text-sm text-gray-600">
                                Time: <%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getCreatedAt())%>
                            </div>
                        </div>
                        <div class="flex mb-4 justify-center">
                            <template x-for="star in [1, 2, 3, 4, 5]" :key="star">
                                <i :class="{'text-yellow-500': <%= comment.getRating()%> >= star, 'text-gray-400': <%= comment.getRating()%> < star }" class="fas fa-star text-2xl mx-1"></i>
                            </template>
                        </div>
                        <div class="flex justify-between items-center space-x-2">
                            <p class="mr-2 text-lg text-gray-700 flex-1 overflow-hidden overflow-ellipsis whitespace-nowrap" x-show="!editing" x-text="commentContent.split(' ').slice(0, 10).join(' ') + (commentContent.split(' ').length > 10 ? '...' : '')"></p>
                            <div class="flex">
                                <% if (user.getRole().getName().equals("Jobseeker") && user.getId() == comment.getJobSeeker().getId()) { %>
                                <div class="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-1 px-1 mr-1 text-center rounded cursor-pointer" x-show="!editing" @click="editing = true">
                                    <i class='fas fa-edit'></i>
                                </div>
                                <% }%>
                                <div class="bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-1 text-center rounded cursor-pointer" x-show="!editing && commentContent.split('').length > 40" @click="showModal = true">
                                    <i class='fas fa-eye'></i>
                                </div>
                            </div>
                        </div>
                        <form method="POST" action="/EPDA_Assignment_SalehAhmed_TP065015-war/Comments/Update?id=<%= comment.getId()%>" x-show="editing">
                            <textarea name="content" class="shadow appearance-none border rounded w-full py-1 px-1 text-gray-700 leading-tight focus:outline-none focus:shadow-outline mt-2" x-model="commentContent"></textarea>
                            <div class="flex justify-center mt-2 space-x-2" x-show="editing">
                                <button type="submit" style="background-color: blue" class="w-24 bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-1 rounded">
                                    <i class='fas fa-paper-plane mr-2'></i>Submit
                                </button>
                                <div type="button" style="background-color: red" class="w-24 bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-1 rounded cursor-pointer" @click="editing = false; commentContent='<%= comment.getContent()%>'">
                                    <i class='fas fa-trash mr-2'></i>Cancel
                                </div>
                            </div>
                        </form>
                        <div x-show="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                            <div class="bg-white p-4 rounded-lg">
                                <h2 class="text-lg font-bold mb-2">Full Comment</h2>
                                <p class="text-gray-700 mb-4" x-text="commentContent"></p>
                                <button class="bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-2 rounded" @click="showModal = false">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <% }%>
            </div>
        </div>
        <%}%> 
    </body>
</html>
